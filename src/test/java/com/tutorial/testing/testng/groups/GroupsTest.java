package com.tutorial.testing.testng.groups;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class GroupsTest {

    @BeforeGroups("smoke")
    public void beforeSmoke() { System.out.println("Setting up for smoke tests"); }

    @Test(groups = {"smoke"})
    public void fastCheck() { Assert.assertTrue(true); }

    @Test(groups = {"regression"})
    public void deeperCheck() { Assert.assertEquals("abc".toUpperCase(), "ABC"); }
}

