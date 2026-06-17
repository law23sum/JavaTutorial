package com.javatutorial.springapp.service;

import com.javatutorial.springapp.exception.UserNotFoundException;
import com.javatutorial.springapp.model.User;
import com.javatutorial.springapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-layer bean — the place for business logic. Controllers should
 * stay thin and delegate to services.
 */
@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

    public List<User> findAll()        { return repo.findAll(); }

    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User create(User user) { return repo.save(user); }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new UserNotFoundException(id);
        repo.deleteById(id);
    }
}
