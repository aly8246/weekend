package com.github.aly8246.core.criteria;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:43
 * @description：
 * @version: ：V
 */
public class CriteriaUtil {
public static Criteria buildCriteria() {
	return Criteria.where("_id").ne(0L);
}

public static Criteria CriteriaWereNe(CriteriaType criteriaType) {
	Criteria criteria = new Criteria();
	
	return Criteria.where("").lt("");
}
}
