package com.github.aly8246.kotlin.mdo

import com.github.aly8246.core.annotation.Command
import com.github.aly8246.kotlin.pojo.UserInfo


interface UserDao {
    //@Command("select * from user_info where userMoney in #{userMoney} and age = #{userAge}")
//    @Command("select id,name from user_info  " +
//            "where age = #{userAge} " +
//            "and userMoney in #{userMoney} " +
//            "when(nameType){ " +
//            "    is 1 -> and name = '增删查';" +
//            "    is 2 -> and name = '超级管理员';" +
//            "    else -> and name = '其他洗脚员工';" +
//            "}")
    // @Command("select * from user_info limit 1,3")
    @Command("select * from user_info")
    fun exec(userAge: Int?, nameType: Int?, userMoney: MutableList<Int>?): List<UserInfo>

    @Command("INSERT INTO user_info(`id`, `name`, `userMoney`, `age`) VALUES #{u}")
    fun insert(u: MutableList<UserInfo>): Int

    @Command("INSERT INTO user_info" +
            "(`id`, `name`, `userMoney`, `age`) VALUES #{userList}")
    fun insertList(userList: MutableList<UserInfo>?): Int

    //from
    //select * from user_info where name = #{name}
    //name =小明
    //to
    //select * from user_info where name = '小明'
    @Command("select * from user_info where name = #{name}")
    fun template(name: String): List<UserInfo>

    // where @{userMoney} == 1 ? userMoney = 500 : userMoney = 700
    //from

    //select * from user_info
    // where
    // #when(userMoney) {
    //      is 1 -> userMoney = 500
    //      is 2 -> userMoney = 700
    // }

    //if userMoney = 1
    //select * from user_info where userMoney=500
    //if userMoney = 2
    //select * from user_info where userMoney=700


}