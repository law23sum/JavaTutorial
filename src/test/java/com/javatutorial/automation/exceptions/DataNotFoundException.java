package com.javatutorial.automation.exceptions;

/**
 * <h2>Custom Exception: {@code DataNotFoundException}</h2>
 * Wraps lower-level data-access failures (e.g. {@code SQLException},
 * {@code IOException}) into a single domain-meaningful unchecked exception
 * that callers can handle without leaking infrastructure types.
 */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) { super(message); }
    public DataNotFoundException(String message, Throwable cause) { super(message, cause); }
}
