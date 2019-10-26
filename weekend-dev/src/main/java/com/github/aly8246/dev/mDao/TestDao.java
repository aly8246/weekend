package com.github.aly8246.dev.mDao;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.dev.pojo.UserInfo;
import com.github.aly8246.dev.pojo.UserInfo2;

import java.util.List;

public interface TestDao {
// "<if test='maxAge != null'>" +
//                    "and age <= #{maxAge}"+
//                "</if>"+

    @Exec(value = "select * from user_info")
    List<UserInfo> exec(String name, String name2);

    @Exec(value = "select * from user_info")
    UserInfo exec2(String name, String name2);

    @Exec(value = "select * from user_info")
    UserInfo2 exec3(String name, String name2);

    @Exec(value = "select * from user_info")
    List<UserInfo2> exec4(String name, String name2);

    @Exec(value = "select * from user_info", returnType = UserInfo2.class)
    List exec5(String name, String name2);

    @Exec("select")
    String exe2();

    @Exec(value = "select * from user_info")
    void insert();
}
