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
        conditions1.setType("where");
        conditions1.setFieldName("userMoney");
        conditions1.setCon(">100");
        conditions1.setValue("0");

        Conditions conditions2 = new Conditions();
        conditions2.setType("and");
        conditions2.setFieldName("id");
        conditions2.setCon(">");
        conditions2.setValue("200000");

        operation.getConditionsList().add(conditions1);
        operation.getConditionsList().add(conditions2);

        return operation;
    }


    public void validCommand(String baseCommand) {
        System.out.println("1-AbstractCommand running");
    }

}
