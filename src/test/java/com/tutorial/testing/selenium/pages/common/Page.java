package com.tutorial.testing.selenium.pages.common;

/**
 * Core page contracts for the Selenium tutorial.
 *
 * Keep Page itself as a marker, then compose behavior via the nested contracts below.
 * This avoids duplicate method names and keeps responsibilities clear.
 */
public interface Page {

    /** Pages/components that can return a title. */
    interface HasTitle {
        String getTitle();
    }

    /** Pages that can verify they've loaded/are at the expected state. */
    interface Verifiable {
        boolean isAt();
    }

    /** Pages/components that require an explicit load step (e.g., navigate or open). */
    interface Loadable {
        void load();
    }

    /** Reusable UI components that must render before use. */
    interface Component {
        void render();
    }
}
