package com.github.aly8246.core.dispatcher

import com.github.aly8246.core.annotation.Mapping
import com.github.aly8246.core.util.ResultCase
import lombok.Data
import org.springframework.data.domain.Page

import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors

@Data
class RetClass(private val retClassInstance: Any?, private val realClass: Any?) {


    fun classType(): RetClassEnum {
        return when {
            this.retClassInstance == null -> RetClassEnum.NULL
            ResultCase.containerCollection(this.retClassInstance.javaClass) -> RetClassEnum.COLLECTION
            else -> if (this.retClassInstance.javaClass == Page::class.java) RetClassEnum.PAGE else RetClassEnum.OBJECT
        }
    }

    fun clazz(): Class<*>? = if (this.realClass == null) null else this.realClass.javaClass

    fun containerMapping(): Boolean {
        return when {
            this.clazz() == null -> false
            else -> {
                var clazz = this.clazz()
                val fieldList = HashSet(listOf(*clazz()!!.declaredFields))
                while (clazz != null && clazz.name.toLowerCase() != "java.lang.object") {
                    fieldList.addAll(listOf(*clazz.declaredFields))
                    clazz = clazz.superclass
                }

                fieldList.stream().filter { e -> e.getAnnotation(Mapping::class.java) != null }.count() > 0
            }
        }

    }
}



