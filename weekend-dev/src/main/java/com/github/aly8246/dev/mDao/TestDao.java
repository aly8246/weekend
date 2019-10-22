package com.github.aly8246.dev.mdao;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.core.handler.JsCommandHandler;
import com.github.aly8246.dev.pojo.UserInfo;

public interface TestDao {
// "<if test='maxAge != null'>" +
//                    "and age <= #{maxAge}"+
//                "</if>"+

@Exec(value = "select * from user_info")
UserInfo exec(String name, String name2);

@Exec("select")
String exe2();
}
