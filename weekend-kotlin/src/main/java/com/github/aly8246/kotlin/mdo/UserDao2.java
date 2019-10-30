package com.github.aly8246.kotlin.mdo;

import com.github.aly8246.core.annotation.Command;
import com.github.aly8246.kotlin.pojo.UserInfo;

import java.util.List;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/30 下午 06:04
 * @description：
 * @version: ：V
 */
public interface UserDao2 {
@Command("select * from user_info")
List<UserInfo> exec();
}
