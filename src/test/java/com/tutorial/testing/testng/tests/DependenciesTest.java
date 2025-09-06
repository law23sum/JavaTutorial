package com.tutorial.testing.testng.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DependenciesTest {

    @Test
    public void stepA() { System.out.println("stepA"); }

    @Test(dependsOnMethods = "stepA")
    public void stepB() {
        System.out.println("stepB");
        Assert.assertTrue(true);
    }
}

