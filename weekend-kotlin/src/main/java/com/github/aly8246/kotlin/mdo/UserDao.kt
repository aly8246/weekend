package com.github.aly8246.kotlin.mdo

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.kotlin.pojo.UserInfo


interface UserDao {
    //@Command("select * from user_info where userMoney in #{userMoney} and age = #{userAge}")
    @Command("select * from user_info where  age = #{userAge}")
    fun exec(userAge: Int, name: String?, userMoney: MutableList<Int>?): UserInfo

    @Command("select * from user_info")
    fun exec2(): List<UserInfo>

    //from
    //select * from user_info where name = #{name}
    //name =小明
    //to
    //select * from user_info where name = '小明'
    @Command("select * from user_info where name = #{name}")
    fun template(name: String): List<UserInfo>


    //from
    //select * from user_info
    // where #{userMoney} == 1 ? userMoney=500 : userMoney=700

    //if userMoney = 1
    //select * from user_info where userMoney=500
    //if userMoney = 2
    //select * from user_info where userMoney=700


}