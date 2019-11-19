package com.github.aly8246.core.page

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_EMPTY)
class PageResult<T> {
    var total: Int? = null
    var totalPage: Int? = null
    var page: Int? = null
    var pageSize: Int? = null
    var data: List<T>? = null
}