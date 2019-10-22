package com.github.aly8246.dev;

import com.github.aly8246.core.annotation.WeekendDaoScan;
import com.github.aly8246.dev.mdao.TestDao;
import com.github.aly8246.dev.pojo.UserInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.List;

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
	List<UserInfo> exec = testDao.exec("小黄", "小黑");
	UserInfo exec2 = testDao.exec2("小黄", "小黑");
	System.out.println(exec);
	System.err.println(exec2);
}
}
