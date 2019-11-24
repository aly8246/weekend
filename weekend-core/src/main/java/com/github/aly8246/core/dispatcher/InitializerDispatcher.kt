package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.driver.MongoConnection
import com.github.aly8246.core.driver.MongoResultSet
import com.github.aly8246.core.driver.MongoStatement
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.page.Page
import com.github.aly8246.core.template.RegexTemplate
import com.github.aly8246.core.util.WordUtil
import net.sf.jsqlparser.parser.CCJSqlParserManager
import net.sf.jsqlparser.statement.delete.Delete
import net.sf.jsqlparser.statement.insert.Insert
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.statement.update.Update
import org.springframework.util.StringUtils
import java.io.StringReader

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.sql.Statement

@Suppress("NAME_SHADOWING")
abstract class InitializerDispatcher<T>(proxy: Any, method: Method, args: Array<Any>?, var mongoConnection: MongoConnection, var target: Class<T>) : AbstractDispatcher<T>(proxy, method, args, target) {

    protected lateinit var baseCommand: String

    override fun run(): T? {
        //解析获得参数
        val param = resolverParam(method)

        this.transmitParam(param)

        //从方法的注解中获得sql/或者生成base sql
        baseCommand = this.resolverBaseCommand(method)
        //获得原始sql(模板替换完成)
        var originalCommand = template(baseCommand, param)

        //解析主键ID
        originalCommand = resolverPrimaryKey(originalCommand)

        //将解析好的sql传递给子类
        this.transmitOriginalCommand(originalCommand)

        //创建statement
        val statement = mongoConnection.createStatement()

        //选择执行的statement
        val executorResult = selectStatement(statement, originalCommand, param, statement)

        statement.close()

        return executorResult
    }

    override fun pageParam(): Page {
        throw WeekendException("要获取分页参数必须自己实现此方法")
    }

    //向下传递
    override fun transmitOriginalCommand(originalCommand: String) {

    }

    override fun transmitParam(paramMap: MutableMap<String, Any?>) {
    }

    //获得用户传入的command
    abstract fun resolverBaseCommand(method: Method): String

    //如果存在注解,则注解替换成_id
    abstract fun resolverPrimaryKey(originalCommand: String): String

    //解析参数
    protected fun resolverParam(method: Method): MutableMap<String, Any?> {
        val paramMap: MutableMap<String, Any?> = mutableMapOf()
        for (index in method.parameters.indices) {
            if (args?.get(index) == null) continue
            val typeSimpleName = method.parameters[index].type.simpleName

            when {
                //是不定长数组参数
                method.parameters[index].isVarArgs -> {
                    val paramArray = args!![index] as Array<*>
                    for ((i, param) in paramArray.withIndex()) {
                        when {
                            StringUtils.isEmpty(param) -> {
                            }
                            isBasicDataType(param!!) -> paramMap["param${i + 1}"] = param
                            else -> this.resolverClassParam(null, param, paramMap)
                        }
                    }
                }

                //是普通参数
                method.parameters[index] is Parameter -> {
                    //基础参数
                    if (this.isBasicDataType(args!![index])) {
                        paramMap[method.parameters[index].name] = args!![index]
                    } else if (typeSimpleName == "List" || typeSimpleName == "Set") {//是集合参数
                        val paramArray = args!![index] as MutableList<*>

                        if (paramArray.size > 0) {
                            //基础类型的参数List直接  nameList = 1,2,3,4就行，不用解析类
                            if (this.isBasicDataType(paramArray[0]!!)) {
                                //如果是基础类型的集合
                                paramMap[method.parameters[index].name] = paramArray
                            } else {
                                for (index in 0 until paramArray.size) {
                                    this.resolverClassParam(index.toString(), paramArray[index], paramMap)
                                    //类本身   0.user = user
                                    paramMap["$index." + WordUtil.underscoreName(paramArray[index]!!.javaClass.simpleName)] = paramArray[index]
                                }
                            }
                        }

                        //是集合参数 true
                        paramMap["isBatch"] = "true"
                        //用户传递参数的名称  userList
                        paramMap["paramName"] = method.parameters[index].name
                        //用户传递参数的类实例名称 user
                        paramMap["paramTypeName"] = WordUtil.underscoreName(paramArray[0]!!::class.java.simpleName)
                        //集合大小 10
                        paramMap["batchSize"] = paramArray.size
                    } else {//类参数
                        this.resolverClassParam(null, args!![index], paramMap)
                        paramMap["paramName"] = WordUtil.underscoreName(args!![index].javaClass.simpleName)
                        paramMap["paramTypeName"] = WordUtil.underscoreName(args!![index].javaClass.simpleName)
                        paramMap[WordUtil.underscoreName(args!![index].javaClass.simpleName)] = args!![index]
                    }
                }
            }
        }
        return paramMap
    }

    private fun isBasicDataType(param: Any): Boolean {
        return when (param) {
            is String, Int, Long, Float, Double, Short -> true
            is java.util.Date -> true
            is java.lang.Integer -> true
            is java.lang.Boolean -> true
            is java.lang.String -> true
            is java.lang.Double -> true
            is java.lang.Float -> true
            is java.lang.Short -> true
            else -> false
        }
    }

    protected fun resolverClassParam(paramNamePrefix: String?, param: Any?, paramMap: MutableMap<String, Any?>) {
        if (param == null) return
        val valueInstance = param::class.java
        val declaredFields = valueInstance.declaredFields
        for (field in declaredFields) {
            //class com.other.test.boot.enitiy.User.id
            //User 转小写才是类名
            var name = field.toString().split(".")[field.toString().split(".").size - 2]
            name = WordUtil.toLowerCaseFirstOne(name)

            val declaredField = valueInstance.getDeclaredField(field.name)
            declaredField.isAccessible = true
            val entityValue = declaredField.get(param)
            if (entityValue != null && entityValue != "") {
                if (paramNamePrefix != null) paramMap["$paramNamePrefix.$name.${field.name}"] = entityValue
                else paramMap["$name.${field.name}"] = entityValue
            }
        }
    }

    override fun selectStatement(statement: Statement, command: String, param: MutableMap<String, Any?>, statement1: Statement): T? {
        return when (val sqlStatement = CCJSqlParserManager().parse(StringReader(command.trim()))) {
            is Select -> {
                val executeQuery = statement.executeQuery(command) as MongoResultSet
                executeQuery.init(method, args, this.target)
                executeQuery.getObject() as T ?: return null
            }
            is Insert -> {
                val mongoStatement = statement as MongoStatement
                mongoStatement.param = param
                statement.addBatch(command)
                val mongoResultSet = statement.resultSet as MongoResultSet
                return mongoResultSet.resultRows as T ?: return null
            }
            is Update -> {
                statement.executeUpdate(command)
                val mongoResultSet = statement.resultSet as MongoResultSet
                return mongoResultSet.resultRows as T ?: return null
            }
            is Delete -> {
                statement.execute(command)
                val mongoResultSet = statement.resultSet as MongoResultSet
                return mongoResultSet.resultRows as T ?: return null
            }
            else -> throw WeekendException("暂时不支持的语句 >> $sqlStatement")
        }
    }

    override fun template(baseCommand: String, param: MutableMap<String, Any?>): String = RegexTemplate().completeCommand(baseCommand, param)
}
