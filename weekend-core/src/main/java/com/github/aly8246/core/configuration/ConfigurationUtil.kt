package com.github.aly8246.core.configuration

import com.github.aly8246.core.driver.MongoAddress
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Configuration
open class ConfigurationUtil {

    companion object {
        var configuration = Configurations()
    }

    open fun getConfigurations(): Configurations {
        return configuration
    }

    /**
     * Init datasource config
     * @see com.github.aly8246.core.configuration.Datasource
     */
    @Resource
    lateinit var datasource: Datasource

    @PostConstruct
    open fun init() {
        configuration.driverName = datasource.driverName!!
        configuration.datasourceUrl = datasource.uri!!
        val mongoAddress = MongoAddress(configuration.datasourceUrl)
        configuration.mongoAddress = mongoAddress
    }
}