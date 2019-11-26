package com.github.aly8246.weekend.spring.register

import com.github.aly8246.core.util.PrintImpl
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanNameGenerator
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.*
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver.*
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.core.type.classreading.MetadataReaderFactory
import java.io.IOException


/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/21 下午 04:58
 * @description：
 * @version: ：V
 */
@Configuration
open class WeekendDaoBatchRegister : ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private var resourcePatternResolver: ResourcePatternResolver? = null
    private var metadataReaderFactory: MetadataReaderFactory? = null

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry, importBeanNameGenerator: BeanNameGenerator?) {
        val mapperScansAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(WeekendDaoScan::class.java.name))
        val packages = mapperScansAttrs!!["value"] as Array<*>
        val classSet = HashSet<Class<*>>()
        for (aPackage in packages) {
            classSet.addAll(this.scannerPackages(aPackage as String))
        }
        classSet.forEach { clazz ->
            val builder = BeanDefinitionBuilder.genericBeanDefinition(clazz)
            val definition = builder.rawBeanDefinition as GenericBeanDefinition
            definition.constructorArgumentValues.addGenericArgumentValue(clazz)
            definition.beanClass = WeekendProxyFactory::class.java
            definition.autowireMode = GenericBeanDefinition.AUTOWIRE_BY_TYPE
            registry.registerBeanDefinition(clazz.simpleName, definition)
        }

    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata?, registry: BeanDefinitionRegistry?) {
        System.err.println("registerBeanDefinitions")
    }

    //com.github.aly8246.dev.mDao
    //to
    //classpath*:com/github/aly8246/dev/mDao/**/*.class
    private fun covertVasePackage(basePackage: String): String =
            CLASSPATH_ALL_URL_PREFIX +
                    basePackage.replace(".", "/") +
                    '/'.toString() +
                    DEFAULT_RESOURCE_PATTERN


    private fun scannerPackages(basePackage: String): Set<Class<*>> {
        val set = LinkedHashSet<Class<*>>()
        val packageSearchPath = covertVasePackage(basePackage)
        try {
            val resources = this.resourcePatternResolver!!.getResources(packageSearchPath)
            for (resource in resources) {
                if (resource.isReadable) {
                    val metadataReader = this.metadataReaderFactory!!.getMetadataReader(resource)
                    val className = metadataReader.classMetadata.className
                    val clazz: Class<*>
                    try {
                        clazz = Class.forName(className)
                        PrintImpl().info("Inject  $className")
                        set.add(clazz)
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return set
    }

    override fun setResourceLoader(resourceLoader: ResourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
        this.metadataReaderFactory = CachingMetadataReaderFactory(resourceLoader)
    }

    companion object {
        private const val DEFAULT_RESOURCE_PATTERN = "**/*.class"
    }
}
