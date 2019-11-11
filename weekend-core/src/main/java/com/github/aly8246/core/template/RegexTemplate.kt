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
        var command = this.processTemplate(sourceCommand, param, "\\$\\{\\w+}")
        command = this.processTemplate(command, param, "\\#\\{\\w+}")

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

    private fun processTemplate(template: String?, params: MutableMap<Parameter, Any?>, regx: String): String {
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
                is String -> {
                    if (regx.contains("#")) m.appendReplacement(sb, "'$value'") else m.appendReplacement(sb, value.toString())
                }
                is List<*> -> {
                    m.appendReplacement(sb, "(" + value.toList().joinToString(",") + ")")
                }
                else -> m.appendReplacement(sb, value?.toString() ?: "")
            }
        }
        m.appendTail(sb)
        return sb.toString()
    }

}