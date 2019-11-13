package com.github.aly8246.auto.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*

@Service
@ConfigurationProperties(prefix = "weekend.config")
open class ConfigurationProperties {
    private val properties = Properties()
    open fun getProperties(): Properties {
        return this.properties
    }

    var nonFieldRemind: Boolean
        get() {
            try {
                return this.properties.getProperty("non-field-remind")!!.toBoolean()
            } catch (e: KotlinNullPointerException) {
            }
            return false
        }
        set(value) {
            this.properties.setProperty("non-field-remind", value.toString())
        }

    var showParam: Boolean
        get() {
            try {
                return this.properties.getProperty("show-param")!!.toBoolean()
            } catch (e: KotlinNullPointerException) {
            }
            return false
        }
        set(value) {
            this.properties.setProperty("show-param", value.toString())
        }
    var showCommand: Boolean
        get() {
            try {
                return this.properties.getProperty("show-command")!!.toBoolean()
            } catch (e: KotlinNullPointerException) {
            }
            return false
        }
        set(value) {
            this.properties.setProperty("show-command", value.toString())
        }
    var showCondition: Boolean
        get() {
            try {
                return this.properties.getProperty("show-condition")!!.toBoolean()
            } catch (e: KotlinNullPointerException) {
            }
            return false
        }
        set(value) {
            this.properties.setProperty("show-condition", value.toString())
        }
    var showResult: Boolean
        get() {
            try {
                return this.properties.getProperty("show-result")!!.toBoolean()
            } catch (e: KotlinNullPointerException) {
            }
            return false
        }
        set(value) {
            this.properties.setProperty("show-result", value.toString())
        }

}