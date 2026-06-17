package com.javatutorial.automation.tests;

import com.javatutorial.automation.annotations.TestCategory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * <h2>Custom-Annotation Demo</h2>
 * Each test method is also tagged with our {@link TestCategory} annotation so
 * an external runner could pick a category (e.g. {@code "Smoke"}) via
 * reflection and execute only matching tests.
 */
public class AssertionCategoryTest {

    @Test
    @TestCategory("Smoke")
    public void verifyAssertionSmoke() {
        System.out.println("Executing Smoke Test - core sanity");
        Assert.assertTrue(5 > 2, "Smoke assertion failed");
    }

    @Test
    @TestCategory("Regression")
    public void verifyAssertionRegression() {
        System.out.println("Executing Regression Test - extended check");
        Assert.assertEquals("expected", "expected");
    }
}
