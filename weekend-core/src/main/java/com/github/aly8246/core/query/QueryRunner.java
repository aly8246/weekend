package com.github.aly8246.core.query;

import com.github.aly8246.core.handler.Conditions;
import com.github.aly8246.core.handler.QueryEnum;
import com.github.aly8246.core.query.queryBuilder.AndBuilder;
import com.github.aly8246.core.query.queryBuilder.OrBuilder;
import com.github.aly8246.core.query.queryBuilder.QueryBuilder;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
			                             .filter(e -> StringUtils.isEmpty(e.getGroup()))
			                             .filter(e -> e.getType().equals(QueryEnum.WHERE) || e.getType().equals(QueryEnum.AND))
			                             .collect(Collectors.toList());
	
	//处理where和and
	QueryBuilder AndBuilder = new AndBuilder(query);
	query = AndBuilder.buildQuery(queryList);
	
	
	//处理or
	Map<String, List<Conditions>> orList = conditionsList
			                                       .stream()
			                                       .filter(e -> e.getType().equals(QueryEnum.OR) || !StringUtils.isEmpty(e.getGroup()))
			                                       .collect(Collectors.groupingBy(Conditions::getGroup));
	Iterator<Map.Entry<String, List<Conditions>>> orIterator =
			orList.entrySet().iterator();
	for (; orIterator.hasNext(); ) {
		Map.Entry<String, List<Conditions>> next = orIterator.next();
		List<Conditions> value = next.getValue();
		QueryBuilder OrBuilder = new OrBuilder(query);
		query = OrBuilder.buildQuery(value);
	}
	
	//处理sort
	
	//处理group
	
	return query;
}
}