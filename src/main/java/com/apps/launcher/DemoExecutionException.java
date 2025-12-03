package com.apps.launcher;

/**
 * Runtime exception thrown when the selected demo cannot be invoked.
 */
public final class DemoExecutionException extends RuntimeException {
    DemoExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
