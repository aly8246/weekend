package com.github.aly8246.core.configuration

import com.github.aly8246.core.driver.MongoAddress
import com.github.aly8246.core.driver.MongoConnection
import java.sql.Connection
import java.sql.Statement

class Configurations {
    lateinit var driverName: String
    lateinit var datasourceUrl: String
    lateinit var connection: Connection
    var mongoAddress: MongoAddress? = null
    lateinit var mongoConnection: MongoConnection
    lateinit var statement: Statement


}