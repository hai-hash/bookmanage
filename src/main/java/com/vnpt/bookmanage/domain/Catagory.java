package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Catagory.
 */
@Entity
@Table(name = "catagory")
public class Catagory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDate modifiedDate;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "catalog", "books" }, allowSetters = true)
    private Catagory catalog;

    @OneToMany(mappedBy = "catagory")
    @JsonIgnoreProperties(value = { "user", "catagory", "publisher", "authors", "bookItems" }, allowSetters = true)
    private Set<Book> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Catagory id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Catagory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public Catagory modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Catagory isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User getUser() {
        return this.user;
    }

    public Catagory user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Catagory getCatalog() {
        return this.catalog;
    }

    public Catagory catalog(Catagory catagory) {
        this.setCatalog(catagory);
        return this;
    }

    public void setCatalog(Catagory catagory) {
        this.catalog = catagory;
    }

    public Set<Book> getBooks() {
        return this.books;
    }

    public Catagory books(Set<Book> books) {
        this.setBooks(books);
        return this;
    }

    public Catagory addBook(Book book) {
        this.books.add(book);
        book.setCatagory(this);
        return this;
    }

    public Catagory removeBook(Book book) {
        this.books.remove(book);
        book.setCatagory(null);
        return this;
    }

    public void setBooks(Set<Book> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.setCatagory(null));
        }
        if (books != null) {
            books.forEach(i -> i.setCatagory(this));
        }
        this.books = books;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catagory)) {
            return false;
        }
        return id != null && id.equals(((Catagory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catagory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
