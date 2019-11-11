package com.github.aly8246.core.template;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/11 下午 02:25
 * @description：
 * @version: ：V
 */
public class TestTemplate {
public static void main(String[] args) {
	String sql = "select * from user_info where name = #{userName} age='#{userName}'";
	String name = "小明";
	String valueName = "userName";

	//如果要的是对象的成员呢
	
	String[] s = sql.split(" ");
	StringBuilder sb = new StringBuilder();
	for (String value : s) {
		if (value.contains(valueName) && !value.contains("'")) {
			sb.append(value.contains("#") ? "'" + name + "'" : name);
		} else sb.append(value);
		sb.append(" ");
	}
	System.out.println(sb.toString());
}
}
