package com.framework.spring.service;

import com.framework.spring.model.Book;
import com.framework.spring.repo.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BookService {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "title");

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll(DEFAULT_SORT);
    }

    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book %d was not found".formatted(id)));
    }

    public Book create(Book candidate) {
        candidate.setId(null);
        return repository.save(candidate);
    }

    public Book update(Long id, Book candidate) {
        Book existing = findById(id);
        if (candidate.getTitle() != null) {
            existing.setTitle(candidate.getTitle());
        }
        if (candidate.getAuthor() != null) {
            existing.setAuthor(candidate.getAuthor());
        }
        if (candidate.getIsbn() != null) {
            existing.setIsbn(candidate.getIsbn());
        }
        BigDecimal price = candidate.getPrice();
        if (price != null) {
            existing.setPrice(price);
        }
        return repository.save(existing);
    }

    public void delete(Long id) {
        Book existing = findById(id);
        repository.delete(existing);
    }

    public boolean hasBooks() {
        return repository.count() > 0;
    }

    public List<Book> saveAll(Collection<Book> books) {
        return repository.saveAll(books);
    }
}
