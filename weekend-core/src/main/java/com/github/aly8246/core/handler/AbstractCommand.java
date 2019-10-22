package com.github.aly8246.core.handler;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 12:54
 * @description：
 * @version: ：V
 */
public abstract class AbstractCommand implements Command {

public Operation run(String baseCommand) {
	this.validCommand(baseCommand);
	
	//TODO 是否有连表条件
	Operation operation = new Operation();
	operation.setBaseCommand(baseCommand);
	operation.setOperation("select");
	operation.setField("*");
	operation.setTableName(Arrays.stream(baseCommand.split(" ")).collect(Collectors.toList()).get(3));
	
	//TODO 获得主表子表和查询条件等等操作
	Condition condition1 = new Condition();
	condition1.setType("where");
	condition1.setFieldName("userMoney");
	condition1.setCon(">100");
	condition1.setValue("0");
	
	Condition condition2 = new Condition();
	condition2.setType("and");
	condition2.setFieldName("id");
	condition2.setCon(">");
	condition2.setValue("200000");
	
	operation.getConditionList().add(condition1);
	operation.getConditionList().add(condition2);
	
	return operation;
}

public void validCommand(String baseCommand) {
	System.out.println("1-AbstractCommand running");
}

}
