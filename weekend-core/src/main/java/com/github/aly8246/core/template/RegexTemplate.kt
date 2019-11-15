package com.github.aly8246.core.template

import com.github.aly8246.core.configuration.Configurations
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.util.PrintImpl
import net.sf.jsqlparser.parser.CCJSqlParserManager
import java.io.StringReader
import java.lang.reflect.Parameter
import java.util.regex.Matcher
import java.util.regex.Pattern

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RegexTemplate : BaseTemplate() {
    override fun replaceParam(sourceCommand: String, param: MutableMap<Parameter, Any?>): String {
        if (configuration.showCommand!!)
            PrintImpl().debug("originalCommand >>   $sourceCommand")
        //替换普通参数
        var command = this.processSimpleTemplate(sourceCommand, param, "\\$\\{\\w+}")
        command = this.processSimpleTemplate(command, param, "\\#\\{\\w+}")

        //执行when条件判断
        command = processConditionTemplate(command, param)

        if (configuration.showCommand!!)
            PrintImpl().debug("analyzedCommand >>   $command")
        return command
    }

    override fun syntaxCheck(command: String) {
        try {
            CCJSqlParserManager().parse(StringReader(command.trim()))
        } catch (e: Exception) {

            throw WeekendException("Bad Syntax By:$command    >>  ${e.message}")
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
            val paramMap: MutableMap<String, Any?> = this.convertParamMap(params)
            val any = paramMap[conditionName]

            if (any == null) {
                PrintImpl().debug("没有传入参数:$conditionName 取消when条件执行语法且不可逆")
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

    private fun convertParamMap(params: MutableMap<Parameter, Any?>): MutableMap<String, Any?> {
        val paramMap: MutableMap<String, Any?> = mutableMapOf()
        params.forEach { e ->
            paramMap[e.key.name] = e.value
        }
        return paramMap
    }

    private fun processSimpleTemplate(template: String, params: MutableMap<Parameter, Any?>, regx: String): String {
        val paramMap: MutableMap<String, Any?> = this.convertParamMap(params)
        //TODO 如果参数值为空，则去掉本段的参数替换
        val m: Matcher = Pattern.compile(regx).matcher(template)
        val sb = StringBuffer()
        while (m.find()) {
            val param: String = m.group()
            when (val value = paramMap[param.substring(2, param.length - 1)]) {
                is String -> if (regx.contains("#")) m.appendReplacement(sb, "'$value'") else m.appendReplacement(sb, value.toString())
                is List<*> -> m.appendReplacement(sb, "(" + value.toList().joinToString(",") + ")")
                else -> m.appendReplacement(sb, value?.toString() ?: "")
            }
        }
        m.appendTail(sb)
        return sb.toString()
    }

}