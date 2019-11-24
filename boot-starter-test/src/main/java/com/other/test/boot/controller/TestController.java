package com.other.test.boot.controller;

import com.github.aly8246.core.page.Page;
import com.github.aly8246.core.page.PageResult;
import com.other.test.boot.config.Result;
import com.other.test.boot.enitiy.User;
import com.other.test.boot.service.TestService;
import com.other.test.boot.util.IDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/")
@Api(tags = "用户管理")
@Validated
@RequiredArgsConstructor
public class TestController {
private final TestService testService;

@PostMapping
@ApiOperation("新增一个用户")
public Result insertOne(@RequestBody User user) {
	user.setId(IDUtil.createID());
	user.setAddress(null);
	int i = testService.insertOne(user);
	String msg = "新增了:" + i + "行记录";
	return Result.success(msg, null);
}

@PostMapping("multiple/")
@ApiOperation("新增多个用户")
public Result insertList(@RequestBody List<User> userList) {
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

@GetMapping("userEntity/")
@ApiOperation("查询指定用户[根据id，但是是创建了一个实体去查询]")
public Result<User> selectByUser(String userId) {
	User user = new User();
	user.setId(userId);
	return Result.success(testService.selectByUser(user));
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

@GetMapping("imitatePage/")
@ApiOperation("模拟分页查询")
@ApiImplicitParams({
		@ApiImplicitParam(name = "start", value = "开始序号"),
		@ApiImplicitParam(name = "end", value = "结束序号")
})
public Result<List<User>> selectByPage(Integer start, Integer end) {
	List<User> userList = testService.selectByPage(start, end);
	return Result.success(userList);
}

@DeleteMapping("{id}")
@ApiOperation("删除一个用户")
public Result deleteById(@PathVariable("id") String id) {
	int i = testService.deleteById(id);
	String msg = "删除了:" + i + "行记录";
	return Result.success(msg, null);
}

@DeleteMapping("idIn/")
@ApiOperation("删除多个用户[根据id]")
public Result deleteByIdIn(@RequestParam(value = "idList") List<String> idList) {
	int i = testService.deleteByIdIn(idList);
	String msg = "删除了:" + i + "行记录";
	return Result.success(msg, null);
}

@PutMapping
@ApiOperation("更新一个用户的名称[根据id]")
@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "用户id"),
		@ApiImplicitParam(name = "userName", value = "用户名称")
})
public Result updateByIdIs(String userId, String userName) {
	int i = testService.updateNameByIdIs(userId, userName);
	String msg = "更新了:" + i + "行记录";
	return Result.success(msg, null);
}

@PutMapping("byUser")
@ApiOperation("更新一个用户的名称[根据实体类]")
public Result updateByUser(@RequestBody @Validated User user) {
	int i = testService.updateByUser(user);
	String msg = "更新了:" + i + "行记录";
	return Result.success(msg, null);
}

@ApiOperation("分页查询[新建一个类来查询]")
@GetMapping("page")
@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "第几页"),
		@ApiImplicitParam(name = "pageSize", value = "页大小")
})
public Result<PageResult<User>> selectPage(Page page, String name) {
	return Result.success(testService.selectPage(page, name));
}

@ApiOperation("分页查询[传递单个参数]")
@GetMapping("pageByParam")
@ApiImplicitParams({
		@ApiImplicitParam(name = "page", value = "第几页"),
		@ApiImplicitParam(name = "pageSize", value = "页大小")
})
public Result<PageResult<User>> selectPageByParam(Page page, String name) {
	return Result.success(testService.selectPageByParam(page, name));
}

@ApiOperation("分页查询[用户自定义无参分页]")
@GetMapping("customPage")
public Result<PageResult<User>> selectPageCustom(Page page) {
	return Result.success(testService.customPage(page));
}
}
