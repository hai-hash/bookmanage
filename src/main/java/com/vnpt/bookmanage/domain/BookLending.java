package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnpt.bookmanage.domain.enumeration.LendingStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A BookLending.
 */
@Entity
@Table(name = "book_len")
public class BookLending implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LendingStatus status;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "bookLending")
    @JsonIgnoreProperties(value = { "bookReservation", "bookLending" }, allowSetters = true)
    private Set<BookLendingDetails> bookLendingDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookLending id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public BookLending creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LendingStatus getStatus() {
        return this.status;
    }

    public BookLending status(LendingStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public BookLending description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return this.user;
    }

    public BookLending user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<BookLendingDetails> getBookLendingDetails() {
        return this.bookLendingDetails;
    }

    public BookLending bookLendingDetails(Set<BookLendingDetails> bookLendingDetails) {
        this.setBookLendingDetails(bookLendingDetails);
        return this;
    }

    public BookLending addBookLendingDetails(BookLendingDetails bookLendingDetails) {
        this.bookLendingDetails.add(bookLendingDetails);
        bookLendingDetails.setBookLending(this);
        return this;
    }

    public BookLending removeBookLendingDetails(BookLendingDetails bookLendingDetails) {
        this.bookLendingDetails.remove(bookLendingDetails);
        bookLendingDetails.setBookLending(null);
        return this;
    }

    public void setBookLendingDetails(Set<BookLendingDetails> bookLendingDetails) {
        if (this.bookLendingDetails != null) {
            this.bookLendingDetails.forEach(i -> i.setBookLending(null));
        }
        if (bookLendingDetails != null) {
            bookLendingDetails.forEach(i -> i.setBookLending(this));
        }
        this.bookLendingDetails = bookLendingDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookLending)) {
            return false;
        }
        return id != null && id.equals(((BookLending) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookLending{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
