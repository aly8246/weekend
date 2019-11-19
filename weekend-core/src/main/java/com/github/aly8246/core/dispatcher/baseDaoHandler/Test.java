package com.github.aly8246.core.dispatcher.baseDaoHandler;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/19 下午 05:38
 * @description：
 * @version: ：V
 */
public class Test {
public static String camelToUnderline(String str) {
	if (str == null || str.trim().isEmpty()) {
		return "";
	}
	int len = str.length();
	StringBuilder sb = new StringBuilder(len);
	sb.append(str.substring(0, 1).toLowerCase());
	for (int i = 1; i < len; i++) {
		char c = str.charAt(i);
		if (Character.isUpperCase(c)) {
			sb.append("_");
			sb.append(Character.toLowerCase(c));
		} else {
			sb.append(c);
		}
	}
	return sb.toString();
}

public static void main(String[] args) {
	String aa = "UserInfo";
	System.out.println(camelToUnderline(aa));
}
}
