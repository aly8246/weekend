package com.github.aly8246.dev.mdao;

import com.github.aly8246.core.annotation.Exec;
import com.github.aly8246.core.handler.JsCommandHandler;

public interface TestDao {
// "<if test='maxAge != null'>" +
//                    "and age <= #{maxAge}"+
//                "</if>"+

@Exec(value = "select * from user where 1=1 <if test ='user!=null'>and user.id = '${name}'</if>", handler = JsCommandHandler.class)
String exec(String name, String name2);

@Exec("select")
String exe2();
}
