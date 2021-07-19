package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnpt.bookmanage.domain.enumeration.BookFormat;
import com.vnpt.bookmanage.domain.enumeration.BookStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A BookItem.
 */
@Entity
@Table(name = "book_item")
public class BookItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "is_reference_only")
    private Boolean isReferenceOnly;

    @Column(name = "borrowed")
    private LocalDate borrowed;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    private BookFormat format;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;

    @Column(name = "date_of_purchase")
    private LocalDate dateOfPurchase;

    @NotNull
    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDate modifiedDate;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "bookItems" }, allowSetters = true)
    private Rack rack;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "catagory", "publisher", "authors", "bookItems" }, allowSetters = true)
    private Book book;

    @JsonIgnoreProperties(value = { "bookItem", "reader", "bookLendingDetails" }, allowSetters = true)
    @OneToOne(mappedBy = "bookItem")
    private BookReservation bookReservation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public BookItem barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Boolean getIsReferenceOnly() {
        return this.isReferenceOnly;
    }

    public BookItem isReferenceOnly(Boolean isReferenceOnly) {
        this.isReferenceOnly = isReferenceOnly;
        return this;
    }

    public void setIsReferenceOnly(Boolean isReferenceOnly) {
        this.isReferenceOnly = isReferenceOnly;
    }

    public LocalDate getBorrowed() {
        return this.borrowed;
    }

    public BookItem borrowed(LocalDate borrowed) {
        this.borrowed = borrowed;
        return this;
    }

    public void setBorrowed(LocalDate borrowed) {
        this.borrowed = borrowed;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public BookItem dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BookItem price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookFormat getFormat() {
        return this.format;
    }

    public BookItem format(BookFormat format) {
        this.format = format;
        return this;
    }

    public void setFormat(BookFormat format) {
        this.format = format;
    }

    public BookStatus getStatus() {
        return this.status;
    }

    public BookItem status(BookStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDate getDateOfPurchase() {
        return this.dateOfPurchase;
    }

    public BookItem dateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
        return this;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public LocalDate getPublicationDate() {
        return this.publicationDate;
    }

    public BookItem publicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public BookItem modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getUser() {
        return this.user;
    }

    public BookItem user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rack getRack() {
        return this.rack;
    }

    public BookItem rack(Rack rack) {
        this.setRack(rack);
        return this;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public Book getBook() {
        return this.book;
    }

    public BookItem book(Book book) {
        this.setBook(book);
        return this;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookReservation getBookReservation() {
        return this.bookReservation;
    }

    public BookItem bookReservation(BookReservation bookReservation) {
        this.setBookReservation(bookReservation);
        return this;
    }

    public void setBookReservation(BookReservation bookReservation) {
        if (this.bookReservation != null) {
            this.bookReservation.setBookItem(null);
        }
        if (bookReservation != null) {
            bookReservation.setBookItem(this);
        }
        this.bookReservation = bookReservation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookItem)) {
            return false;
        }
        return id != null && id.equals(((BookItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookItem{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", isReferenceOnly='" + getIsReferenceOnly() + "'" +
            ", borrowed='" + getBorrowed() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", price=" + getPrice() +
            ", format='" + getFormat() + "'" +
            ", status='" + getStatus() + "'" +
            ", dateOfPurchase='" + getDateOfPurchase() + "'" +
            ", publicationDate='" + getPublicationDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
