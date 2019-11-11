package com.github.aly8246.core.template

import com.github.aly8246.core.annotation.Command
import java.lang.reflect.Parameter

interface Template {
    fun completeCommand(command: Command, param: MutableMap<Parameter, Any?>): String
}
