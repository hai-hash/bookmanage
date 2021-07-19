package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "isbn", nullable = false)
    private String isbn;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @NotNull
    @Size(max = 4000)
    @Column(name = "subject", length = 4000, nullable = false)
    private String subject;

    @Size(max = 4000)
    @Column(name = "target", length = 4000)
    private String target;

    @NotNull
    @Column(name = "language", nullable = false)
    private String language;

    @NotNull
    @Column(name = "number_of_pages", nullable = false)
    private Integer numberOfPages;

    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDate modifiedDate;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "catalog", "books" }, allowSetters = true)
    private Catagory catagory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "books" }, allowSetters = true)
    private Publisher publisher;

    @ManyToMany
    @JoinTable(name = "rel_book__author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonIgnoreProperties(value = { "user", "books" }, allowSetters = true)
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties(value = { "user", "rack", "book", "bookReservation" }, allowSetters = true)
    private Set<BookItem> bookItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book id(Long id) {
        this.id = id;
        return this;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public Book isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return this.title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return this.subject;
    }

    public Book subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTarget() {
        return this.target;
    }

    public Book target(String target) {
        this.target = target;
        return this;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLanguage() {
        return this.language;
    }

    public Book language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getNumberOfPages() {
        return this.numberOfPages;
    }

    public Book numberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
        return this;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Book imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public Book modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getUser() {
        return this.user;
    }

    public Book user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Catagory getCatagory() {
        return this.catagory;
    }

    public Book catagory(Catagory catagory) {
        this.setCatagory(catagory);
        return this;
    }

    public void setCatagory(Catagory catagory) {
        this.catagory = catagory;
    }

    public Publisher getPublisher() {
        return this.publisher;
    }

    public Book publisher(Publisher publisher) {
        this.setPublisher(publisher);
        return this;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public Book authors(Set<Author> authors) {
        this.setAuthors(authors);
        return this;
    }

    public Book addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
        return this;
    }

    public Book removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
        return this;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<BookItem> getBookItems() {
        return this.bookItems;
    }

    public Book bookItems(Set<BookItem> bookItems) {
        this.setBookItems(bookItems);
        return this;
    }

    public Book addBookItem(BookItem bookItem) {
        this.bookItems.add(bookItem);
        bookItem.setBook(this);
        return this;
    }

    public Book removeBookItem(BookItem bookItem) {
        this.bookItems.remove(bookItem);
        bookItem.setBook(null);
        return this;
    }

    public void setBookItems(Set<BookItem> bookItems) {
        if (this.bookItems != null) {
            this.bookItems.forEach(i -> i.setBook(null));
        }
        if (bookItems != null) {
            bookItems.forEach(i -> i.setBook(this));
        }
        this.bookItems = bookItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", isbn='" + getIsbn() + "'" +
            ", title='" + getTitle() + "'" +
            ", subject='" + getSubject() + "'" +
            ", target='" + getTarget() + "'" +
            ", language='" + getLanguage() + "'" +
            ", numberOfPages=" + getNumberOfPages() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
