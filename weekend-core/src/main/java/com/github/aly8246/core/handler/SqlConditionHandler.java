package com.github.aly8246.core.handler;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 12:58
 * @description：
 * @version: ：V
 */
public class SqlConditionHandler extends AbstractSqlCondition {

@Override
public void validCommand(String baseCommand) {
	System.out.println("sql构建条件来验证命令");
}

@Override
public Operation operationDetails(String baseCommand) {
	return super.operationDetails(baseCommand);
}
}
