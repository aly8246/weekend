package com.github.aly8246.weekend.spring.configuration.global

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.weekend.spring.configuration.datasource.PropertyDatasourceConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
@EnableConfigurationProperties(ConfigurationProperties::class)
@AutoConfigureAfter(PropertyDatasourceConfiguration::class)
@ConditionalOnMissingBean(name = ["weekendGlobalProperties"])
open class PropertyGlobalConfiguration {

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