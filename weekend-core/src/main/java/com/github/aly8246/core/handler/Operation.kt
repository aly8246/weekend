package com.github.aly8246.core.handler

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

import java.util.LinkedList

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 02:26
 * @description：
 * @version: ：V
 */
class Operation {
    lateinit var baseCommand: String
    lateinit var operation: OperationEnum
    lateinit var field: String
    lateinit var tableName: String
    var conditionsList: List<Conditions> = LinkedList()
}
