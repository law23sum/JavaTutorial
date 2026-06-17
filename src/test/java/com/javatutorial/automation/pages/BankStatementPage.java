package com.javatutorial.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * <h2>BankStatementPage</h2>
 * Matches the {@code BankStatementAnalyzer} cheat-sheet example. Looks up an
 * account, then reads a (date, balance) table to find the largest balance.
 */
public class BankStatementPage extends BasePage {

    private static final By ACCOUNT_ID = By.id("accountId");
    private static final By SEARCH_BTN = By.id("searchBtn");
    private static final By ROWS       = By.cssSelector("table tr");

    public BankStatementPage(WebDriver driver) { super(driver); }

    public void searchAccount(String accountId) {
        type(ACCOUNT_ID, accountId);
        clickWhenClickable(SEARCH_BTN);
    }

    /** @return [maxDate, maxBalance] or {@code ["", 0.0]} if the table is empty. */
    public Object[] findMaxBalance() {
        List<WebElement> rows = driver.findElements(ROWS);
        double maxBalance = 0.0;
        String maxDate    = "";
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 2) {
                String date    = cells.get(0).getText();
                double balance = Double.parseDouble(cells.get(1).getText());
                if (balance > maxBalance) {
                    maxBalance = balance;
                    maxDate    = date;
                }
            }
        }
        return new Object[]{maxDate, maxBalance};
    }
}
