package com.github.aly8246.dev.mdao;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.dev.pojo.UserInfo;

import java.util.List;

public interface TestDao {
// "<if test='maxAge != null'>" +
//                    "and age <= #{maxAge}"+
//                "</if>"+

@Exec(value = "select * from user_info")
List<UserInfo> exec(String name, String name2);

@Exec(value = "select * from user_info")
UserInfo exec2(String name, String name2);


@Exec("select")
String exe2();
}
