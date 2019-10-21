package com.github.aly8246.dev;

import com.github.aly8246.core.annotation.WeekendDaoScan;
import com.github.aly8246.dev.mDao.TestDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

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
        String x = testDao.exec("小黄");
        System.out.println(x);
    }
}
