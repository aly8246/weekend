package com.other.test.boot.service.impl;

import com.other.test.boot.dao.TestDao;
import com.other.test.boot.enitiy.User;
import com.other.test.boot.service.BaseMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseMethodServiceImpl implements BaseMethodService {
    private final TestDao testDao;

    @Override
    public Integer insertSelective(User user) {
        return testDao.insertSelective(user);
    }
}
