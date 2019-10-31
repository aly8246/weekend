package com.github.aly8246.core.template

import com.github.aly8246.core.annotation.Command
import org.springframework.util.StringUtils

class BaseTemplate : Template {

    override fun completeCommand(command: Command): String {
        val baseCommand = command.value.joinToString("")
        if (StringUtils.isEmpty(baseCommand)) throw RuntimeException("BaseCommand不能为空")
        return baseCommand
    }
    //TODO 先将模板替换成具体参数值
    // args command
    // 完整命令

}
