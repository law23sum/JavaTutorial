package com.framework.spring.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Simple JPA entity representing a book in the sample catalog.
 */
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    protected Book() {
        // JPA only
    }

    public Book(String title, String author, String isbn, BigDecimal price) {
        this.title = Objects.requireNonNull(title, "title");
        this.author = Objects.requireNonNull(author, "author");
        this.isbn = Objects.requireNonNull(isbn, "isbn");
        this.price = Objects.requireNonNull(price, "price");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void copyFrom(Book source) {
        setTitle(source.getTitle());
        setAuthor(source.getAuthor());
        setIsbn(source.getIsbn());
        setPrice(source.getPrice());
    }
}
