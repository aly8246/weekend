package com.github.aly8246.dev.mDao;

import com.github.aly8246.core.annotation.Exec;

public interface TestDao {

    @Exec(value = "select * from user where user.id = '${name}'")
    String exec(String name);
}
