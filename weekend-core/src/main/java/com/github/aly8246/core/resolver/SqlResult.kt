package com.github.aly8246.core.resolver

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import org.bson.conversions.Bson
import org.bson.Document

class SqlResult {
    lateinit var collection: MongoCollection<Document>

    lateinit var selectItem: BasicDBObject
    lateinit var queryCondition: Bson
}