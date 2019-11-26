//package com.other.test.boot.config;
//
//import com.github.aly8246.core.configuration.WeekendMongodbInfo;
//import com.github.aly8246.core.configuration.WeekendGlobalProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class WeekendConfig {
//
//    @Bean
//    public WeekendMongodbInfo weekendMongodbInfo() {
//        WeekendMongodbInfo weekendMongodbInfo = new WeekendMongodbInfo();
//        weekendMongodbInfo.setDatasourceUrl("jdbc:mongodb://148.70.16.82:27017/weekend-dev");
//        return weekendMongodbInfo;
//    }
//
//
//    @Bean
//    public WeekendGlobalProperties weekendGlobalProperties() {
//        WeekendGlobalProperties weekendGlobalProperties = new WeekendGlobalProperties();
//        weekendGlobalProperties.setShowResult(true);
//        return weekendGlobalProperties;
//    }
//}
