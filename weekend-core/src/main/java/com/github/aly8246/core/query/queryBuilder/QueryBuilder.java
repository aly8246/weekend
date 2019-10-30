package com.github.aly8246.core.query.queryBuilder;

import com.github.aly8246.core.handler.Conditions;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
public interface QueryBuilder {
Query buildQuery(List<Conditions> conditionsList);
}