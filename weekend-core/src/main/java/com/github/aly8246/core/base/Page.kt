package com.github.aly8246.core.base

import io.swagger.annotations.ApiModelProperty

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/11/19 上午 10:50
 * @description：
 * @version: ：V
 */
class Page {
    @ApiModelProperty(value = "第几页[默认1]", example = "1")
    private val page: Int? = null

    @ApiModelProperty(value = "每页大小[默认10]", example = "10")
    private val pageSize: Int? = null
}
