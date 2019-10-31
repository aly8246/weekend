package com.github.aly8246.core.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/22 下午 02:26
 * @description：
 * @version: ：V
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation {
public String baseCommand;
public OperationEnum operation;
public String field;
public String tableName;

public List<Conditions> conditionsList = new LinkedList<>();
}
