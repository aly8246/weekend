package com.github.aly8246.core.handler;

import com.github.aly8246.core.query.CriteriaEnum;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 12:54
 * @description：
 * @version: ：V
 */
public abstract class AbstractCondition implements Condition {

public Operation run(String baseCommand) {
	this.validCommand(baseCommand);
	
	//TODO 是否有连表条件
	Operation operation = new Operation();
	operation.setBaseCommand(baseCommand);
	operation.setOperation(OperationEnum.SELECT);
	operation.setField("*");
	operation.setTableName(Arrays.stream(baseCommand.split(" ")).collect(Collectors.toList()).get(3));
	
	//TODO 获得主表子表和查询条件等等操作
	Conditions conditions1 = new Conditions();
	conditions1.setType(QueryEnum.WHERE);
	conditions1.setFieldName("userMoney");
	conditions1.setSign(CriteriaEnum.GT);
	conditions1.setValue("100");
	
	Conditions conditions2 = new Conditions();
	conditions2.setType(QueryEnum.AND);
	conditions2.setFieldName("id");
	conditions2.setSign(CriteriaEnum.GT);
	conditions2.setValue("200000");
	
	Conditions conditions3 = new Conditions();
	conditions3.setType(QueryEnum.OR);
	conditions3.setFieldName("age");
	conditions3.setSign(CriteriaEnum.LE);
	conditions3.setValue("18");
	conditions3.setGroup("1");
	
	Conditions conditions4 = new Conditions();
	conditions4.setType(QueryEnum.OR);
	conditions4.setFieldName("age");
	conditions4.setSign(CriteriaEnum.LE);
	conditions4.setValue("22");
	conditions4.setGroup("1");
	
	operation.getConditionsList().add(conditions1);
	operation.getConditionsList().add(conditions2);
	
	return operation;
}


public void validCommand(String baseCommand) {
	System.out.println("1-AbstractCommand running");
}

}
