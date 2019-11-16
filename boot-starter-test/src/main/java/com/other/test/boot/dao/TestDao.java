package com.other.test.boot.dao;

import com.github.aly8246.core.annotation.Command;
import com.other.test.boot.enitiy.User;

import java.util.List;

public interface TestDao {
    @Command("INSERT INTO user" +
            "(`id`, `name`, `nick_name`, `address`, `age`, `password`, `safe_key`,`state`,`creator`,`create_time`) " +
            "VALUES #{user}")
    int insertOne(User user);

    @Command("INSERT INTO user" +
            "(`id`, `name`, `nick_name`, `address`, `age`, `password`, `safe_key`,`state`,`creator`,`create_time`) " +
            "VALUES #{userList}")
    int insertList(List<User> userList);

    @Command("select * from user")
    List<User> selectAll();

    @Command("select * from user where id = #{id}")
    User selectById(String id);

    @Command("select * from user where name in #{nameList}")
    List<User> selectByNameIn(List<String> nameList);

    @Command("select * from user" +
            " when(type){ " +
            "     is 1 -> order by age;" +
            "     is 2 -> order by age desc;" +
            "     else -> order by age;" +
            "}"
    )
    List<User> selectAllOrderByAge(Integer type);

    @Command("select * from user limit #{start},#{end}")
    List<User> selectByPage(Integer start, Integer end);
}

