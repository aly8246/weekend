package com.github.aly8246.weekend.spring.configuration.datasource

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoAddress
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
@EnableConfigurationProperties(DatasourceProperties::class)
@ConditionalOnMissingBean(name = ["weekendMongodbInfo"])
open class PropertyDatasourceConfiguration {
    @Resource
    lateinit var datasourceProperties: DatasourceProperties

    @PostConstruct
    open fun init() {
        configuration.driverName = datasourceProperties.driverName
        configuration.datasourceUrl = datasourceProperties.uri
        configuration.username = datasourceProperties.username
        configuration.password = datasourceProperties.password

        if (datasourceProperties.uri == null) throw RuntimeException("The URI must like >> jdbc:mongodb://localhost:27017/weekend")
        val mongoAddress = configuration.datasourceUrl?.let { MongoAddress(it) }
        configuration.mongoAddress = mongoAddress
    }


}