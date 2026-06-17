package com.javatutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point for the curriculum demo app.
 *
 * <p>Run with:
 * <pre>mvn spring-boot:run</pre>
 *
 * <p>The tutorial / OOP / data-structure / algorithm classes each ship with
 * their own {@code main(String[])} so they can be executed standalone from an
 * IDE without starting the web app.
 */
@SpringBootApplication
public class JavaTutorialApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaTutorialApplication.class, args);
    }
}
