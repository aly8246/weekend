package com.github.aly8246.core.template

import org.springframework.util.StringUtils
import java.lang.reflect.Parameter

abstract class BaseTemplate : Template {

    override fun completeCommand(baseCommand: String, param: MutableMap<String, Any?>): String {
        when {
            StringUtils.isEmpty(baseCommand) -> throw RuntimeException("BaseCommand不能为空")
        }
        val replaceParam = replaceParam(baseCommand, param)
        syntaxCheck(replaceParam)
        return replaceParam
    }

    abstract fun replaceParam(sourceCommand: String, param: MutableMap<String, Any?>): String

    abstract fun syntaxCheck(command: String)

}
