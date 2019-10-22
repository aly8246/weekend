package com.github.aly8246.core.handler;

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
	Operation operation = new Operation();
	operation.setBaseCommand(baseCommand);
	operation.setOperation("select");
	operation.setField("*");
	operation.setTableName("user_info2");
	
	//TODO 获得主表子表和查询条件等等操作
	Condition condition = new Condition();
	condition.setType("where");
	condition.setFieldName("id");
	condition.setCon("!=");
	condition.setValue("0");
	operation.getConditionList().add(condition);
	
	return operation;
}

public void validCommand(String baseCommand) {
	System.out.println("1-AbstractCommand running");
}

}
