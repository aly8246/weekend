package com.github.aly8246.core.template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/12 下午 05:37
 * @description：
 * @version: ：V
 */
public class TestCondition {
private final static String aa = "select * from user_info  " +
		                                 "where age = #{userAge} " +
		                                 "and userMoney in #{userMoney} " +
		                                 " and " +
		                                 "when(nameType){ " +
		                                 "    is 1 -> name='小黑'" +
		                                 "    is 2 -> name='超级管理员'" +
		                                 "    else -> name='其他洗脚员工'" +
		                                 "}" + " and " +
		                                 "when(ageType){ " +
		                                 "    is 1 -> age = 18" +
		                                 "    is 2 -> age = 22" +
		                                 "    else -> age = 30" +
		                                 "}";

public static void main(String[] args) {
	Matcher matcher = Pattern.compile("when.*\\{([\\s\\S].*){4}[\\s\\S].*\\}").matcher(aa);
	while (matcher.find()) {
		System.out.println(matcher.group());
	}
}
}
