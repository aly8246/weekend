package com.other.test.boot.service.impl;

import com.github.aly8246.core.page.Page;
import com.github.aly8246.core.page.PageResult;
import com.other.test.boot.dao.TestDao;
import com.other.test.boot.enitiy.User;
import com.other.test.boot.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestDao testDao;

    @Override
    public int insertOne(User user) {
        return testDao.insertOne(user);
    }

    @Override
    public int insertList(List<User> userList) {
        return testDao.insertList(userList);
    }

    @Override
    public List<User> selectAll() {
        return testDao.selectAll();
    }

    @Override
    public User selectById(String id) {
        return testDao.selectById(id);
    }

    @Override
    public User selectByUser(User user) {
        return testDao.selectByUser(user);
    }

    @Override
    public List<User> selectByNameIn(List<String> nameList) {
        return testDao.selectByNameIn(nameList);
    }

    @Override
    public List<User> selectAllOrderByAge(Integer type) {
        return testDao.selectAllOrderByAge(type);
    }

    @Override
    public List<User> selectByPage(Integer start, Integer end) {
        return testDao.selectByPage(start, end);
    }

    @Override
    public int deleteById(String id) {
        return testDao.deleteById(id);
    }

    @Override
    public int deleteByIdIn(List<String> idList) {
        return testDao.deleteByIdIn(idList);
    }

    @Override
    public int updateNameByIdIs(String id, String name) {
        return testDao.updateNameByIdIs(id, name);
    }

    @Override
    public int updateByUser(User user) {
        return testDao.updateByUser(user);
    }

    @Override
    public PageResult<User> selectPage(Page page, String name) {
        User user = new User();
        user.setName(name);
        return testDao.selectPage(page, "where name = #{user.name}", user);
    }

    @Override
    public PageResult<User> selectPageByParam(Page page, String name) {
        return testDao.selectPage(page, "where name = #{param1}", name);
    }

    @Override
    public PageResult<User> customPage(Page page) {
        return testDao.customPage(page);
    }

}
