package com.github.aly8246.core.query;

import com.github.aly8246.core.handler.Condition;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:37
 * @description：
 * @version: ：V
 */
public interface WeekendQuery {

Query run(List<Condition> conditionList);

}
