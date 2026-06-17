package com.javatutorial.automation.tests;

import com.javatutorial.automation.driver.DriverFactory;
import com.javatutorial.automation.pages.BankStatementPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * <h2>BankStatementAnalyzerTest</h2>
 * End-to-end Selenium walk-through that mirrors the cheat-sheet example:
 * navigate to a bank URL, search for an account, find the largest balance
 * in the (date, balance) table.
 *
 * <p>Tagged with TestNG group <b>"ui"</b> so it's <i>excluded by default</i>
 * (no browser required for {@code mvn test}). Run it explicitly with:
 *
 * <pre>mvn test -Pui -Dtest=BankStatementAnalyzerTest</pre>
 */
public class BankStatementAnalyzerTest {

    private WebDriver driver;
    private BankStatementPage page;

    @BeforeClass(groups = "ui")
    public void setUp() {
        driver = DriverFactory.newDriver();
        driver.get("https://example.com/bank");   // replace with real URL
        page = new BankStatementPage(driver);
    }

    @Test(groups = "ui")
    public void findsMaxBalanceForAccount() {
        page.searchAccount("12345");
        Object[] result = page.findMaxBalance();
        System.out.println("Max balance " + result[1] + " on " + result[0]);
    }

    @AfterClass(groups = "ui", alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
