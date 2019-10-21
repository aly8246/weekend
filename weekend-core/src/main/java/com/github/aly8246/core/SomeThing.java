package com.github.aly8246.core;


public class SomeThing {
//TODO step1.用户通过@Mql注解来书写mysql代码
//@Mql("select * from user where id=#{userId}")
//TODO step2.用户通过参数上面的@Param注解来指定参数
//List<UserVo> selectUser(@Param("userId")String userId)

//TODO step3.拦截接口并且解析SQL
//select * from user id= #{userId}
//解析到select操作,然后解析查询所有，主表为user 参数为id

//TODO step4.组装查询器，主表名称就是user， 根据userId组装Query查询条件

//TODO step5.将查询结果组装成List
}
