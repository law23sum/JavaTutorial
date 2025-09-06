package com.tutorial.testing.testng.annotations;

import org.testng.annotations.*;

public class AnnotationsTest {

    @BeforeSuite
    public void beforeSuite() { System.out.println("@BeforeSuite"); }

    @AfterSuite
    public void afterSuite() { System.out.println("@AfterSuite"); }

    @BeforeClass
    public void beforeClass() { System.out.println("@BeforeClass"); }

    @AfterClass
    public void afterClass() { System.out.println("@AfterClass"); }

    @BeforeMethod
    public void beforeMethod() { System.out.println("@BeforeMethod"); }

    @AfterMethod
    public void afterMethod() { System.out.println("@AfterMethod"); }

    @Test
    public void sampleTest1() { System.out.println("@Test sampleTest1"); }

    @Test
    public void sampleTest2() { System.out.println("@Test sampleTest2"); }
}

