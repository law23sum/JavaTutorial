package com.tutorial.selenium.pages.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage provides shared WebDriver/WebDriverWait and common UI helpers.
 * It implements HasTitle because it can always read the browser title.
 * Concrete pages opt-in to Verifiable/Loadable if they need those behaviors.
 */
public abstract class BasePage implements Page, Page.HasTitle {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    /** Click once the element is clickable. */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /** Type once the element is visible (clears first by default). */
    protected void type(WebElement element, String text) {
        WebElement visible = wait.until(ExpectedConditions.visibilityOf(element));
        visible.clear();
        visible.sendKeys(text);
    }

    /** Generic wait helper for custom conditions. */
    protected <T> T waitUntil(ExpectedCondition<T> condition) {
        return wait.until(condition);
    }
}
