package com.github.aly8246.core.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageResult<T> {

private Integer total;
private Integer totalPage;
private Integer page;
private Integer pageSize;
private List<T> data;
}