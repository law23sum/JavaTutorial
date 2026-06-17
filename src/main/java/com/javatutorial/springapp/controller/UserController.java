package com.javatutorial.springapp.controller;

import com.javatutorial.springapp.model.User;
import com.javatutorial.springapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Tiny REST resource exposing CRUD over {@link User}s.
 *
 * <pre>
 *   GET    /api/users        list
 *   GET    /api/users/{id}   one
 *   POST   /api/users        create  (validated body)
 *   DELETE /api/users/{id}   delete
 * </pre>
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping
    public List<User> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public User one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User saved = service.create(user);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
