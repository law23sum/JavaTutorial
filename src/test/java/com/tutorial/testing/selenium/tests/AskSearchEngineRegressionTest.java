package com.tutorial.testing.selenium.tests;

import com.tutorial.testing.selenium.pages.pageobjectmodel.AskSearchEngineHomePage;
import com.tutorial.testing.selenium.pages.pageobjectmodel.AskSearchEngineResultsPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class AskSearchEngineRegressionTest {
    private static final Logger log = LoggerFactory.getLogger(AskSearchEngineRegressionTest.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        // spin up your driver however you do it; example with Chrome:
        System.clearProperty("webdriver.chrome.driver"); // ensure Selenium Manager is used
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().window().setSize(new Dimension(1280, 900));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void search_showsResults() {
        AskSearchEngineHomePage home = new AskSearchEngineHomePage(driver);
        home.load();
        Assert.assertTrue(home.isAt(), "Not on Ask.com home page");
        AskSearchEngineResultsPage results = home.search("selenium");
        Assert.assertFalse(results.firstResultTitle().isBlank(), "First result title was blank");
        log.info("Automation Single Test Case Sample: Success");
    }
}