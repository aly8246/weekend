package com.github.aly8246.core.page

import com.github.aly8246.core.exception.WeekendException

abstract class BasePageResult<T> : BasePage<T> {
    override fun preHandlerPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun transmitPageSize(pageSize: Int) {
        throw WeekendException("Please impl transmitPageSize")
    }

    override fun transmitPage(page: Int) {
        throw WeekendException("Please impl transmitPage")
    }

    override fun transmitPageData(data: T) {
        throw WeekendException("Please impl transmitPageData")
    }

    override fun originalPageParam(pageParam: Any) {

    }

    override fun afterHandlerPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}