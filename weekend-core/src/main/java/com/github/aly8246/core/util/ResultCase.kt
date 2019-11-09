package com.github.aly8246.core.util

import java.util.*
import kotlin.reflect.KClass

object ResultCase {
    private val listList = ArrayList<Class<*>>()
    private val mapList = ArrayList<Class<*>>()
    private val setList = ArrayList<Class<*>>()
    public val allList = ArrayList<Class<*>>()

    init {
        listList.add(List::class.java)
        listList.add(ArrayList::class.java)
        listList.add(LinkedList::class.java)

        mapList.add(Map::class.java)
        mapList.add(HashMap::class.java)
        mapList.add(LinkedHashMap::class.java)

        setList.add(Set::class.java)
        setList.add(TreeSet::class.java)
        setList.add(HashSet::class.java)
        setList.add(LinkedHashSet::class.java)

        allList.addAll(listList)
        allList.addAll(mapList)
        allList.addAll(setList)
    }

    fun getInstance(aClass: Class<*>?): Any? {
        if (aClass == List::class.java) return ArrayList<Any>()
        if (listList.stream().anyMatch { aClass == it }) return ArrayList<Any>()
        if (mapList.stream().anyMatch { aClass == it })
            return HashMap<Any, Any>()
        if (setList.stream().anyMatch { aClass == it })
            return HashSet<Any>()
        try {
            return aClass?.newInstance()
        } catch (ignored: InstantiationException) {
            //返回结果集为void
        } catch (ignored: IllegalAccessException) {
        }

        return null
    }

    fun containerCollection(javaClass: Class<*>?): Boolean {
        return allList.stream().anyMatch { javaClass == it }
    }


}
