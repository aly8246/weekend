package com.github.aly8246.core.configuration

import com.github.aly8246.core.driver.MongoAddress

class Configurations {
    var driverName: String? = null
    var datasourceUrl: String? = null
    var mongoAddress: MongoAddress? = null
    var nonFieldRemind: Boolean? = null

    var showParam: Boolean? = null
    var showCommand: Boolean? = null
    var showCondition: Boolean? = null
    var showResult: Boolean? = null

    companion object {
        var configuration = Configurations()
    }
}