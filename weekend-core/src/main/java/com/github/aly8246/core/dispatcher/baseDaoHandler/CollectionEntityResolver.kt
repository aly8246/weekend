package com.github.aly8246.core.dispatcher.baseDaoHandler

import com.github.aly8246.core.annotation.Mapping
import com.github.aly8246.core.annotation.WeekendCollection
import com.github.aly8246.core.annotation.WeekendId
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.dispatcher.baseDaoHandler.base.BaseDao
import com.github.aly8246.core.util.WordUtil.Companion.underscoreName
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class CollectionEntityResolver {
    //获取实体的集合名称
    fun <T> collectionName(target: Class<T>): String {
        //如果有@Collection注解，则取value
        val returnClass = this.returnClass(target)
        val returnClassAnnotationList = returnClass.declaredAnnotations.toMutableList()
        for (annotation in returnClassAnnotationList) {
            when (annotation) {
                is WeekendCollection -> {
                    return annotation.value.joinToString("")
                }
            }
        }

        //否则转带下划线的名称
        return underscoreName(returnClass.simpleName)
    }

    fun <T> returnClass(target: Class<T>): Class<*> {
        var resultClassType: Class<*>? = null
        val genericInterfacesTypeList = target.genericInterfaces.toMutableList()
        genericInterfacesTypeList.forEach { e ->
            run {
                if (e.typeName.contains(BaseDao::class.java.name) || e.typeName.contains("WeekendDao")) {
                    val matcher = Pattern.compile("\\<.*\\>").matcher(e.typeName)
                    while (matcher.find()) {
                        val retClassName = matcher.group().replace("<", "").replace(">", "")
                        resultClassType = Class.forName(retClassName).newInstance().javaClass
                    }
                }
            }
        }
        return resultClassType!!
    }

    fun <T> primaryKeyName(target: Class<T>): String {
        //如果类上有@WeekendId字段，则取该字段的名称
//        val returnClass = this.returnClass(target)
//        val fieldList = returnClass.declaredFields.toMutableList()
//        fieldList.forEach { e ->
//            run {
//                val weekendId = e.getDeclaredAnnotation(WeekendId::class.java)
//                if (weekendId != null) return e.name
//            }
//        }
        //否则返回_id
        return "_id"
    }


    //TODO 注意mapping
    //TODO 传递一个entity 返回不为null的字段的名称和值
    fun <T> resolverEntity(entity: Any): MutableMap<String, Any> {
        val resultMap: MutableMap<String, Any> = mutableMapOf()
        val entityClass = entity::class.java
        val declaredFields = entityClass.declaredFields
        for (field in declaredFields) {
            val mapping = field.getDeclaredAnnotation(Mapping::class.java)
            val weekendId = field.getDeclaredAnnotation(WeekendId::class.java)

            var fieldName = if (mapping != null) {
                mapping.name.joinToString("")
            } else {
                underscoreName(field.name)
            }

            //替换主键ID
            if (weekendId != null || fieldName == "id" || fieldName == "_id") {
                fieldName = "_id"
            }

            val declaredField = entity.javaClass.getDeclaredField(field.name)
            declaredField.isAccessible = true
            val entityValue = declaredField.get(entity)
            if (entityValue != null) {
                when (entityValue) {
                    is String -> resultMap["`$fieldName`"] = "'$entityValue'"
                    is Date -> resultMap["`$fieldName`"] = "'" + SimpleDateFormat(configuration.dataFormat).format(Date(entityValue.toString())) + "'"
                    else -> resultMap["`$fieldName`"] = entityValue
                }
            }
        }
        return resultMap
    }

}