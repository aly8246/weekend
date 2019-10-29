package com.github.aly8246.core.query;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
public class AndBuilder implements CriteriaBuilder {
private Query query;

public AndBuilder(Query query) {
	this.query = query;
}

@Override
public Query buildQuery(String key, String value, CriteriaEnum criteriaEnum) {
	switch (criteriaEnum) {
		case GT://  >
			return query.addCriteria(Criteria.where(key).gt(value));
		case LT://  <
			return query.addCriteria(Criteria.where(key).lt(value));
		case EQ://  =
			return query.addCriteria(Criteria.where(key).is(value));
		case GE://  >=
			return query.addCriteria(Criteria.where(key).gte(value));
		case LE://  <=
			return query.addCriteria(Criteria.where(key).lte(value));
		case NQ:// !=
			return query.addCriteria(Criteria.where(key).ne(value));
		case IN:// in...
			return query.addCriteria(Criteria.where(key).in(Arrays.asList(value.split(","))));
	}
	return query;
}
}
