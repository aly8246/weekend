package com.github.aly8246.core.query.queryBuilder.basic;

import com.github.aly8246.core.query.enmu.QueryEnum;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/30 下午 05:51
 * @description：
 * @version: ：V
 */
public interface CriteriaBuilder {
public Criteria build(String fieldName, Object value, QueryEnum sign);
}
