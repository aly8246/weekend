package com.github.aly8246.weekend.spring.configuration.datasource


open class WeekendMongodbInfo {
    /**
     * 驱动名称 全限定路径
     * 一般是com.github.aly8246.core.driver.MongoDriver
     * @see com.github.aly8246.core.driver.MongoDriver
     */
    var driverName: String? = null

    /**
     * 数据库连接信息
     * 类似jdbc:mongodb://localhost:27017/weekend-dev
     */
    var datasourceUrl: String? = null

    /**
     * 数据库用户名
     */
    var username: String? = null


    /**
     * 数据库密码
     */
    var password: String? = null
}