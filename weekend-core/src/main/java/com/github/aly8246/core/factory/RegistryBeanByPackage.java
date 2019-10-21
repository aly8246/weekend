package com.github.aly8246.core.factory;

import com.github.aly8246.core.configuration.WeekendProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javax.annotation.Resource;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/21 下午 04:58
 * @description：
 * @version: ：V
 */
//@Configuration
public class RegistryBeanByPackage implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {
//注入配置文件
private MetadataReaderFactory metadataReaderFactory;
private ResourcePatternResolver resourcePatternResolver;
private ApplicationContext applicationContext;
@Resource
private WeekendProperties weekendProperties;


@Override
public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
	
	
	Class<Object> beanClazz = Object.class;
	BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
	GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
	
	//在这里，我们可以给该对象的属性注入对应的实例。
	//比如mybatis，就在这里注入了dataSource和sqlSessionFactory，
	// 注意，如果采用definition.getPropertyValues()方式的话，
	// 类似definition.getPropertyValues().add("interfaceType", beanClazz);
	// 则要求在FactoryBean（本应用中即ServiceFactory）提供setter方法，否则会注入失败
	// 如果采用definition.getConstructorArgumentValues()，
	// 则FactoryBean中需要提供包含该属性的构造方法，否则会注入失败
	definition.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);
	
	//注意，这里的BeanClass是生成Bean实例的工厂，不是Bean本身。
	// FactoryBean是一种特殊的Bean，其返回的对象不是指定类的一个实例，
	// 其返回的是该工厂Bean的getObject方法所返回的对象。
	definition.setBeanClass(ServiceFactory.class);
	
	//这里采用的是byType方式注入，类似的还有byName等
	definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
	registry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
}

@Override
public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//TODO 不做什么
}

@Override
public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	this.applicationContext = applicationContext;
}

@Override
public void setResourceLoader(ResourceLoader resourceLoader) {
	this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
	this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	
}

}
