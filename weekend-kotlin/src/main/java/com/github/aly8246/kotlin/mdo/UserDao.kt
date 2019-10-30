package com.github.aly8246.kotlin.mdo

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.kotlin.pojo.UserInfo

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/30 下午 03:12
 * @description：
 * @version:   ：V
 */
interface UserDao {
    @Command("select * from user_info")
    fun exec(): List<UserInfo>
}