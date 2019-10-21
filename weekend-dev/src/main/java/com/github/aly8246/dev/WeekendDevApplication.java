package com.github.aly8246.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.github.aly8246.core"})
public class WeekendDevApplication {

public static void main(String[] args) {
	SpringApplication.run(WeekendDevApplication.class, args);
}

}
