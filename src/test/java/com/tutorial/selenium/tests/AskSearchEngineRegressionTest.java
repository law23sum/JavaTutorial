package com.tutorial.selenium.tests;

import com.tutorial.selenium.pages.pageobjectmodel.AskSearchEngineHomePage;
import com.tutorial.selenium.pages.pageobjectmodel.AskSearchEngineResultsPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class AskSearchEngineRegressionTest {
    private static final Logger log = LoggerFactory.getLogger(AskSearchEngineRegressionTest.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        configureWebDriverManagerCache();
        // Normalize input (chrome, firefox). Default to chrome when the TestNG parameter is missing/blank.
        String browserName = (browser == null || browser.isBlank()) ? "chrome" : browser.trim().toLowerCase();

        switch (browserName) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

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

    private void configureWebDriverManagerCache() {
        Path cacheDir = Paths.get("target", "wdm-cache").toAbsolutePath();
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create WebDriverManager cache directory " + cacheDir, e);
        }
        System.setProperty("wdm.cachePath", cacheDir.toString());
        System.setProperty("wdm.resolutionCachePath", cacheDir.resolve("resolution.properties").toString());
    }
}
