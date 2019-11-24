## weekend

> 一款轻量级mongodb持久层框架，像mybatis查询mysql一样，组合动态sql查询mongodb.还集成了类似通用mapper这样的通用方法，无需学习开箱即用！

> 本工具遵从springboot的设定理念:约定大于配置

> 使用本插件可以开箱即用，如果日常使用基本上无需任何配置[数据库信息除外]，如果你需要对某些地方进行定制修改，也提供了一些注解进行配置修改

[在线示例，使用weekend实现的基本crud操作](http://148.70.16.82:9999/doc.html) <<--**点此查看在线示例**

## 什么是mongodb
> MongoDB 是一个基于分布式文件存储的数据库。由 C++ 语言编写。旨在为 WEB 应用提供可扩展的高性能数据存储解决方案。
> MongoDB 是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的

**mongodb的出现解决了关系型数据库一些问题**
1. mongodb更适合读取较多的业务，有时候被当成中间库，就好比我公司的物联网海量数据都使用mongodb来存放，再然后读取到mysql进行持久存放，如果内存够大，则mongodb的效率会很快

2. mongodb新增库，表，字段，都是直接update一下就行，如果一个表没有money字段，则直接update money=100 ，这个表就有money字段了，适合在初期还没有正式确定数据表的情况下使用

3. mongodb自带了分布式文件系统，自带横向扩展的集群部署，集群每新增一台mongodb节点，则性能也会成倍的增强，还自带对一些运算框架的支持，这让mongodb在海量数据前成为了首选

**mongodb这么好？为什么工作上还是少见呢？还有的人根本就没有了解过mongodb是什么**
mysql和mongodb对比:

```sql
select * from user_info where userMoney in (500,700) and age=18
```

```javascript
db.user_info.find({
    "userMoney": {
        "$in": [500, 700]
    },
    "age": 18
});
```

在简单查询条件里就已经有了一个疑问，明明mysql这么简洁，为什么mongodb要加上这么多中括号，大括号呢？
**原因不得而知，谁也无法证明bson和sql哪个执行更快，或许这就是mongodb用的人少的原因，学一个数据库又得学一大堆东西**

## mongodb其实在日常web开发中，很多需求更加适合mongodb
你会选择使用mongodb吗？或许你会，但是更多人不会

**如果可以使用mysql来简单查询mongodb你又会使用吗**

> Q >  还有这等好事?那我肯定要试试啊

---


**逐渐进入主题**
大家都知道mybatis，只需要一个接口，一个xml或者一个sql注解就可以查询mysql数据库，而mybatis使用了jdk动态代理来为用户所提供的接口来实现增强

那么我们先写一个接口

```java
public interface TestMongoDao {
    List<UserInfo> selectAll();
}
```
为selectAll方法加上sql注解

```java
public interface TestMongoDao {
	@Command("select * from user_info")
    List<UserInfo> selectAll();
}
```


然后直接注入接口

```java
    @Resource
    private UserDao userDao;
```
直接使用试试

```java
List<UserInfo> userInfoList = userDao.select();
userInfoList.forEach(System.err::println)
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191110123940153.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM1NDI1MjQz,size_16,color_FFFFFF,t_70)

> Q >>  这个看起来像mybatis使用注解sql来查询mysql数据库，这个和mongodb有什么关系呢？这个注解怎么看起来怪怪的？

> A >>  首先，这个查询出来的数据其实是mongodb里的数据，因为我正在尝试开发一款名为weekend的应用,可以像mybatis一样来查询mongodb，除了一些注解不大一样以外

**需要做什么呢？**

step1. pom依赖
```bash
<dependencies>
	 <dependency>
	      <groupId>com.github.aly8246</groupId>
	      <artifactId>weekend</artifactId>
	      <version>1.1.0.RELEASE</version>
	 </dependency>
	 <dependency>
	       <groupId>org.jetbrains.kotlin</groupId>
	       <artifactId>kotlin-stdlib-jdk8</artifactId>
	       <version>1.3.50</version>
	  </dependency>
	  <dependency>
	       <groupId>org.jetbrains.kotlin</groupId>
	       <artifactId>kotlin-test</artifactId>
	       <version>1.3.50</version>
	       <scope>test</scope>
	   </dependency>
    </dependencies>
<build>
    <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
          <plugin>
              <groupId>org.jetbrains.kotlin</groupId>
              <artifactId>kotlin-maven-plugin</artifactId>
              <version>1.3.50</version>
              <executions>
                  <execution>
                      <id>compile</id>
                      <phase>compile</phase>
                      <goals>
                          <goal>compile</goal>
                      </goals>
                  </execution>
                  <execution>
                      <id>test-compile</id>
                      <phase>test-compile</phase>
                      <goals>
                          <goal>test-compile</goal>
                      </goals>
                  </execution>
              </executions>
              <configuration>
                  <jvmTarget>1.8</jvmTarget>
              </configuration>
          </plugin>
          <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <executions>
                  <execution>
                      <id>compile</id>
                      <phase>compile</phase>
                      <goals>
                          <goal>compile</goal>
                      </goals>
                  </execution>
                  <execution>
                      <id>testCompile</id>
                      <phase>test-compile</phase>
                      <goals>
                          <goal>testCompile</goal>
                      </goals>
                  </execution>
              </executions>
          </plugin>
      </plugins>
 </build>
```
step2.配置文件
```yaml
weekend:
  mongodb:
    uri: jdbc:mongodb://localhost:27017/weekend
    driver-name: com.github.aly8246.core.driver.MongoDriver
    username: root
    password: 123456
```
step3.启动类扫描

```kotlin
@WeekendDaoScan(
        "com.github.aly8246.kotlin.mdo",
        "com.github.aly8246.kotlin.dd"
)
```

step4.写接口和注解sql然后注入接口来查询




**不幸的是这个应用刚刚起步**
**Unfortunately, this application is just starting**
但是没有关系，所有的基础设施基本上已经完善，接下来只需要花时间去维护和开发
But it doesn't matter. All the infrastructure is basically complete. Next, it only takes time to maintain and develop


[已经有了第一个可以正常crud的版本](https://github.com/aly8246/weekend/tree/master/boot-starter-test)  <-点此查看示例源码


**weekend**
n. 	星期六和星期日; 周末; 星期六和星期日(或略长一点的)休息时间;
v. 	过周末; 度周末;

> 取名自英文星期天，寓意如果你只是简单使用mongodb进行新增查询，那么你不必要再去学习mongodb的语法，如果weekend能让你不用去学习mongodb就能熟练使用，如果weekend能让你节约出一个weekend[节约出周末时间]，那我的初衷就达到了


> The name comes from English Weekend, which means that if you simply use mongodb, then you don't need to learn mongodb's syntax. If you can use mongodb skillfully without learning it, if you can release a weekend time, then my original intention is achieved

---
## Thanks for I can help everyone!


起初使用java进行开发，可是后来由于某些原因，我转向kotlin，不过java和kotlin都是我的最爱，成年人就应该全都要!
