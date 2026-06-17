package com.javatutorial.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * <h2>LoginPage</h2>
 * Classic Page Object: locators live here, tests stay clean. Methods describe
 * <i>user intent</i> ("login as X"), not low-level WebDriver mechanics.
 */
public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By SUBMIT   = By.id("submit");

    public LoginPage(WebDriver driver) { super(driver); }

    public void loginAs(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        clickWhenClickable(SUBMIT);
    }
}
