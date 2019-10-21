package com.github.aly8246.dev.mDao;

import com.github.aly8246.core.annotation.Exec;

public interface TestDao {
    // "<if test='maxAge != null'>" +
//                    "and age <= #{maxAge}"+
//                "</if>"+
    @Exec("select * from user where 1=1 <if test ='user!=null'>and user.id = '${name}'</if>")
    String exec(String name);
}
