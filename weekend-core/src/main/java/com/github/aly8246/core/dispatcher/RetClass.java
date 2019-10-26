package com.github.aly8246.core.dispatcher;

import com.github.aly8246.core.annotation.Mapping;
import com.github.aly8246.core.util.WeekendResultObj;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class RetClass {
    private Object retClassInstance;
    private Object realClass;

    public RetClass(Object retClassInstance, Object realClass) {
        this.retClassInstance = retClassInstance;
        this.realClass = realClass;
    }

    public RetClass(Object retClassInstance) {
        this.retClassInstance = retClassInstance;
    }


    public static String regxListParamClass(String source) {
        System.out.println(source);
        Matcher matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source);
        for (; matcher.find(); ) {
            return matcher.group();
        }
        throw new RuntimeException("异常regxListParamClass");
    }


    public RetClassEnum classType() {
        if (this.retClassInstance == null) return RetClassEnum.NULL;
        if (WeekendResultObj.containerCollection(this.retClassInstance.getClass()))
            return RetClassEnum.COLLECTION;
        if (this.retClassInstance.getClass().equals(Page.class))
            return RetClassEnum.PAGE;
        return RetClassEnum.OBJECT;
    }

    public Class<?> clazz() {
        if (this.realClass == null) return null;
        return this.realClass.getClass();
    }

    public boolean containerMapping() {
        if (this.clazz() == null) return false;
        Class<?> clazz = this.clazz();
        Set<Field> fieldList = new HashSet<>(Arrays.asList(clazz().getDeclaredFields()));
        while (clazz != null && !clazz.getName().toLowerCase().equals("java.lang.object")) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        List<Field> mappingField = fieldList
                .stream()
                .filter(e -> e.getAnnotation(Mapping.class) != null)
                .collect(Collectors.toList());
        if (mappingField.size() > 0) return true;

        return false;
    }
}
