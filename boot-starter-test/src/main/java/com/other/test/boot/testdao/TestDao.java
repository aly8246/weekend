package com.other.test.boot.testdao;

import com.github.aly8246.core.annotation.Command;
import com.other.test.boot.UserInfo;

import java.util.List;

public interface TestDao {
    @Command("select * from user_info where age = 18")
    List<UserInfo> selectAll();
}
