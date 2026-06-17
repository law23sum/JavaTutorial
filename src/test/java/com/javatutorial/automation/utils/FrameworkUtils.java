package com.javatutorial.automation.utils;

import com.javatutorial.automation.exceptions.DataNotFoundException;
import com.javatutorial.automation.exceptions.ElementNotClickableException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import javax.sql.DataSource;

/**
 * <h2>FrameworkUtils</h2>
 * Cross-cutting helpers the framework reuses: a "click only when clickable"
 * with a friendly exception, and a JDBC user-existence check that maps any
 * SQL failure to {@link DataNotFoundException}.
 */
public class FrameworkUtils {

    private final WebDriver driver;
    private final DataSource dataSource;

    public FrameworkUtils(WebDriver driver, DataSource dataSource) {
        this.driver = driver;
        this.dataSource = dataSource;
    }

    /** Click {@code locator} only when clickable; otherwise throws. */
    public void clickWhenClickable(By locator, Duration timeout) {
        try {
            WebElement el = new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
        } catch (TimeoutException | ElementNotInteractableException e) {
            throw new ElementNotClickableException("Failed to click: " + locator, e);
        }
    }

    /** @throws DataNotFoundException if the user is missing or the DB call fails. */
    public void verifyUserExists(String userId) {
        String sql = "SELECT 1 FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DataNotFoundException("User not found: " + userId);
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("Database error verifying user: " + userId, e);
        }
    }
}
