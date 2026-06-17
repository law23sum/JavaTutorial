package com.javatutorial.automation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2>Custom Annotation: {@code @TestCategory}</h2>
 * Tag a test method with a category string (e.g. {@code "Smoke"},
 * {@code "Regression"}). A custom runner can then filter and execute only
 * tests matching the desired category at runtime.
 *
 * <pre>
 *   &#64;Test
 *   &#64;TestCategory("Smoke")
 *   public void verifyLogin() { ... }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestCategory {
    String value();
}
