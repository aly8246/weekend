package com.github.aly8246.core.driver

class MongoMapping {
    lateinit var codeFieldName: String
    lateinit var dbFieldName: String

    lateinit var codeFieldType: Class<*>
    lateinit var dbFieldType: Class<*>

    var value: Any? = null
}