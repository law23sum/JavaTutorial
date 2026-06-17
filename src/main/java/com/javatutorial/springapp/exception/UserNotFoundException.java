package com.javatutorial.springapp.exception;

/** Thrown by {@link com.javatutorial.springapp.service.UserService} when an id is not found. */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) { super("User not found: " + id); }
}
