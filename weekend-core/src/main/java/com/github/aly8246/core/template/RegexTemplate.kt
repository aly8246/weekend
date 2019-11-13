package com.github.aly8246.core.template

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
        PrintImpl().debug(" $sourceCommand")
        //替换普通参数
        var command = this.processSimpleTemplate(sourceCommand, param, "\\$\\{\\w+}")
        command = this.processSimpleTemplate(command, param, "\\#\\{\\w+}")

        //执行when条件判断
        command = processConditionTemplate(command, param)

        PrintImpl().debug(" $command")
        return command
    }

    override fun syntaxCheck(command: String) {
        try {
            CCJSqlParserManager().parse(StringReader(command.trim()))
        } catch (e: Exception) {
            throw WeekendException("Bad Syntax By:$command")
        }
    }

    private fun processConditionTemplate(command: String, params: MutableMap<Parameter, Any?>): String {
        val whenCommand = "$command "



        println(whenCommand)

        return whenCommand
    }

    private var str: String = "select * from user_info  " +
            "where age = #{userAge} " +
            "and userMoney in #{userMoney} " +
            "when(nameType){ " +
            "    is 1 -> and name = '小黑';" +
            "    is 2 -> and name = '超级管理员';" +
            "    else -> and name = '其他洗脚员工';" +
            "}" + " " +
            "when(ageType){ " +
            "    is 1 -> and age = 18\n" +
            "    is 2 -> and age = 22\n" +
            "    else -> and age = 30\n" +
            "}  "

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val regexTemplate = RegexTemplate()
            val sb = StringBuffer()
            //匹配when >>> when\([a-zA-Z0-9]+\)\{*.[\s\S]+?\}\s+(?!.?')
            //获取参数 >>> when\(*.*\)
            //取得行   >>> (else|is)\s+\S*\s*->\s+\S+\s+\S+\s+'*.+?'*?;
            //取得正文 >>> is\s+.\s*->
            val m: Matcher = Pattern.compile("when\\([a-zA-Z0-9]+\\)\\{*.[\\s\\S]+?\\}\\s+(?!.?')").matcher(regexTemplate.str)
            while (m.find()) {
                println(m.group())

                var conditionName: String = ""
                val c = Pattern.compile("when\\(*.*\\)").matcher(m.group())
                while (c.find()) {
                    conditionName = c.group().replace("when(", "").replace(")", "")
                }
                var nameType: Int = 2
                val condition = Pattern.compile("(else|is)\\s+\\S*\\s*->\\s+\\S+\\s+\\S+\\s+'*.+?'*?;").matcher(m.group())
                while (condition.find()) {
                    val matcher = Pattern.compile("is\\s+.\\s*->").matcher(condition.group())
                    while (matcher.find()) {
                        val conditionValue = matcher.group().replace("is ", "").replace(" ->", "")
                        if (nameType.toString() == conditionValue) {
                            // println("查询到条件:" + condition.group())
                            println("得到字段:" + condition.group().replace(matcher.group(), "").replace(";", ""))
                        }
                    }
                }
            }
            m.appendTail(sb)

            //  println(sb.toString())
        }
    }

    private fun acquisitionCondition(): MutableList<String> {
        var list: MutableList<String> = mutableListOf()



        return list
    }

    //发生回溯


    private fun processSimpleTemplate(template: String, params: MutableMap<Parameter, Any?>, regx: String): String {
        val paramMap: MutableMap<String, Any?> = mutableMapOf()
        params.forEach { e ->
            paramMap[e.key.name] = e.value
        }
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