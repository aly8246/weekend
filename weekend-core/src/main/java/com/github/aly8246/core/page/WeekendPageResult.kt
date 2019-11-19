package com.github.aly8246.core.page

class WeekendPageResult<T> : BasePageResult<T>() {
    private var page: Int? = null
    private var pageSize: Int? = null
    private var totalPage: Int? = null
        get() = pageSize?.div(this.page!!)

    private var data: T? = null

    override fun transmitPageSize(pageSize: Int) {
        this.pageSize = pageSize
    }

    override fun transmitPage(page: Int) {
        this.page = page
    }

    override fun transmitPageData(data: T) {
        this.data = data
    }
}