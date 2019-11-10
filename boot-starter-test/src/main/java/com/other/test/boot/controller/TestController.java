package com.other.test.boot.controller;

import com.other.test.boot.UserInfo;
import com.other.test.boot.testdao.TestDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {
    @Resource
    TestDao testDao;

    @GetMapping
    public Object test() {
        List<UserInfo> userInfos = testDao.selectAll();
        userInfos.forEach(System.err::println);
        System.out.println(userInfos.get(0));
        return userInfos;
    }
}
