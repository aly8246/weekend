package com.github.aly8246.core.query;

import com.github.aly8246.core.handler.Conditions;
import com.github.aly8246.core.handler.QueryEnum;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:37
 * @description：
 * @version: ：V
 */
public class QueryRunner extends AbstractWeekendQuery {

@Override
public Query run(List<Conditions> conditionsList) {
	return super.run(conditionsList);
}

@Override
protected Query query(List<Conditions> conditionsList) {
	System.err.println(conditionsList);
	Query query = new Query();
	
	List<Conditions> queryList = conditionsList
			                             .stream()
			                             .filter(e -> e.getType().equals(QueryEnum.WHERE) || e.getType().equals(QueryEnum.AND))
			                             .collect(Collectors.toList());
	for (Conditions conditions : queryList) {//处理where和and
		CriteriaBuilder criteriaBuilder = new AndBuilder(query);
		query = criteriaBuilder.buildQuery(conditions.getFieldName(), conditions.getValue(), conditions.getSign());
	}
	
	
	//处理or
	//TODO 生成数据
	List<Conditions> orList = conditionsList.stream().filter(e -> e.getType().equals(QueryEnum.OR)).collect(Collectors.toList());
	
	//条件构建器，如果是where或者and则一起,如果是排序则是其他构建器
	
	return query;
}
}
