package com.other.test.boot.service.impl;

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
}
