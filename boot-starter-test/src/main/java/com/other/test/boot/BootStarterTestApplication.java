package com.other.test.boot;

import com.github.aly8246.weekend.spring.register.WeekendDaoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@WeekendDaoScan("com.other.test.boot.dao")
public class BootStarterTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootStarterTestApplication.class, args);
    }

}
