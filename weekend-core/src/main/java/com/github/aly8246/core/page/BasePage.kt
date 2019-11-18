package com.github.aly8246.core.page

interface BasePage {

    //我的接口，让分页类实现设置 查询总条数，分页总数，分页大小，第几页，分页数据
    //当用户查询完了以后我为用户的分页类设置信息，并且由用户自己的分页类来完善一些操作，最后返回给前端
}