package com.framework.spring.controller;

import com.apps.demo.DemoRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    private final DemoRegistry registry;

    public HelloController(DemoRegistry registry) {
        this.registry = registry;
    }

    @GetMapping
    public Map<String, Object> hello() {
        return Map.of(
                "message", "Spring Boot app is running",
                "demoCount", registry.demos().size());
    }
}
