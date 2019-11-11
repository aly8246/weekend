package com.github.aly8246.auto.configuration

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoAddress
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
@EnableConfigurationProperties(DatasourceProperties::class)
open class WeekendAutoConfiguration {
    @Resource
    lateinit var datasourceProperties: DatasourceProperties

    @PostConstruct
    open fun init() {
        configuration.driverName = datasourceProperties.driverName
        configuration.datasourceUrl = datasourceProperties.uri

        if (datasourceProperties.uri == null) throw RuntimeException("The URI must like >> jdbc:mongodb://localhost:27017/weekend")
        val mongoAddress = configuration.datasourceUrl?.let { MongoAddress(it) }
        configuration.mongoAddress = mongoAddress
    }


}