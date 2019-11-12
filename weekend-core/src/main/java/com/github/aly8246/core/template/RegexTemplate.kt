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

        println(command)
        println(params)
        "        when(nameType){ " +
                "    is 1 -> name='小黑'" +
                "    is 2 -> name='超级管理员'" +
                "}"

        return command
    }

    private var str: String = "select * from user_info  " +
            "where age = #{userAge} " +
            "and userMoney in #{userMoney} " +
            " and " +
            "when(nameType){ " +
            "    is 1 -> name='小黑'" +
            "    is 2 -> name='超级管理员'" +
            "    else -> name='其他洗脚员工'" +
            "}" + " and " +
            "when(ageType){ " +
            "    is 1 -> age = 18" +
            "    is 2 -> age = 22" +
            "    else -> age = 30" +
            "}"

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val regexTemplate = RegexTemplate()
            //(when)(.*){([\s\S].*.[\s\S].*.[\s\S].*.*)
            //(when)(.*)\{([\s\S].*){4}\}
            val sb = StringBuffer()
            val m: Matcher = Pattern.compile("when.*\\{([\\s\\S].*){4}[\\s\\S].*\\}").matcher(regexTemplate.str)
            while (m.find()) {
                println(m.group())
                println("")
                m.appendReplacement(sb, "我被替换了！")
//                for (index in 0..m.groupCount()) {
//                    println(m.group(index))
//                    println("=========")
//                }
            }
            m.appendTail(sb)

            println(sb.toString())
        }
    }

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