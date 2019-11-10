//package com.github.aly8246.core.util
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import java.lang.reflect.Field
//import java.util.*
//
//object MapUtil {
//    fun <T> mapToPojo(map: Map<*, *>?, clazz: Class<T>): T {
//        return ObjectMapper().convertValue(map, clazz)
//    }
//
//    @Throws(Exception::class)
//    fun <T> mapToList(resourceMap: List<Map<*, *>>?, clazz: Class<T>): List<T> {
//        val rtnlist: MutableList<Any> = ArrayList()
//        if (resourceMap == null || resourceMap.size == 0) {
//            return rtnlist as List<T>
//        }
//        for (map in resourceMap) {
//            val tobj: T = clazz.newInstance()
//            for (key in map.keys) {
//                var field: Field? = null
//                if (key.toString() == "_id") {
//                    field = clazz.getDeclaredField("id")
//                    field.isAccessible = true
//                    field.set(tobj, map[key])
//                } else try {
//                    field = clazz.getDeclaredField(key as String)
//                    field.isAccessible = true
//                    if (field.type.simpleName == "int") {
//                        val o: Any = map.get(key)
//                        field.set(tobj, Integer.parseInt(o.toString()))
//                    } else {
//                        field.set(tobj, map[key])
//                    }
//                } catch (e: NoSuchFieldException) {
//                    println("不存在的字段:$e")
//                }
//            }
//            rtnlist.add(tobj)
//        }
//        return rtnlist as List<T>
//    }
//}