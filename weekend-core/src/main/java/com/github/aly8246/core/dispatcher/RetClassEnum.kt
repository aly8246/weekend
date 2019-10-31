package com.github.aly8246.core.dispatcher

import lombok.AllArgsConstructor
import lombok.Getter

@Getter
@AllArgsConstructor
enum class RetClassEnum {
    COLLECTION, OBJECT, PAGE, NULL
}
