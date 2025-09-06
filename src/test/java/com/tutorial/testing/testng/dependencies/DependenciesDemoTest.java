package com.tutorial.testing.testng.dependencies;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DependenciesDemoTest {

    @Test
    public void stepA() { System.out.println("stepA"); }

    @Test(dependsOnMethods = "stepA")
    public void stepB() {
        System.out.println("stepB");
        Assert.assertTrue(true);
    }
}

