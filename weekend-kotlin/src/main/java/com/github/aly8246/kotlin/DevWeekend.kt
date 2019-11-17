package com.github.aly8246.kotlin

import com.github.aly8246.kotlin.mdo.UserDao
import com.github.aly8246.kotlin.mdo.UserDao2
import com.github.aly8246.kotlin.pojo.UserInfo
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.annotation.Resource

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/11/11 下午 04:58
 * @description：
 * @version:   ：V
 */
@Configuration
@AutoConfigureAfter(WeekendKotlinApplication::class)
open class DevWeekend {
    @Resource
    lateinit var userDao: UserDao
    @Resource
    lateinit var userDao2: UserDao2

    @Bean
    open fun test() {

    }
//
//    @Bean
//    open fun test() {
//        var moneyList: MutableList<Int> = mutableListOf(500, 700)
//        userDao.exec(22, 1, moneyList).listIterator().forEach { e -> println(e) }
//        //    println(userDao.exec(22, 1, moneyList))
//       // userDao.exec(22, 1, moneyList).forEach { e -> println(e) }
//       // userDao2.exec().listIterator().forEach { e -> println(e) }
//
//        val userInfo = UserInfo()
//        userInfo.userId = System.currentTimeMillis().toString()
//        userInfo.userName = "小高"
//        // userInfo.userMoney = 800.00
//        // userInfo.userAge = 22
//        userInfo.clazz = "aaaa"
//        userInfo.time = "bbbb"
//        val userInfo2 = UserInfo()
//        userInfo2.userId = System.currentTimeMillis().toString() + "Custom"
//        userInfo2.userName = "小五"
//        userInfo2.userMoney = 820.00
//        userInfo2.userAge = 11
//        userInfo2.clazz = "aaaa"
//        userInfo2.time = "bbbb"
//        var list: MutableList<UserInfo> = mutableListOf(userInfo, userInfo2)
//
//       // val insert = userDao.insert(list)
//      //  println("新增的行数:$insert")
//    }
}