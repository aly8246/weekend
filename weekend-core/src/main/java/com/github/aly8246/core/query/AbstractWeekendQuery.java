package com.github.aly8246.core.query;

import com.github.aly8246.core.handler.Conditions;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 01:37
 * @description：
 * @version: ：V
 */
public abstract class AbstractWeekendQuery implements WeekendQuery {

@Override
public Query run(List<Conditions> conditionsList) {
	return this.query(conditionsList);
}

protected abstract Query query(List<Conditions> conditionsList);
//TODO 得到一个Query对象，根据用户所输入的条件来继续组装query，然后将query交给execute

}
