package com.github.aly8246.kotlin

import com.github.aly8246.core.annotation.WeekendDaoScan
import com.github.aly8246.kotlin.mdo.UserDao
import com.github.aly8246.kotlin.mdo.UserDao2
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import javax.annotation.Resource


@SpringBootApplication(scanBasePackages = ["com.github.aly8246.kotlin"])
@ComponentScan("com.github.aly8246.core")

@WeekendDaoScan(
        "com.github.aly8246.kotlin.mdo",
        "com.github.aly8246.kotlin.dd"
)
open class WeekendKotlinApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(WeekendKotlinApplication::class.java, *args)
        }
    }

    @Resource
    lateinit var userDao: UserDao
    @Resource
    lateinit var userDao2: UserDao2

    @Bean
    open fun test() {
        //  println(userDao2.exec2())
        // println(userDao.exec2())
        userDao.exec().listIterator().forEach { e -> println(e) }
        userDao2.exec().listIterator().forEach { e -> println(e) }
    }
}