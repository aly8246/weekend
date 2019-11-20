package com.github.aly8246.core.page

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import io.swagger.annotations.ApiModelProperty
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_EMPTY)
class PageResult<T> {
    @ApiModelProperty(value = "总记录数", example = "963")
    var total: Int? = null

    @ApiModelProperty(value = "总页数", example = "97")
    var totalPage: Int? = null

    @ApiModelProperty(value = "位于第几页", example = "18")
    var page: Int? = null

    @ApiModelProperty(value = "每页的大小", example = "10")
    var pageSize: Int? = null

    @ApiModelProperty(value = "分页数据", example = "10")
    var data: List<T>? = null
}