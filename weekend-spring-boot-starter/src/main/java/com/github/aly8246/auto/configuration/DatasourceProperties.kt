package com.github.aly8246.auto.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConfigurationProperties(prefix = "weekend.mongodb")
class DatasourceProperties {
    private val properties = Properties()
    open fun getProperties(): Properties {
        return this.properties
    }

    var uri: String?
        get() {
            return this.properties.getProperty("uri")
        }
        set(value) {
            this.properties.setProperty("uri", value)
        }
    var driverName: String
        get() {
            return try {
                this.properties.getProperty("driver-name")
            } catch (e: IllegalStateException) {
                "com.github.aly8246.core.driver.MongoDriver"
            }
        }
        set(value) {
            this.properties.setProperty("driver-name", value)
        }
}