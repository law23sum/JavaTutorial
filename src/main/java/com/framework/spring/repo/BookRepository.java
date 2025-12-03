package com.framework.spring.repo;

import com.framework.spring.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
