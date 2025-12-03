package com.framework.spring.controller;

import com.framework.spring.model.Book;
import com.framework.spring.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> all() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody BookRequest request) {
        return bookService.create(request.toBook());
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request.toBook());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    public record BookRequest(
            @NotBlank String title,
            @NotBlank String author,
            @NotBlank @Pattern(regexp = "[0-9\\-]{10,17}", message = "ISBN must be digits/dashes") String isbn,
            @NotNull @Positive BigDecimal price) {
        Book toBook() {
            return new Book(title, author, isbn, price);
        }
    }
}
