package com.github.aly8246.core.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 02:31
 * @description：
 * @version: ：V
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conditions {

private String type;
//执行的类型
private String fieldName;
//执行的字段名
private String con;
//执行的条件
private String value;
//执行的值

private String other;
//其他
}
