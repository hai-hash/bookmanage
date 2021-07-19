package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnpt.bookmanage.domain.enumeration.ReservationStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A BookReservation.
 */
@Entity
@Table(name = "book_res")
public class BookReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @JsonIgnoreProperties(value = { "user", "rack", "book", "bookReservation" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private BookItem bookItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "bookReservations" }, allowSetters = true)
    private Reader reader;

    @JsonIgnoreProperties(value = { "bookReservation", "bookLending" }, allowSetters = true)
    @OneToOne(mappedBy = "bookReservation")
    private BookLendingDetails bookLendingDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookReservation id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public BookReservation creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public ReservationStatus getStatus() {
        return this.status;
    }

    public BookReservation status(ReservationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public BookItem getBookItem() {
        return this.bookItem;
    }

    public BookReservation bookItem(BookItem bookItem) {
        this.setBookItem(bookItem);
        return this;
    }

    public void setBookItem(BookItem bookItem) {
        this.bookItem = bookItem;
    }

    public Reader getReader() {
        return this.reader;
    }

    public BookReservation reader(Reader reader) {
        this.setReader(reader);
        return this;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public BookLendingDetails getBookLendingDetails() {
        return this.bookLendingDetails;
    }

    public BookReservation bookLendingDetails(BookLendingDetails bookLendingDetails) {
        this.setBookLendingDetails(bookLendingDetails);
        return this;
    }

    public void setBookLendingDetails(BookLendingDetails bookLendingDetails) {
        if (this.bookLendingDetails != null) {
            this.bookLendingDetails.setBookReservation(null);
        }
        if (bookLendingDetails != null) {
            bookLendingDetails.setBookReservation(this);
        }
        this.bookLendingDetails = bookLendingDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookReservation)) {
            return false;
        }
        return id != null && id.equals(((BookReservation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookReservation{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
