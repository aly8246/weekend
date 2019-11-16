package com.github.aly8246.auto.configuration

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
@EnableConfigurationProperties(ConfigurationProperties::class)
@AutoConfigureAfter(WeekendAutoConfiguration::class)
open class WeekendGlobalConfiguration {
    @Resource
    lateinit var configurationProperties: ConfigurationProperties

    @PostConstruct
    open fun init() {
        configuration.nonFieldRemind = configurationProperties.nonFieldRemind

        configuration.showParam = configurationProperties.showParam
        configuration.showCommand = configurationProperties.showCommand
        configuration.showCondition = configurationProperties.showCondition
        configuration.showResult = configurationProperties.showResult
        configuration.dataFormat = configurationProperties.dataFormat
    }


}