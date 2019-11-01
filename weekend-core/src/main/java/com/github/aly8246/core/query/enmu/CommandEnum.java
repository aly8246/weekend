package com.github.aly8246.core.query.enmu;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:45
 * @description：
 * @version: ：V
 */
@Getter
@AllArgsConstructor
public enum CommandEnum {
	WHERE("WHERE", "where"),
	CriteriaBo("NE", "!=");
private String name;
private String value;
}
