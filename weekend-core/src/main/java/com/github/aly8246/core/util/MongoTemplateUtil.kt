package com.github.aly8246.core.util;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


@Component
public class MongoTemplateUtil {
private MongoTemplate mongoTemplate2;

@javax.annotation.Resource
public void setMongoTemplate2(MongoTemplate mongoTemplate) {
	this.mongoTemplate2 = mongoTemplate;
	MongoTemplateUtil.mongoTemplate = mongoTemplate;
}

public static MongoTemplate mongoTemplate;
}
