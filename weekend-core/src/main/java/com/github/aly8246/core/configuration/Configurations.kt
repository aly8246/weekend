package com.github.aly8246.core.configuration

import com.github.aly8246.core.driver.MongoAddress

class Configurations {
    lateinit var driverName: String
    lateinit var datasourceUrl: String
    var mongoAddress: MongoAddress? = null


}