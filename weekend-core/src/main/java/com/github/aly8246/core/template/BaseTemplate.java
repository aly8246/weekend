package com.github.aly8246.core.template;

import com.github.aly8246.core.annotation.Command;
import org.springframework.util.StringUtils;

public class BaseTemplate implements Template {

@Override
public String completeCommand(Command command) {
	String baseCommand = String.join("", command.value());
	if (StringUtils.isEmpty(baseCommand)) throw new RuntimeException("BaseCommand不能为空");
	return baseCommand;
}
//TODO 先将模板替换成具体参数值
// args command
// 完整命令

}
