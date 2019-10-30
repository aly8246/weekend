package com.github.aly8246.core.handler;

import com.github.aly8246.core.query.enmu.QueryEnum;

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
	
	//where userMoney > 100
//	Conditions conditions1 = new Conditions();
//	conditions1.setType(com.github.aly8246.core.handler.QueryEnum.WHERE);
//	conditions1.setFieldName("userMoney");
//	conditions1.setSign(QueryEnum.GT);
//	conditions1.setValue(100);
	
	//年龄等于18或者 年龄等于22但是金钱小于400
	//and (age = 18 or age = 22 and userMoney < 400)
	Conditions conditions3 = new Conditions();
	conditions3.setType(com.github.aly8246.core.handler.QueryEnum.OR);
	conditions3.setFieldName("age");
	conditions3.setSign(QueryEnum.EQ);
	conditions3.setValue(18);
	conditions3.setGroup("1");
	Conditions conditions4 = new Conditions();
	conditions4.setType(com.github.aly8246.core.handler.QueryEnum.OR);
	conditions4.setFieldName("age");
	conditions4.setSign(QueryEnum.EQ);
	conditions4.setValue(22);
	conditions4.setGroup("1");
	
	Conditions conditions5 = new Conditions();
	conditions5.setType(com.github.aly8246.core.handler.QueryEnum.AND);
	conditions5.setFieldName("userMoney");
	conditions5.setSign(QueryEnum.LT);
	conditions5.setValue(400);
	//conditions5.setGroup("1");
	
	//年龄是18或者22
	//AND ( a.login_name LIKE '%${keyword}%' OR a.real_name LIKE '%${keyword}%')
	//and (age like 18 or age like 22)
	//一个联合or需要解析成or查询组,但是query里需要
	/**
	 if (pageReqParams.getStartTime() != null && pageReqParams.getEndTime() != null) {
	 ----Criteria criteria = Criteria.where("_id").ne(0L);
	 ----query.addCriteria(criteria.andOperator(
	 ------------Criteria.where("collectionTime").gte(pageReqParams.getStartTime()),
	 ------------Criteria.where("collectionTime").lte(pageReqParams.getEndTime()))
	 ----);
	 }
	 */
	//operation.getConditionsList().add(conditions1);
	operation.getConditionsList().add(conditions3);
	operation.getConditionsList().add(conditions4);
	operation.getConditionsList().add(conditions5);
	
	return operation;
}


public void validCommand(String baseCommand) {
	System.out.println("1-AbstractCommand running");
}

}
