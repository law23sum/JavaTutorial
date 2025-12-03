package com.apps.demo;

import java.util.Objects;

public final class DemoExample {
    private final String id;
    private final String name;
    private final String pseudoCode;
    private final DemoAction action;

    public DemoExample(String id, String name, String pseudoCode, DemoAction action) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.pseudoCode = Objects.requireNonNull(pseudoCode, "pseudoCode");
        this.action = Objects.requireNonNull(action, "action");
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String pseudoCode() {
        return pseudoCode;
    }

    public String runDemo() {
        return DemoOutputCapture.runAndCapture(action);
    }

    @Override
    public String toString() {
        return name;
    }
}
