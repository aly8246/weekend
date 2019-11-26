//package com.github.aly8246.weekend.spring.configuration;
//
//import com.github.aly8246.weekend.spring.configuration.datasource.WeekendMongodbInfo;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.context.properties.NestedConfigurationProperty;
//import org.springframework.stereotype.Component;
//
//@Component
//@ConfigurationProperties(prefix = "weekend")
//@EnableConfigurationProperties(WeekendMongodbInfo.class)
//public class WeekendProperties {
//
//    @NestedConfigurationProperty
//    private WeekendMongodbInfo weekendMongodbInfo;
//
//    public WeekendMongodbInfo getWeekendMongodbInfo() {
//        return weekendMongodbInfo;
//    }
//    public void setWeekendMongodbInfo(WeekendMongodbInfo weekendMongodbInfo) {
//        this.weekendMongodbInfo = weekendMongodbInfo;
//    }
//}
