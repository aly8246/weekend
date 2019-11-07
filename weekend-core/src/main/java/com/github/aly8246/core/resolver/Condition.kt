package com.github.aly8246.core.resolver

import com.github.aly8246.core.query.enmu.OperationSignEnum

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 02:31
 * @description：
 * @version: ：V
 */

class Condition {
    //执行的类型
    lateinit var type: ConditionEnum

    //执行的字段名
    var fieldName: String = ""

    //执行的条件
    lateinit var sign: OperationSignEnum

    //执行的值
    lateinit var value: Any

    lateinit var classType: Class<*>

    //其他
    var other: String = ""

    // 如果是or还需要条件分组
    var group: String = ""

}
