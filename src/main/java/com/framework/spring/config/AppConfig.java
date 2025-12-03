package com.framework.spring.config;

import com.framework.spring.model.Book;
import com.framework.spring.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    CommandLineRunner demoCatalog(BookService bookService) {
        return args -> {
            if (bookService.hasBooks()) {
                return;
            }
            List<Book> samples = List.of(
                    new Book("Effective Java", "Joshua Bloch", "978-0134685991", new BigDecimal("54.99")),
                    new Book("Designing Data-Intensive Applications", "Martin Kleppmann", "978-1449373320", new BigDecimal("64.99")),
                    new Book("Clean Architecture", "Robert C. Martin", "978-0134494166", new BigDecimal("42.00")));
            bookService.saveAll(samples);
        };
    }
}
