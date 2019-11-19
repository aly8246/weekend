package com.github.aly8246.core.dispatcher.baseDaoHandler

import com.github.aly8246.core.annotation.WeekendCollection
import com.github.aly8246.core.base.BaseDao
import com.github.aly8246.core.util.WordUtil.Companion.camelToUnderline
import java.util.regex.Pattern


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
        return camelToUnderline(returnClass.simpleName)
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
}