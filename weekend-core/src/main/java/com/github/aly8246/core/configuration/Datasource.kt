package com.github.aly8246.core.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service

@Service
@ConfigurationProperties(prefix = "weekend.mongodb")
class Datasource {
    var uri: String? = null
    var driverName: String? = null
}