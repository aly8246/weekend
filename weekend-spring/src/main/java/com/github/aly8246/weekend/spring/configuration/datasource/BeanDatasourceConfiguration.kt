package com.github.aly8246.weekend.spring.configuration.datasource

import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.driver.MongoAddress
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.annotation.Resource

/**
 * bean注入的配置文件
 */
@Configuration
@ConditionalOnBean(name = ["weekendMongodbInfo"])
open class BeanDatasourceConfiguration {
    @Resource
    var weekendMongodbInfo: WeekendMongodbInfo? = null

    @PostConstruct
    fun init() {
        configuration.driverName = weekendMongodbInfo?.driverName
        configuration.datasourceUrl = weekendMongodbInfo?.datasourceUrl
        configuration.username = weekendMongodbInfo?.username
        configuration.password = weekendMongodbInfo?.password

        if (configuration.driverName == null) {
            configuration.driverName = "com.github.aly8246.core.driver.MongoDriver"
        }
        val mongoAddress = configuration.datasourceUrl?.let { MongoAddress(it) }
                ?: throw RuntimeException("The URI must like >> jdbc:mongodb://localhost:27017/weekend")

        configuration.mongoAddress = mongoAddress
    }
}