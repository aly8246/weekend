package com.github.aly8246.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Author ：南有乔木
 * @Email ：1558146696@qq.com
 * @date ：Created in 2019/10/21 下午 05:58
 * @description：
 * @version: ：V
 */
@ConfigurationProperties(
		prefix = "weekend"
)
@Component
public class WeekendProperties {
private Properties properties = new Properties();

public Properties getProperties() {
	return this.properties;
}

private String basePackage = "base-package";

public void setBasePackage(String basePackage) {
	this.properties.setProperty(this.basePackage, basePackage);
}

public String getBasePackage() {
	return this.properties.getProperty(this.basePackage);
}

}
