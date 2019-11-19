package com.other.test.boot.enitiy;

import com.github.aly8246.core.annotation.WeekendCollection;
import com.github.aly8246.core.annotation.Mapping;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

//@WeekendCollection("user")
public class User {
@ApiModelProperty(value = "id", example = "aaa1111aaa")
private String id;

@ApiModelProperty(value = "名称", example = "张三")
private String name;

@ApiModelProperty(value = "昵称", example = "张三王八")
@Mapping(name = "nick_name")
private String nickName;

@ApiModelProperty(value = "地址", example = "重庆市渝北区大竹林街道xxx")
private String address;

@ApiModelProperty(value = "年龄", example = "18")
private Integer age;

@ApiModelProperty(value = "密码", example = "123456")
private String password;

@ApiModelProperty(value = "安全码", example = "1111")
@Mapping(name = "safe_key")
private String safeKey;

@ApiModelProperty(value = "状态: 0正常 1禁用", example = "0")
private Integer state;

@ApiModelProperty(value = "创建者", example = "system")
private String creator;

@ApiModelProperty(value = "创建时间", example = "2019-11-17 01:05:01")
@Mapping(name = "create_time")
private Date createTime;
}
