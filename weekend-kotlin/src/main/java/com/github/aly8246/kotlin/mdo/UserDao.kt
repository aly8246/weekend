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
    @Command("select * from user_info where userMoney in #{userMoney} and age = #{userAge}")
    fun exec(userAge: Int, name: String?, userMoney: MutableList<Int>): List<UserInfo>

    @Command("select * from user_info")
    fun exec2(): List<UserInfoMapping>

    //from
    //select * from user_info where name = #{name}
    //name =小明
    //to
    //select * from user_info where name = '小明'
    @Command("select * from user_info where name = #{name}")
    fun template(name: String): List<UserInfo>
}