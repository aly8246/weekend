package com.other.test.boot.service.impl;

import com.other.test.boot.dao.BaseMethodDao;
import com.other.test.boot.enitiy.User;
import com.other.test.boot.service.BaseMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseMethodServiceImpl implements BaseMethodService {
    private final BaseMethodDao baseMethodDao;

    @Override
    public Integer insertOne(User user) {
        return baseMethodDao.insertOne(user);
    }

    @Override
    public Integer insertAll(List<User> userList) {
        return baseMethodDao.insertAll(userList);
    }
}
