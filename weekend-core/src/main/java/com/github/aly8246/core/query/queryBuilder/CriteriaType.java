package com.github.aly8246.core.criteria;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:45
 * @description：
 * @version: ：V
 */
@Getter
public enum CriteriaType {
	WHERE("WHERE", "where"),
	CriteriaBo("NE", "!=");
private String name;
private String value;

CriteriaType(String name, String value) {
	this.name = name;
	this.value = value;
}

}
