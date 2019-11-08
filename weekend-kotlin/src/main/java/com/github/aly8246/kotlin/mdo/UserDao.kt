package com.github.aly8246.kotlin.mdo

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.kotlin.pojo.UserInfo
import com.github.aly8246.kotlin.pojo.UserInfoMapping

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/30 下午 03:12
 * @description：
 * @version:   ：V
 */
interface UserDao {
    @Command("select * from user_info where userMoney <= 700 and (age=22 or age =18) ")
    fun exec(): List<UserInfo>

    @Command("select * from user_info")
    fun exec2(): UserInfoMapping

    //select * from user_info where name = #{name}
    //name =小明
    //to
    //select * from user_info where name = '小明'
    @Command("select * from user_info where name = #{name}")
    fun template(name: String): List<UserInfo>
}