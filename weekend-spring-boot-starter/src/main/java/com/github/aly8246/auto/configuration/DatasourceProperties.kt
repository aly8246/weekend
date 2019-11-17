package com.github.aly8246.auto.configuration

import com.github.aly8246.core.exception.WeekendException
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConfigurationProperties(prefix = "weekend.mongodb")
open class DatasourceProperties {
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
    var username: String
        get() {
            return try {
                this.properties.getProperty("username")
            } catch (e: IllegalStateException) {
                ""
            }
        }
        set(value) {
            this.properties.setProperty("username", value)
        }
    var password: String
        get() {
            return try {
                this.properties.getProperty("password")
            } catch (e: IllegalStateException) {
                ""
            }
        }
        set(value) {
            this.properties.setProperty("password", value)
        }
}