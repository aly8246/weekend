package com.github.aly8246.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtil {
    public static <T> T mapToPojo(Map map, Class<T> clazz) {
        return new ObjectMapper().convertValue(map, clazz);
    }

    public static <T> List<T> mapToList(List<Map> resourceMap, Class<T> clazz) throws Exception {
        List<Object> rtnlist = new ArrayList<>();
        if (resourceMap == null || resourceMap.size() == 0) {
            return (List<T>) rtnlist;
        }
        for (Map map : resourceMap) {
            Object tobj = clazz.newInstance();
            for (Object key : map.keySet()) {
                Field field = null;
                if (key.toString().equals("_id")) {
                    field = clazz.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(tobj, map.get(key));
                } else
                    try {
                        field = clazz.getDeclaredField((String) key);
                        field.setAccessible(true);
                        if (field.getType().getSimpleName().equals("int")) {
                            Object o = map.get(key);
                            field.set(tobj, Integer.parseInt(o.toString()));
                        } else {
                            field.set(tobj, map.get(key));
                        }
                    } catch (NoSuchFieldException e) {
                        System.out.println("不存在的字段:" + e);
                    }
            }
            rtnlist.add(tobj);
        }
        return (List<T>) rtnlist;
    }
}
