package com.other.test.boot.service;

import com.other.test.boot.enitiy.User;

import java.util.List;

public interface TestService {
    int insertOne(User user);

    int insertList(List<User> userList);

    List<User> selectAll();

    User selectById(String id);

    List<User> selectByNameIn(List<String> nameList);

    List<User> selectAllOrderByAge(Integer type);

    List<User> selectByPage(Integer start, Integer end);
}