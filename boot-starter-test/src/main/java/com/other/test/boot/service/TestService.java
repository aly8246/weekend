package com.other.test.boot.service;

import com.github.aly8246.core.page.Page;
import com.github.aly8246.core.page.PageResult;
import com.other.test.boot.enitiy.User;

import java.util.List;

public interface TestService {
int insertOne(User user);

int insertList(List<User> userList);

List<User> selectAll();

User selectById(String id);

User selectByUser(User user);

List<User> selectByNameIn(List<String> nameList);

List<User> selectAllOrderByAge(Integer type);

List<User> selectByPage(Integer start, Integer end);

int deleteById(String id);

int deleteByIdIn(List<String> idList);

int updateNameByIdIs(String id, String name);

int updateByUser(User user);

PageResult<User> selectPage(Page page);

}
