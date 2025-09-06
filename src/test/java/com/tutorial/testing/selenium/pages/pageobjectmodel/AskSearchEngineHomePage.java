package com.tutorial.testing.selenium.pages.pageobjectmodel;

import com.tutorial.testing.selenium.pages.common.BasePage;
import com.tutorial.testing.selenium.pages.common.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Ask landing page
 */
public class AskSearchEngineHomePage extends BasePage implements Page.Loadable, Page.Verifiable {
    private static final String URL = "https://www.ask.com";

    // Component encapsulating search box behavior and readiness
    private final SearchBox searchBox = new SearchBox();

    public AskSearchEngineHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void load() {
        driver.get(URL);
        searchBox.render();
    }

    @Override
    public boolean isAt() {
        return getTitle().toLowerCase().contains("ask") && searchBox.isReady();
    }

    /** Perform a search and land on results */
    public AskSearchEngineResultsPage search(String query) {
        return searchBox.search(query);
    }

    /**
     * SearchBox is a reusable UI component that renders before interactions.
     * Implements Page.Component to make the contract explicit and testable.
     */
    private class SearchBox implements Page.Component {
        private static final By SEARCH_BOX = By.xpath("//*[@id='page']/header/div/div/div/div[4]/div/form/input");

        @Override
        public void render() {
            waitUntil(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX));
        }

        boolean isReady() {
            return !driver.findElements(SEARCH_BOX).isEmpty();
        }

        AskSearchEngineResultsPage search(String query) {
            WebElement box = waitUntil(ExpectedConditions.elementToBeClickable(SEARCH_BOX));

            // First click into the box
            click(box);

            // Then type the query
            type(box, query);

            // Finally hit Enter
            box.sendKeys(Keys.ENTER);

            return new AskSearchEngineResultsPage(driver);
        }
    }
}
