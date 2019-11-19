package com.github.aly8246.core.template

import java.lang.reflect.Parameter

interface Template {
    fun completeCommand(baseCommand: String, param: MutableMap<Parameter, Any?>): String
}
