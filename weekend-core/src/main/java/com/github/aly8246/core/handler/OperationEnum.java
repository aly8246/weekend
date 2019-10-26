package com.github.aly8246.core.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationEnum {
    INSERT("INSERT"), DELETE("DELETE"), UPDATE("UPDATE"), SELECT("SELECT"), OTHER("OTHER");
    private String type;
}
