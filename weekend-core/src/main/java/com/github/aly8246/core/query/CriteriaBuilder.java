package com.github.aly8246.core.query;

import org.springframework.data.mongodb.core.query.Query;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/29 下午 05:40
 * @description：
 * @version: ：V
 */
public interface CriteriaBuilder {
Query buildQuery(String key, String value, CriteriaEnum criteriaEnum);
}
