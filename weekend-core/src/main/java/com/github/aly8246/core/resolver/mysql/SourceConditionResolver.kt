package com.github.aly8246.core.resolver.mysql;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/07 下午 05:19
 * @description：
 * @version: ：V
 */
public class SourceConditionResolver {
public void sourceBuildCondition
		(PlainSelect plainSelect) {
	BinaryExpression rootExpression = (BinaryExpression) plainSelect.getWhere();
	
	if (rootExpression == null) return;
	
	System.out.println(rootExpression);
	
	for (; true; ) {
		Expression rightExpression = rootExpression.getRightExpression();
		Expression leftExpression = rootExpression.getLeftExpression();
		if (rightExpression instanceof EqualsTo) {
			System.out.println("右边是EqualsTo");
		}
		if (leftExpression instanceof EqualsTo) {
			System.out.println("左边是EqualsTo");
		}
		
		
		//如果左边节点的left节点是LongValue或者StringValue
		//则证明左边节点为结束节点，递归右边节点
		
		//如果左边节点和右边节点都为空，则完成节点递归
		if (rightExpression instanceof Parenthesis) {
			System.out.println("右边是组合参数 先递归");
		} else if (!nodeEnd((BinaryExpression) leftExpression)) rootExpression = (BinaryExpression) leftExpression;
		
		//如果是Parenthesis参数则证明是组合参数，否则继续递归
		if (rightExpression instanceof Parenthesis) {
			System.out.println("左边是组合参数 先递归");
		} else if (!nodeEnd((BinaryExpression) rightExpression)) rootExpression = (BinaryExpression) rightExpression;
		if (rightExpression instanceof Parenthesis) {
			System.out.println("最终存在组合参数，停止递归");
			break;
		} else if (nodeEnd((BinaryExpression) leftExpression) && nodeEnd((BinaryExpression) rightExpression)) break;
	}
	
}

/**
 * 如果节点不是MinorThan这种查询节点
 * 如果节点的左子节点和右子节点都不是查询节点
 */
private boolean nodeEnd(BinaryExpression rootExpression) {
	BinaryExpression expression;
	if (rootExpression instanceof MinorThan
			    || rootExpression instanceof MinorThanEquals
			    || rootExpression instanceof OrExpression
			    || rootExpression instanceof AndExpression) {
		try {
			if (rootExpression.getLeftExpression() instanceof Parenthesis) return false;
			expression = (BinaryExpression) rootExpression.getLeftExpression();
		} catch (ClassCastException e) {
			System.out.println("rootExpression是结束节点:" + rootExpression.getLeftExpression());
			return true;
		}
		try {
			if (rootExpression.getRightExpression() instanceof Parenthesis) return false;
			expression = (BinaryExpression) rootExpression.getRightExpression();
		} catch (ClassCastException e) {
			System.out.println(e);
			System.out.println("rootExpression是结束节点:" + rootExpression.getRightExpression());
			return true;
		}
	}
	System.out.println("rootExpression还未结束");
	return true;
}
}
