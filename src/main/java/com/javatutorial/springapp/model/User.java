package com.javatutorial.springapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * JPA entity for the demo Spring Boot app. Persisted to in-memory H2.
 *
 * <p>Showcases:
 * <ul>
 *   <li>{@code @Entity} mapping a class to a table</li>
 *   <li>{@code @Id} + auto-generated PK</li>
 *   <li>Bean validation with {@code @NotBlank}, {@code @Email}</li>
 * </ul>
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    protected User() {} // JPA

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long   getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }

    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}
