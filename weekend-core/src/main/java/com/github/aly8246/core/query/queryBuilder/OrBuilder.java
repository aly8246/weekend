package com.github.aly8246.core.query.queryBuilder;

import com.github.aly8246.core.handler.Conditions;
import com.github.aly8246.core.query.queryBuilder.basic.CriteriaBuilder;
import com.github.aly8246.core.query.queryBuilder.basic.OperationSignCriteriaBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
public class OrBuilder implements QueryBuilder {
private Query query;

public OrBuilder(Query query) {
	this.query = query;
}

@Override
public Query buildQuery(List<Conditions> conditionsList) {
	
	//条件构建器，如果是where或者and则一起,如果是排序则是其他构建器
	Criteria[] criteriaArray = new Criteria[conditionsList.size()];
	for (int i = 0; i < conditionsList.size(); i++) {
		Conditions conditions = conditionsList.get(i);
		
		String fieldName = conditions.getFieldName();
		Object value = conditions.getValue();
		CriteriaBuilder criteriaBuilder = new OperationSignCriteriaBuilder();
		Criteria criteria = criteriaBuilder.build(fieldName, value, conditions.getSign());
		criteriaArray[i] = criteria;
	}
	
	query.addCriteria(new Criteria().orOperator(criteriaArray));
	return query;
}
}
