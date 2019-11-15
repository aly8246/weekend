package com.github.aly8246.kotlin.mdo;

import com.github.aly8246.core.annotation.Command;
import com.github.aly8246.kotlin.pojo.UserInfo;

import java.util.List;


public interface UserDao2 {
@Command("select * from user_info")
List<UserInfo> exec();

@Command("select * from user_info")
UserInfo exec2();


}
