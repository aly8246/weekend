package com.other.test.boot.service;


import com.other.test.boot.enitiy.User;

import java.util.List;

public interface BaseMethodService {
    Integer insertOne(User user);

    Integer insertAll(List<User> userList);
}
