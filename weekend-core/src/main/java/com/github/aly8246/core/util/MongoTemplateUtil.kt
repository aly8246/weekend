package com.github.aly8246.core.util

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component


@Component
class MongoTemplateUtil {
    private var mongoTemplate2: MongoTemplate? = null

    @javax.annotation.Resource
    fun setMongoTemplate2(mongoTemplate: MongoTemplate) {
        this.mongoTemplate2 = mongoTemplate
        MongoTemplateUtil.mongoTemplate = mongoTemplate
    }

    companion object {
        lateinit var mongoTemplate: MongoTemplate
    }
}
