package com.tutorial.testing.selenium.pages.pageobjectmodel;

import com.tutorial.testing.selenium.pages.common.BasePage;
import com.tutorial.testing.selenium.pages.common.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object for Ask.com search results.
 */
public class AskSearchEngineResultsPage extends BasePage implements Page.Verifiable {

    // Locator: first result link under the "SEARCH RESULTS" section
    private static final By ASK_FIRST_RESULT = By.xpath("//header[.='SEARCH RESULTS']/following-sibling::*[1]//a[contains(@class,'result-title-link')]");

    public AskSearchEngineResultsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        return hasAny(ASK_FIRST_RESULT);
    }

    public String firstResultTitle() {
        WebElement first = waitUntil(ExpectedConditions.visibilityOfElementLocated(ASK_FIRST_RESULT));
        return first.getText();
    }

    private boolean hasAny(By by) {
        List<WebElement> els = driver.findElements(by);
        return els != null && !els.isEmpty();
    }
}
