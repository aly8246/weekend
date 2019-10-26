package com.github.aly8246.core.page;

import java.io.Serializable;

public class PageResult<T> {
    private int page;
    private int pageSize;
    private int total;
    private int totalPage;
    private T data;
}
