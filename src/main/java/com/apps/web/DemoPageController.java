package com.apps.web;

import com.apps.demo.DemoRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoPageController {

    private final DemoRegistry registry;

    public DemoPageController(DemoRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("demoCount", registry.demos().size());
        return "demo";
    }
}
