package com.github.aly8246.core.query.queryBuilder;

import com.github.aly8246.core.handler.Conditions;
import com.github.aly8246.core.query.queryBuilder.basic.CriteriaBuilder;
import com.github.aly8246.core.query.queryBuilder.basic.OperationSignCriteriaBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;


/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
public class AndBuilder implements QueryBuilder {
private Query query;

public AndBuilder(Query query) {
	this.query = query;
}

@Override
public Query buildQuery(List<Conditions> conditionsList) {
	for (Conditions conditions : conditionsList) {
		String fieldName = conditions.getFieldName();
		Object value = conditions.getValue();
		CriteriaBuilder criteriaBuilder = new OperationSignCriteriaBuilder();
		
		Criteria criteria = criteriaBuilder.build(fieldName, value, conditions.getSign());
		query.addCriteria(criteria);
		
	}
	return query;
}
}
