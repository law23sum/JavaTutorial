package com.javatutorial.springapp.config;

import com.javatutorial.springapp.model.User;
import com.javatutorial.springapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Seeds a couple of demo users at startup so the API has something to return. */
@Configuration
public class AppConfig {

    @Bean
    CommandLineRunner seed(UserRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new User("Ada Lovelace",  "ada@example.com"));
                repo.save(new User("Alan Turing",   "alan@example.com"));
            }
        };
    }
}
