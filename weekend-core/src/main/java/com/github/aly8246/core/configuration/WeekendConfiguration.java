//package com.github.aly8246.core.configuration;
//
//import com.github.aly8246.core.factory.RegistryBeanByPackage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Configuration
//@EnableConfigurationProperties(value = WeekendProperties.class)
//public class WeekendConfiguration {
//    @Resource
//    private WeekendProperties weekendProperties;
//
//    public static List<String> basePackageList = new ArrayList<>();
//
//
//    @PostConstruct
//    public void ddd() {
//        List<String> packageList = new ArrayList<>();
//
//        //TODO 获取MqlProperties的路径，再根据扫描的路径来合并到一起
//        packageList.add("com.ddd.test");
//
//        packageList.add(weekendProperties.getBasePackage());
//        this.basePackageList.addAll(packageList);
//
//        //System.out.println(basePackageList);
//
//    }
//}
