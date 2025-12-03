package com.apps.demo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility that captures everything written to System.out while the demo executes.
 */
public final class DemoOutputCapture {

    private DemoOutputCapture() {
    }

    public static String runAndCapture(DemoAction action) {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        try {
            System.setOut(capture);
            System.setErr(capture);
            action.execute();
        } catch (Exception e) {
            e.printStackTrace(capture);
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
            capture.flush();
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
