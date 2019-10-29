package com.github.aly8246.core.util;


import java.util.*;

public class WeekendResultObj {
//是否包含
private static List<Class<?>> listList = new ArrayList<>();
private static List<Class<?>> mapList = new ArrayList<>();
private static List<Class<?>> setList = new ArrayList<>();
private static List<Class<?>> allList = new ArrayList<>();

static {
	listList.add(List.class);
	listList.add(ArrayList.class);
	listList.add(LinkedList.class);
	
	mapList.add(Map.class);
	mapList.add(HashMap.class);
	mapList.add(LinkedHashMap.class);
	
	setList.add(Set.class);
	setList.add(TreeSet.class);
	setList.add(HashSet.class);
	setList.add(LinkedHashSet.class);
	
	allList.addAll(listList);
	allList.addAll(mapList);
	allList.addAll(setList);
}

public static Object getInstance(Class<?> aClass) {
	if (listList.stream().anyMatch(aClass::equals)) return
			                                                new ArrayList<>();
	if (mapList.stream().anyMatch(aClass::equals)) return
			                                               new HashMap<>();
	if (setList.stream().anyMatch(aClass::equals)) return
			                                               new HashSet<>();
	try {
		return aClass.newInstance();
	} catch (InstantiationException | IllegalAccessException ignored) {
		//返回结果集为void
	}
	return null;
}

public static boolean containerCollection(Class<?> aClass) {
	if (allList.stream().anyMatch(aClass::equals)) return true;
	return false;
}

}
