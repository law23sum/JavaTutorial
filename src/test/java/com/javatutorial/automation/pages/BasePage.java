package com.javatutorial.automation.pages;

import com.javatutorial.automation.exceptions.ElementNotClickableException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * <h2>BasePage</h2>
 * Foundation of the Page Object Model. Concrete page classes extend this and
 * inherit safe, reusable interactions instead of calling Selenium directly.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Click only when the element is clickable; otherwise translate the
     * Selenium failure into a domain-meaningful
     * {@link ElementNotClickableException}.
     */
    protected void clickWhenClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (TimeoutException | ElementNotInteractableException e) {
            throw new ElementNotClickableException("Failed to click: " + locator, e);
        }
    }

    protected void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public String pageSource() { return driver.getPageSource(); }
}
