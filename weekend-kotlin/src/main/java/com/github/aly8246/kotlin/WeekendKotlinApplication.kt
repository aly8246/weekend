package com.github.aly8246.kotlin

import com.github.aly8246.core.annotation.WeekendDaoScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration

@SpringBootApplication(exclude = [MongoAutoConfiguration::class])
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
}