package com.other.test.boot.controller;

import com.other.test.boot.config.Result;
import com.other.test.boot.enitiy.User;
import com.other.test.boot.service.TestService;
import com.other.test.boot.util.IDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/")
@Api(tags = "用户管理")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @PostMapping
    @ApiOperation("新增一个用户")
    public Result insertOne(@RequestBody User user) {
        System.out.println(user);
        user.setId(IDUtil.createID());
        int i = testService.insertOne(user);
        String msg = "新增了:" + i + "行记录";
        return Result.success(msg, null);
    }

    @PostMapping("multiple/")
    @ApiOperation("新增多个用户")
    public Result insertList(@RequestBody List<User> userList) {
        System.out.println(userList);
        for (User user : userList) {
            user.setId(IDUtil.createID());
        }
        int i = testService.insertList(userList);
        String msg = "新增了:" + i + "行记录";
        return Result.success(msg, null);
    }

    @GetMapping
    @ApiOperation("查询全部用户")
    public Result<List<User>> selectAll() {
        List<User> userList = testService.selectAll();
        return Result.success(userList);
    }

    @GetMapping("{id}")
    @ApiOperation("查询指定用户[根据id]")
    public Result<User> selectById(@PathVariable("id") String id) {
        User user = testService.selectById(id);
        return Result.success(user);
    }

    @GetMapping("byNameIn/")
    @ApiOperation("查询用户名为?")
    public Result<List<User>> selectByNameIn(@RequestParam(value = "nameList") List<String> nameList) {
        List<User> userList = testService.selectByNameIn(nameList);
        return Result.success(userList);
    }

    @GetMapping("orderByAge/")
    @ApiOperation("查询所有用户并根据年龄排序[根据type来动态选择sql段执行,如果条件都不满足则执行else,如果不传入参数则整个when无效]")
    @ApiImplicitParam(name = "type", value = "排序类型: 1正序 2倒序")
    public Result<List<User>> selectAllOrderByAge(Integer type) {
        List<User> userList = testService.selectAllOrderByAge(type);
        return Result.success(userList);
    }

    @GetMapping("page/")
    @ApiOperation("模拟分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "开始序号"),
            @ApiImplicitParam(name = "end", value = "结束序号")
    })
    public Result<List<User>> selectByPage(Integer start, Integer end) {
        List<User> userList = testService.selectByPage(start, end);
        return Result.success(userList);
    }


}
