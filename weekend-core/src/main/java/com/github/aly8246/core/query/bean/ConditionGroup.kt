package com.github.aly8246.core.query.bean

import com.github.aly8246.core.handler.Conditions
import lombok.Data
import lombok.Setter
import org.springframework.data.mongodb.core.query.Query

/**
 * @Author     ：南有乔木
 * @Email      ：1558146696@qq.com
 * @date       ：Created in 2019/10/31 下午 06:34
 * @description：
 * @version:   ：V
 */
class ConditionGroup {
    lateinit var commonConditions: List<Conditions>
    lateinit var groupConditions: Map<String, List<Conditions>>
    lateinit var sortConditions: List<Conditions>
    lateinit var groupByConditions: List<Conditions>
}