package com.other.test.boot;

import com.github.aly8246.auto.configuration.WeekendAutoConfiguration;
import com.github.aly8246.core.annotation.WeekendDaoScan;
import com.other.test.boot.testdao.TestDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@WeekendDaoScan("com.other.test.boot.testdao")
public class BootStarterTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootStarterTestApplication.class, args);
    }

    @Resource
    TestDao testDao;


    @Bean
    @ConditionalOnBean(WeekendAutoConfiguration.class)
    void test() {
        List<UserInfo> userInfos = testDao.selectAll();
        userInfos.forEach(System.err::println);
    }

}
