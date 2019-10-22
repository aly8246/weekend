package com.github.aly8246.dev;

import com.github.aly8246.core.annotation.WeekendDaoScan;
import com.github.aly8246.dev.mdao.TestDao;
import com.github.aly8246.dev.pojo.UserInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootApplication(scanBasePackages = "com.github.aly8246.dev")
@ComponentScan({"com.github.aly8246.core"})

@WeekendDaoScan({
		"com.github.aly8246.dev.mDao",
		"com.github.aly8246.dev.dd"
})
public class WeekendDevApplication {

public static void main(String[] args) {
	SpringApplication.run(WeekendDevApplication.class, args);
}

@Resource
TestDao testDao;
@Resource
MongoTemplate mongoTemplate;

@Bean
public void test() {
	UserInfo exec = testDao.exec("小黄", "小黑");
//	UserInfo u = new UserInfo(UUID.randomUUID().toString(), "小黑", "400");
//	mongoTemplate.insert(u, "user_info2");
//	UserInfo user_info = mongoTemplate.findOne(new Query(), UserInfo.class, "user_info2");
//	System.out.println(user_info);
	//System.out.println(testDao.exe2());
	System.out.println(exec);
}
}
