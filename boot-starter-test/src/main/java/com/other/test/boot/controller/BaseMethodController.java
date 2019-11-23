package com.other.test.boot.controller;

import com.other.test.boot.config.Result;
import com.other.test.boot.enitiy.User;
import com.other.test.boot.service.BaseMethodService;
import com.other.test.boot.util.IDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("user/baseMethod/")
@Api(tags = "用户管理[通用方法]")
@Validated
@RequiredArgsConstructor
public class BaseMethodController {
    private final BaseMethodService baseMethodService;

    @PostMapping
    @ApiOperation("[insertOne]新增一个用户")
    public Result insertOne(@RequestBody User user) {
        System.out.println(user);
        user.setId(IDUtil.createID());
        user.setAddress("");
        int i = baseMethodService.insertOne(user);
        String msg = "新增了:" + i + "行记录";
        return Result.success(msg, null);
    }

    @PostMapping("batch/")
    @ApiOperation("[insertAll]新增多个用户")
    public Result insertBatch(@RequestBody List<User> userList) {
        int i = baseMethodService.insertAll(userList);
        String msg = "新增了:" + i + "行记录";
        return Result.success(msg, null);
    }

}
