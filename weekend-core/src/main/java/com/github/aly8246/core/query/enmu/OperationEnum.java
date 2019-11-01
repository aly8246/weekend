package com.github.aly8246.core.query.enmu;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:42
 * @description：
 * @version: ：V
 */
@Getter
@AllArgsConstructor
public enum OperationEnum {
	// 大于
	GT(">"),
	// 小于
	LT("<"),
	// 等于
	EQ("="),
	// 大于等于
	GE(">="),
	// 小于等于
	LE("<="),
	// 不等于
	NQ("!="),
	// 在...中
	IN("in");
// 以..开头
// 以..结尾
// 包含
private String sign;
}
