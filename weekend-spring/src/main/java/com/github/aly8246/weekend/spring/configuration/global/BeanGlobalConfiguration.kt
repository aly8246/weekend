package com.github.aly8246.weekend.spring.configuration.global

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.annotation.Resource

/**
 * bean注入的配置文件
 */
@Configuration
@ConditionalOnBean(name = ["weekendGlobalProperties"])
open class BeanGlobalConfiguration {
    @Resource
    var weekendGlobalProperties: WeekendGlobalProperties? = null

    @PostConstruct
    fun init() {
        configuration.nonFieldRemind = weekendGlobalProperties?.nonFieldRemind
        configuration.showParam = weekendGlobalProperties?.showParam
        configuration.showCommand = weekendGlobalProperties?.showCommand
        configuration.showCondition = weekendGlobalProperties?.showCondition
        configuration.showResult = weekendGlobalProperties?.showResult
        configuration.dataFormat = weekendGlobalProperties?.dataFormat

        if (configuration.nonFieldRemind == null) {
            configuration.nonFieldRemind = false
        }
        if (configuration.showParam == null) {
            configuration.showParam = false
        }
        if (configuration.showCommand == null) {
            configuration.showCommand = false
        }
        if (configuration.showCondition == null) {
            configuration.showCondition = false
        }
        if (configuration.showResult == null) {
            configuration.showResult = false
        }
        if (configuration.dataFormat == null) {
            configuration.dataFormat = "yyy-MM-dd HH:mm:ss"
        }

    }
}