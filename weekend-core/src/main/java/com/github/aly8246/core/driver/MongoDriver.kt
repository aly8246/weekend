package com.github.aly8246.core.driver

import com.github.aly8246.august.coretest.driver.MongoAddress
import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager
import java.sql.DriverPropertyInfo
import java.util.*
import java.util.logging.Logger

class MongoDriver : Driver {
    override fun connect(url: String, info: Properties?): Connection {
        val mongoAddress = MongoAddress(url)
        return MongoConnection(mongoAddress)
    }

    companion object {
        init {
            DriverManager.registerDriver(MongoDriver())
        }
    }

    override fun getPropertyInfo(url: String, info: Properties?): Array<DriverPropertyInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun acceptsURL(url: String): Boolean = url.startsWith("jdbc:mongodb:")

    override fun getMajorVersion(): Int = 4
    override fun getMinorVersion(): Int = 0


    override fun getParentLogger(): Logger? = null

    override fun jdbcCompliant(): Boolean = false

}