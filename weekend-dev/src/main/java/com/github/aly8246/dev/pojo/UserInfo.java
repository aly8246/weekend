package com.github.aly8246.dev.pojo;

import com.github.aly8246.core.annotation.Mapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 上午 11:45
 * @description：
 * @version: ：V
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

@Mapping(name = "id", value = "id")
private String id;

@Mapping(name = "name", value = "name")
private String name;

@Mapping(name = "userMoney", value = "user_money")
private String userMoney;
}
