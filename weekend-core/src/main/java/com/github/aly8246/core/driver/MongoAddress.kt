package com.github.aly8246.core.driver

import com.github.aly8246.core.exception.WeekendException
import com.mongodb.ServerAddress

open class MongoAddress(host: String) : ServerAddress(resolverUrl(host)) {
    private var sourceUrl: String = host
    fun dbName(): String {
        var url: String = sourceUrl
        url = url.replace(URL_PREFIX, "")
        if (url.contains("/")) return url.split("/")[1]
        throw WeekendException("Bad config , It's missing databaseName!")
    }
}

// jdbc:mongodb://148.70.16.82/weekend-dev
// to
// 148.70.16.82
private const val URL_PREFIX: String = "jdbc:mongodb://"
private const val LOCAL_HOST: String = "localhost"
private fun resolverUrl(sourceUrl: String): String {
    if (!sourceUrl.startsWith(URL_PREFIX)) throw WeekendException("The URI must like >> jdbc:mongodb://localhost:27017/weekend")
    var url = sourceUrl
    if (url.contains(LOCAL_HOST)) return LOCAL_HOST
    url = url.replace(URL_PREFIX, "")
    if (url.contains("/")) return url.split("/")[0]
    return url
}
