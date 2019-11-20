package com.github.aly8246.core.page

import io.swagger.annotations.ApiModelProperty


class Page {
    @ApiModelProperty(value = "第几页[默认1]", example = "1", required = false)
    var page: Int = 1

    @ApiModelProperty(value = "每页大小[默认10]", example = "10", required = false)
    var pageSize: Int = 10
}
