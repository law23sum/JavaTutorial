package com.javatutorial.automation.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * <h2>DriverFactory</h2>
 * Centralizes WebDriver creation. Uses {@link WebDriverManager} so the
 * correct browser binary is fetched automatically — no manual driver
 * downloads.
 *
 * <p>Honors two system properties:
 * <ul>
 *   <li>{@code -Dbrowser=chrome} (default)</li>
 *   <li>{@code -Dheadless=true|false} (default {@code true})</li>
 * </ul>
 */
public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver newDriver() {
        String browser  = System.getProperty("browser",  "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        if (!"chrome".equals(browser)) {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        if (headless) opts.addArguments("--headless=new", "--disable-gpu", "--window-size=1280,800");

        WebDriver driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }
}
