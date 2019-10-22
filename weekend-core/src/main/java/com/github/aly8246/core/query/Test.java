package com.github.aly8246.core.query;

import com.github.aly8246.core.criteria.CriteriaUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:42
 * @description：
 * @version: ：V
 */
public class Test {
public static void main(String[] args) {
	WeekendQuery weekendQuery = new QueryRunner();
	//Query query = weekendQuery.buildQuery().addCriteria(CriteriaUtil.buildCriteria());
	
	//if command=
	//   select *  user表   where 用户名 = '111' and 密码='222'

//	for (int i = 0; i < 10; i++) {
//		query.addCriteria(Criteria.where("_id").ne(0L));
//	}
//
	System.out.println(new Query());
}
}
