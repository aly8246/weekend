package com.github.aly8246.core.template

import com.github.aly8246.core.annotation.Mapping
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.util.PrintImpl
import com.github.aly8246.core.util.WordUtil
import net.sf.jsqlparser.parser.CCJSqlParserManager
import org.springframework.util.StringUtils
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Collectors.toList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class RegexTemplate : BaseTemplate() {
    override fun replaceParam(sourceCommand: String, paramMap: MutableMap<String, Any?>): String {
        if (configuration.showCommand!!)
            PrintImpl().debug("originalCommand >>   $sourceCommand")
        var command = ""

        if (sourceCommand.startsWith("SELECT") || sourceCommand.startsWith("select") || sourceCommand.startsWith("Select")
                || sourceCommand.startsWith("DELETE") || sourceCommand.startsWith("delete") || sourceCommand.startsWith("Delete")
                || sourceCommand.startsWith("UPDATE") || sourceCommand.startsWith("update") || sourceCommand.startsWith("Update")
        ) {
            //替换普通参数
            command = this.processSimpleTemplate(sourceCommand, paramMap, "\$\\{.*?\\}")
            command = this.processSimpleTemplate(command, paramMap, "\\#\\{.*?\\}")
            //执行when条件判断
            command = processConditionTemplate(command, paramMap)
        } else if (sourceCommand.startsWith("INSERT") || sourceCommand.startsWith("insert") || sourceCommand.startsWith("Insert")) {
            //为insert方法处理参数
            command = this.processInsertValue(sourceCommand, paramMap)
        }

        //如果发生参数为空，则回退set|where|and 条件匹配
        command = this.processNullParam(command)
        //如果用户强制需要查询为空字符串''
        command = this.processEmptyString(command)
        if (configuration.showCommand!!)
            PrintImpl().debug("analyzedCommand >>   $command")
        return command
    }

    protected fun processNullParam(command: String): String {
        val matcher = Pattern.compile("(set|where|and)\\s+\\S+\\s+\\S+\\s+##NULL_PARAM##").matcher(command)
        val sb = StringBuffer()
        while (matcher.find()) {
            PrintImpl().warning("存在参数为空的字段，强制取消执行条件 >> ${matcher.group()}")
            matcher.appendReplacement(sb, "")
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    //where name = #{param1}
    //where name = ''''
    protected fun processEmptyString(command: String): String {
        val matcher = Pattern.compile("##EMPTY_STRING##").matcher(command)
        val sb = StringBuffer()
        while (matcher.find()) {
            matcher.appendReplacement(sb, "''")
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    private fun processInsertValue(sourceCommand: String, paramMap: MutableMap<String, Any?>): String {
        // if (paramMap.size > 1 || paramMap.isEmpty()) throw WeekendException("要新增数据的时候参数应当是一个类,用来承载参数")

        //step1. 判断是单个插入的sql还是多个插入的sql
        val insertColumn = this.insertColumn(sourceCommand)
        val paramName = paramMap["paramName"]
        val paramTypeName = paramMap["paramTypeName"]

        val insertValueList: MutableList<String> = mutableListOf()
        if (paramMap["isBatch"] != null) {
            for (index in 0 until paramMap["batchSize"].toString().toInt()) {
                val buildInsertValue = buildInsertValue(insertColumn, paramMap["$index.$paramTypeName"])
                insertValueList.add(buildInsertValue)
            }
        } else {
            insertValueList.add(buildInsertValue(insertColumn, paramMap["$paramTypeName"]))
        }

        val sb = StringBuffer()
        val matcher = Pattern.compile("#\\{$paramName}").matcher(sourceCommand)
        while (matcher.find()) {
            matcher.appendReplacement(sb, insertValueList.joinToString(","))
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    //根据要新增的行.从参数里拿出数据
    private fun buildInsertValue(columnList: MutableList<String>, paramValue: Any?): String {
        val paramValueClass = paramValue!!::class.java
        val declaredFields = paramValueClass.declaredFields

        //paramValue是传递的参数类,依次取出参数值
        //如果参数值在columnList则说明需要,放到map里,为了防止参数位置错乱
        val valueList: MutableList<Any> = mutableListOf()

        for (field in declaredFields) {
            run {
                val mapping = field.getAnnotation(Mapping::class.java)
                val fieldName = when {
                    mapping != null -> mapping.name.joinToString { it + "" }
                    else -> WordUtil.underscoreName(field.name)
                }
                //通过反射取到的值,但是不一定是新增的时候需要的值
                if (columnList.stream().anyMatch { e -> e == fieldName }) {
                    val declaredField = paramValue.javaClass.getDeclaredField(field.name)
                    declaredField.isAccessible = true

                    when (val value = declaredField.get(paramValue)) {
                        StringUtils.isEmpty(value) -> valueList.add("''")
                        is String -> valueList.add("'$value'")
                        is Date -> valueList.add("'" + SimpleDateFormat(configuration.dataFormat).format(Date(value.toString())) + "'")
                        else -> valueList.add("$value")
                    }
                }
            }
        }

        return "(${valueList.joinToString(",")})"
    }


    //获取新增的时候需要新增的行
    private fun insertColumn(sourceCommand: String): MutableList<String> {
        val matcher = Pattern.compile("\\(.*?\\)").matcher(sourceCommand)
        while (matcher.find()) {
            val group = matcher.group().replace("(", "").replace(")", "").replace("`", "").replace(" ", "")
            return group.split(',') as MutableList<String>
        }
        throw WeekendException("无法解析的sql:$sourceCommand")
    }

    override fun syntaxCheck(command: String) {
        try {
            CCJSqlParserManager().parse(StringReader(command.trim()))
        } catch (e: Exception) {
            throw WeekendException("Bad Syntax By >> $command    >>  ${e.message}")
        }
    }

    private fun processConditionTemplate(command: String, paramMap: MutableMap<String, Any?>): String {
        val whenCommand = "$command "

        val sb = StringBuffer()
        //获得when
        val whenRegex: Matcher = Pattern.compile("when\\([a-zA-Z0-9]+\\)\\{*.[\\s\\S]+?\\}\\s+(?!.?')").matcher(whenCommand)
        while (whenRegex.find()) {
            //获得参数名
            var conditionName = ""
            val conditionRegex = Pattern.compile("when\\(*.*\\)").matcher(whenRegex.group())
            while (conditionRegex.find()) {
                conditionName = conditionRegex.group().replace("when(", "").replace(")", "")
            }

            val any = paramMap[conditionName]
            if (any == null) {
                PrintImpl().warning("没有传入参数:$conditionName 取消when条件执行")
                whenRegex.appendReplacement(sb, "")
                break
            }
            val conditionValue: String = any.toString()
            //获取执行条件
            val condition = Pattern.compile("(else|is)\\s+\\S*\\s*->\\s+\\S+\\s+\\S+\\s+'*.+?'*?;").matcher(whenRegex.group())
            while (condition.find()) {
                val judgeRegex = Pattern.compile("(else|is)\\s+\\S*\\s*->").matcher(condition.group())
                while (judgeRegex.find()) {
                    val judgeValue = judgeRegex.group().replace("is ", "").replace(" ->", "")
                    var realCondition = condition.group().replace(judgeRegex.group(), "").replace(";", "")

                    //[空格]and name = 'xx'
                    //to
                    //and name = 'xx'
                    if (realCondition.startsWith(" ")) realCondition = realCondition.substring(1, realCondition.length)

                    //如果满足is条件
                    if (conditionValue == judgeValue) {
                        whenRegex.appendReplacement(sb, realCondition)
                        break
                        //如果条件都不满足
                    } else if (judgeValue == "else") {
                        try {
                            whenRegex.appendReplacement(sb, realCondition)
                        } catch (ignore: IndexOutOfBoundsException) {
                            //匹配else的时候发生异常,正常情况
                        }
                        break
                    }
                }

            }
        }
        whenRegex.appendTail(sb)
        return sb.toString()
    }

    private fun processSimpleTemplate(template: String, paramMap: MutableMap<String, Any?>, regx: String): String {
        val m: Matcher = Pattern.compile(regx).matcher(template)
        val sb = StringBuffer()
        while (m.find()) {
            val param: String = m.group()
            when (val value = paramMap[param.substring(2, param.length - 1)]) {
                is String -> {
                    if (regx.contains("#")) m.appendReplacement(sb, "'$value'")
                    else m.appendReplacement(sb, value.toString())
                }
                is List<*> -> {
                    if (value.count() == 0) {
                        throw WeekendException("查询参数为空,暂时无法处理")
                    }
                    when (value[0]) {
                        is String -> {
                            val strList: MutableList<String> = mutableListOf();
                            value.forEach { e ->
                                run {
                                    strList.add("'" + (e as String) + "'")
                                }
                            }
                            m.appendReplacement(sb, "(" + strList.joinToString(",") + ")")
                        }
                        else -> m.appendReplacement(sb, "(" + value.toList().joinToString(",") + ")")
                    }
                }
                else -> {
                    if (value == null) {
                        m.appendReplacement(sb, "##NULL_PARAM##")
                    } else
                        m.appendReplacement(sb, value.toString())
                }
            }
        }
        m.appendTail(sb)
        return sb.toString()
    }

}