package com.github.aly8246.core.template

import com.github.aly8246.core.exception.WeekendException
import net.sf.jsqlparser.parser.CCJSqlParserManager
import java.io.StringReader

class RegexTemplate : BaseTemplate() {
    override fun replaceParam(sourceCommand: String): String {
        println("准备模板替换")
        return sourceCommand
    }

    override fun syntaxCheck(command: String) {
        try {
            CCJSqlParserManager().parse(StringReader(command.trim()))
        } catch (e: Exception) {
            throw WeekendException("Bad Syntax By:$command")
        }
    }
}