package com.javatutorial.springapp.repository;

import com.javatutorial.springapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA gives us CRUD methods for free. */
public interface UserRepository extends JpaRepository<User, Long> {
}
