package com.github.aly8246.core.configuration

import com.github.aly8246.core.driver.MongoAddress

class Configurations {
    lateinit var driverName: String
    var datasourceUrl: String? = null
    var mongoAddress: MongoAddress? = null

    companion object {
        var configuration = Configurations()
    }

}