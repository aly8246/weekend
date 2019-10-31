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
        if (this.retClassInstance == null) return RetClassEnum.NULL
        if (ResultCase.containerCollection(this.retClassInstance.javaClass))
            return RetClassEnum.COLLECTION
        return if (this.retClassInstance.javaClass == Page::class.java) RetClassEnum.PAGE else RetClassEnum.OBJECT
    }

    fun clazz(): Class<*>? {
        return if (this.realClass == null) null else this.realClass.javaClass
    }

    fun containerMapping(): Boolean {
        if (this.clazz() == null) return false
        var clazz = this.clazz()
        val fieldList = HashSet(Arrays.asList(*clazz()!!.declaredFields))
        while (clazz != null && clazz.name.toLowerCase() != "java.lang.object") {
            fieldList.addAll(Arrays.asList(*clazz.declaredFields))
            clazz = clazz.superclass
        }

        return fieldList.stream().filter { e -> e.getAnnotation(Mapping::class.java) != null }
                .count() > 0
    }
}



