package com.github.aly8246.core.template

import com.github.aly8246.core.annotation.Command

interface Template {
    fun completeCommand(command: Command): String
}
