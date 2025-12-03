package com.apps.web;

import com.apps.demo.DemoExample;
import com.apps.demo.DemoRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/api/demos", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoApiController {

    private final DemoRegistry registry;

    public DemoApiController(DemoRegistry registry) {
        this.registry = registry;
    }

    @GetMapping
    public List<DemoResponse> demos() {
        return registry.demos().stream()
                .map(DemoResponse::from)
                .toList();
    }

    @PostMapping(path = "/{id}/run")
    public RunResponse run(@PathVariable String id) {
        String output = registry.run(id);
        return new RunResponse(output);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleMissing(NoSuchElementException ex) {
        return Map.of("error", ex.getMessage());
    }

    public record DemoResponse(String id, String name, String pseudoCode) {
        static DemoResponse from(DemoExample example) {
            return new DemoResponse(example.id(), example.name(), example.pseudoCode());
        }
    }

    public record RunResponse(String output) {}
}
