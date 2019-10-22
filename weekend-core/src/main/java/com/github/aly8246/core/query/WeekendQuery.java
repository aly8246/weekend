package com.github.aly8246.core.query;

import org.springframework.data.mongodb.core.query.Query;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:37
 * @description：
 * @version: ：V
 */
public interface WeekendQuery {

Query buildQuery();

Query where(Query query);

Query and(Query query);
}
