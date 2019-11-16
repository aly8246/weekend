package com.github.aly8246.kotlin

import com.github.aly8246.kotlin.mdo.UserDao
import com.github.aly8246.kotlin.mdo.UserDao2
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
        var moneyList: MutableList<Int> = mutableListOf(500, 700)
        // userDao.exec(22, null, moneyList).listIterator().forEach { e -> println(e) }
        // println(userDao.exec(22, 1, moneyList))
        userDao.exec(null, null, null).forEach { e -> println(e) }
        //userDao2.exec().listIterator().forEach { e -> println(e) }
        val insert = userDao.insert()
        println("新增的行数:$insert")
    }
}