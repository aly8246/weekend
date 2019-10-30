package com.github.aly8246.core.query.queryBuilder.basic;

import com.github.aly8246.core.query.enmu.QueryEnum;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/30 下午 05:52
 * @description：
 * @version: ：V
 */
public class OperationSignCriteriaBuilder implements CriteriaBuilder {
@Override
public Criteria build(String fieldName, Object value, QueryEnum sign) {
	Criteria criteria = null;
	switch (sign) {
		case GT://  >
			criteria = Criteria.where(fieldName).gt(value);
			break;
		case LT://  <
			criteria = Criteria.where(fieldName).lt(value);
			break;
		case EQ://  =
			criteria = Criteria.where(fieldName).is(value);
			break;
		case GE://  >=
			criteria = Criteria.where(fieldName).gte(value);
			break;
		case LE://  <=
			criteria = Criteria.where(fieldName).lte(value);
			break;
		case NQ:// !=
			criteria = Criteria.where(fieldName).ne(value);
			break;
		case IN:// in...
			criteria = Criteria.where(fieldName).in(Arrays.asList(value.toString().split(",")));
			break;
	}
	return criteria;
}
}
