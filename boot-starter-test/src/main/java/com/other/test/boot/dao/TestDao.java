package com.other.test.boot.dao;

import com.github.aly8246.core.annotation.Command;
import com.github.aly8246.core.annotation.PageMethod;
import com.github.aly8246.core.page.Page;
import com.github.aly8246.core.page.PageResult;
import com.other.test.boot.enitiy.User;

import java.util.List;

public interface TestDao extends WeekendDao<User> {
@Command("INSERT INTO user" +
		         "(`id`, `name`, `nickName`, `address`, `age`, `password`, `safe_key`,`state`,`creator`,`create_time`) " +
		         "VALUES #{user}")
int insertOne(User user);

@Command("INSERT INTO user" +
		         "(`id`, `name`, `nickName`, `address`, `age`, `password`, `safe_key`,`state`,`creator`,`create_time`) " +
		         "VALUES #{userList}")
int insertList(List<User> userList);

@Command("select * from user where id = #{user.id}")
User selectByUser(User user);

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

@Command("delete from user where id = #{id}")
int deleteById(String id);

@Command("delete from user where id in #{idList}")
int deleteByIdIn(List<String> idList);

@Command("update user set name = #{name} where id = #{id}")
int updateNameByIdIs(String id, String name);

@Command("update user set name = #{user.name} where id = #{user.id}")
int updateByUser(User user);

@Command("select * from user")
@PageMethod
PageResult<User> customPage(Page page);
}

