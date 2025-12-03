package com.framework.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Spring Boot application. The default component scan only inspects the
 * package of this class (and children), so we expand it to cover the tutorial packages that hold
 * the REST controllers and console demo infrastructure.
 */
@SpringBootApplication(scanBasePackages = "com")
public class SpringApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }
}
