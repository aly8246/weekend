package com.github.aly8246.core.template

import com.github.aly8246.core.annotation.Command
import org.springframework.util.StringUtils

abstract class BaseTemplate : Template {

    override fun completeCommand(command: Command): String {
        val baseCommand = command.value.joinToString("")
        when {
            StringUtils.isEmpty(baseCommand) -> throw RuntimeException("BaseCommand不能为空")
        }
        val replaceParam = replaceParam(baseCommand)
        syntaxCheck(replaceParam)
        return replaceParam
    }

    abstract fun replaceParam(sourceCommand: String): String
    abstract fun syntaxCheck(command: String)

    //TODO 先将模板替换成具体参数值
    // args command
    // 完整命令

    //select * from user_info where name = #{name}
    //name =小明
    //to
    //select * from user_info where name = '小明'

    //select * from user_info where name = #{name}?
    //name = null
    //to
    //select * from user_info

    //select * from user_info where id in @{idList}
    //idList=[1,2,3,4,5]
    //to
    //select * from user_info where id in (1,2,3,4,5)
}
