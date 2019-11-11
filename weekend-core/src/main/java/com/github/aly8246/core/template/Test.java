package com.github.aly8246.core.template;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/11 下午 02:47
 * @description：
 * @version: ：V
 */
public class Test {

public static String processTemplate(String template, Map<String, Object> params, String regx) {
	Matcher m = Pattern.compile(regx).matcher(template);
	StringBuffer sb = new StringBuffer();
	while (m.find()) {
		String param = m.group();
		Object value = params.get(param.substring(2, param.length() - 1));
		if (value instanceof String) {
			if (regx.contains("#"))
				m.appendReplacement(sb, "'" + value.toString() + "'");
			else m.appendReplacement(sb, value.toString());
		} else m.appendReplacement(sb, value == null ? "" : value.toString());
	}
	m.appendTail(sb);
	return sb.toString();
}

public static void main(String[] args) {
	Map map = new HashMap();
	map.put("name", "张三");
	map.put("age", 18);
	String message = processTemplate("select * from user_info where name like 'name-${name}' and age = #{age}", map, "\\$\\{\\w+}");
	message = processTemplate(message, map, "\\#\\{\\w+}");
	System.out.println(message);
}
}
