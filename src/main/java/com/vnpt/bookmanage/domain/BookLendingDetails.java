package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A BookLendingDetails.
 */
@Entity
@Table(name = "book_len_dtl")
public class BookLendingDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @JsonIgnoreProperties(value = { "bookItem", "reader", "bookLendingDetails" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private BookReservation bookReservation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "bookLendingDetails" }, allowSetters = true)
    private BookLending bookLending;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookLendingDetails id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public BookLendingDetails dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public BookLendingDetails returnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BookLendingDetails price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookReservation getBookReservation() {
        return this.bookReservation;
    }

    public BookLendingDetails bookReservation(BookReservation bookReservation) {
        this.setBookReservation(bookReservation);
        return this;
    }

    public void setBookReservation(BookReservation bookReservation) {
        this.bookReservation = bookReservation;
    }

    public BookLending getBookLending() {
        return this.bookLending;
    }

    public BookLendingDetails bookLending(BookLending bookLending) {
        this.setBookLending(bookLending);
        return this;
    }

    public void setBookLending(BookLending bookLending) {
        this.bookLending = bookLending;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookLendingDetails)) {
            return false;
        }
        return id != null && id.equals(((BookLendingDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookLendingDetails{" +
            "id=" + getId() +
            ", dueDate='" + getDueDate() + "'" +
            ", returnDate='" + getReturnDate() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
