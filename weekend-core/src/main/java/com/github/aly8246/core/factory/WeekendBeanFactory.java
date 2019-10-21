package com.github.aly8246.core.factory;

import com.github.aly8246.core.annotation.WeekendDaoScan;
import com.github.aly8246.core.print.WeekendPrint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/21 下午 04:58
 * @description：
 * @version: ：V
 */
@Component
public class WeekendBeanFactory implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourcePatternResolver resourcePatternResolver;
    private MetadataReaderFactory metadataReaderFactory;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        AnnotationAttributes mapperScansAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(WeekendDaoScan.class.getName()));
        String[] packages = (String[]) mapperScansAttrs.get("value");
        Set<Class<?>> classSet = new HashSet<>();
        for (String aPackage : packages) {
            classSet.addAll(this.scannerPackages(aPackage));
        }
        for (Class<?> clazz : classSet) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
            definition.setBeanClass(WeekendProxyFactory.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(clazz.getSimpleName(), definition);
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.err.println("registerBeanDefinitions");
    }


    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    //com.github.aly8246.dev.mDao
    //to
    //classpath*:com/github/aly8246/dev/mDao/**/*.class
    private String covertVasePackage(String basePackage) {
        basePackage = basePackage.replace(".", "/");
        return ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                basePackage +
                '/' +
                DEFAULT_RESOURCE_PATTERN;
    }


    private Set<Class<?>> scannerPackages(String basePackage) {

        Set<Class<?>> set = new LinkedHashSet<>();
        String packageSearchPath = covertVasePackage(basePackage);
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                        WeekendPrint.err("Weekend Inject:  " + className);
                        set.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }
}
