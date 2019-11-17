package com.github.aly8246.core.template

import com.github.aly8246.core.annotation.Mapping
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.util.BasicDataTypeUtil
import com.github.aly8246.core.util.PrintImpl
import net.sf.jsqlparser.parser.CCJSqlParserManager
import java.io.StringReader
import java.lang.reflect.Parameter
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Collectors.toList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class RegexTemplate : BaseTemplate() {
    override fun replaceParam(sourceCommand: String, param: MutableMap<Parameter, Any?>): String {
        if (configuration.showCommand!!)
            PrintImpl().debug("originalCommand >>   $sourceCommand")
        var command = ""

        if (sourceCommand.startsWith("SELECT") || sourceCommand.startsWith("select") || sourceCommand.startsWith("Select")
                || sourceCommand.startsWith("DELETE") || sourceCommand.startsWith("delete") || sourceCommand.startsWith("Delete")
                || sourceCommand.startsWith("UPDATE") || sourceCommand.startsWith("update") || sourceCommand.startsWith("Update")
        ) {
            //替换普通参数
            command = this.processSimpleTemplate(sourceCommand, param, "\$\\{.*?\\}")
            command = this.processSimpleTemplate(command, param, "\\#\\{.*?\\}")
            //执行when条件判断
            command = processConditionTemplate(command, param)
        } else if (sourceCommand.startsWith("INSERT") || sourceCommand.startsWith("insert") || sourceCommand.startsWith("Insert")) {
            //为insert方法处理参数
            command = this.processInsertValue(sourceCommand, param)
        }

        command = this.processNullParam(command)
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

    private fun processInsertValue(sourceCommand: String, param: MutableMap<Parameter, Any?>): String {
        val convertParamMap = this.convertParamMap(param, false)
        if (convertParamMap.size > 1 || convertParamMap.isEmpty()) throw WeekendException("要新增数据的时候参数应当是一个类,用来承载参数")
        var paramName = ""
        var paramValue: Any? = null
        val insertColumn = this.insertColumn(sourceCommand)

        convertParamMap.entries.forEach { e ->
            run {
                paramName = e.key
                paramValue = e.value
            }
        }

        val sb = StringBuffer()
        val matcher = Pattern.compile("#\\{$paramName}").matcher(sourceCommand)
        while (matcher.find()) {
            //插入的values 无论是单个插入还是批量插入
            val insertValue = StringBuilder()
            when (paramValue) {
                is Collections, is Collection<*> -> {
                    val arrayList = paramValue as ArrayList<*>
                    arrayList.forEach { e ->
                        run {
                            val buildInsertValue = buildInsertValue(insertColumn, e)
                            insertValue.append(buildInsertValue).append(",")
                        }
                    }
                    insertValue.deleteCharAt(insertValue.length - 1)
                }
                else -> {
                    insertValue.append(buildInsertValue(insertColumn, paramValue))
                }
            }
            matcher.appendReplacement(sb, insertValue.toString())
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    //根据要新增的行.从参数里拿出数据
    private fun buildInsertValue(columnList: MutableList<String>, paramValue: Any?): String {
        val paramValueClass = paramValue!!::class.java
        val declaredFields = paramValueClass.declaredFields

        val stringBuilder = StringBuilder()
        stringBuilder.append(" (")

        //paramValue是传递的参数类,依次取出参数值
        //如果参数值在columnList则说明需要,放到map里,为了防止参数位置错乱
        val valueMap: MutableMap<String, String> = mutableMapOf()
        columnList.forEach { e ->
            run {
                valueMap.put(e, "")
            }
        }
        for (field in declaredFields) {
            run {
                val annotationList = field.annotations.toList()
                val mappingList = annotationList.stream().filter { it is Mapping }.collect(toList())
                val fieldName = when {
                    mappingList.size >= 1 -> {
                        val fieldMappingName = (mappingList[0] as Mapping).name.joinToString { it + "" }
                        fieldMappingName
                    }
                    else -> field.name
                }
                //通过反射取到的值,但是不一定是新增的时候需要的值
                if (columnList.stream().anyMatch { e -> e == fieldName }) {

                    val declaredField = paramValue.javaClass.getDeclaredField(field.name)
                    declaredField.isAccessible = true

                    when (val value = declaredField.get(paramValue)) {
                        is String -> valueMap[fieldName] = "'$value'"
                        is Date -> valueMap[fieldName] = "'" + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(value.toString())) + "'"
                        else -> valueMap[fieldName] = "$value"
                    }
                }
            }
        }
        stringBuilder.append(valueMap.values.stream().collect(Collectors.joining(",")))
        stringBuilder.append(")")
        return stringBuilder.toString()
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

    private fun processConditionTemplate(command: String, params: MutableMap<Parameter, Any?>): String {
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
            val paramMap: MutableMap<String, Any?> = this.convertParamMap(params, true)
            val any = paramMap[conditionName]

            if (any == null) {
                PrintImpl().warning("没有传入参数:$conditionName 取消when条件执行")
                whenRegex.appendReplacement(sb, "")
                break
            }
            var conditionValue: String = any.toString()
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

    // resolverClass 是否解析类里面的字段，新增的时候不需要解析
    private fun convertParamMap(params: MutableMap<Parameter, Any?>, resolverClass: Boolean): MutableMap<String, Any?> {
        val resultParamMap: MutableMap<String, Any?> = mutableMapOf()
        params.forEach { e ->
            //如果参数中包含一个类
            //    UserInfo userInfo
            //    #{userInfo.id}
            if (resolverClass)
                if (!BasicDataTypeUtil.isBasicDataType(e.key)) {
                    val className = toLowerCaseFirstOne(e.key.type.simpleName)

                    val valueInstance = e.value!!::class.java
                    val declaredFields = valueInstance.declaredFields

                    for (field in declaredFields) {
                        val declaredField = valueInstance.getDeclaredField(field.name)
                        declaredField.isAccessible = true
                        val entityValue = declaredField.get(e.value)
                        if (entityValue != null) {
                            resultParamMap["$className.${field.name}"] = entityValue
                        }
                    }
                }
            resultParamMap[e.key.name] = e.value
        }
        return resultParamMap
    }

    private fun toLowerCaseFirstOne(str: String): String {
        if (str[0].isLowerCase()) {
            return str
        }
        return java.lang.StringBuilder().append(str[0].toLowerCase()).append(str.substring((1))).toString()
    }

    private fun processSimpleTemplate(template: String, params: MutableMap<Parameter, Any?>, regx: String): String {
        val paramMap: MutableMap<String, Any?> = this.convertParamMap(params, true)
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