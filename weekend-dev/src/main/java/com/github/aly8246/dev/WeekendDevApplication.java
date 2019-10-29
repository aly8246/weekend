package com.github.aly8246.dev;

import com.github.aly8246.core.annotation.WeekendDaoScan;
import com.github.aly8246.dev.mDao.TestDao;
import com.github.aly8246.dev.pojo.UserInfo;
import com.github.aly8246.dev.pojo.UserInfo2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

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

@Bean
public void test() {
	System.out.println("=================================");
	System.out.println("=================================");
	System.out.println("=================================");
	List list = testDao.exec5("小黄", "小黑");
//
//	List<UserInfo> exec = testDao.exec("小黄", "小黑");
//	UserInfo exec2 = testDao.exec2("小黄", "小黑");
//	UserInfo2 userInfo2 = testDao.exec3("小黄", "小黑");
//	List<UserInfo2> userInfo3 = testDao.exec4("小黄", "小黑");
//	testDao.insert();
//	System.out.println("带映射的list查询" + exec);
//	System.err.println("带映射的对象查询" + exec2);
//
//	System.out.println("默认映射的list查询" + userInfo3);
//	System.out.println("默认映射的对象查询" + userInfo2);
	System.out.println("通用List查询" + list);
}
}
