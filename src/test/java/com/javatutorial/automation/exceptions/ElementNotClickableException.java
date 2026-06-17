package com.javatutorial.automation.exceptions;

/**
 * Thrown when an element exists in the DOM but is not interactable within
 * the configured timeout (covered by overlay, disabled, animating, etc.).
 */
public class ElementNotClickableException extends RuntimeException {
    public ElementNotClickableException(String message) { super(message); }
    public ElementNotClickableException(String message, Throwable cause) { super(message, cause); }
}
