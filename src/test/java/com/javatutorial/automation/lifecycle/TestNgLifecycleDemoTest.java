package com.javatutorial.automation.lifecycle;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * <h2>TestNG Lifecycle Cheat Sheet</h2>
 *
 * <pre>
 *   &#64;BeforeSuite          // once before everything
 *     &#64;BeforeTest         // once per &lt;test&gt; tag in testng.xml
 *       &#64;BeforeClass      // once per class
 *         &#64;BeforeMethod   // before every &#64;Test
 *           &#64;Test
 *         &#64;AfterMethod
 *       &#64;AfterClass
 *     &#64;AfterTest
 *   &#64;AfterSuite
 * </pre>
 *
 * Run this single class to see the full sequence on the console.
 */
public class TestNgLifecycleDemoTest {

    @BeforeSuite  public void beforeSuite()  { System.out.println("@BeforeSuite"); }
    @BeforeTest   public void beforeTest()   { System.out.println("  @BeforeTest"); }
    @BeforeClass  public void beforeClass()  { System.out.println("    @BeforeClass"); }
    @BeforeMethod public void beforeMethod() { System.out.println("      @BeforeMethod"); }

    @Test public void testOne() { System.out.println("        @Test testOne"); }
    @Test public void testTwo() { System.out.println("        @Test testTwo"); }

    @AfterMethod public void afterMethod() { System.out.println("      @AfterMethod"); }
    @AfterClass  public void afterClass()  { System.out.println("    @AfterClass"); }
    @AfterTest   public void afterTest()   { System.out.println("  @AfterTest"); }
    @AfterSuite  public void afterSuite()  { System.out.println("@AfterSuite"); }
}
