package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnpt.bookmanage.domain.enumeration.AccountStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Reader.
 */
@Entity
@Table(name = "reader")
public class Reader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDate modifiedDate;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "reader")
    @JsonIgnoreProperties(value = { "bookItem", "reader", "bookLendingDetails" }, allowSetters = true)
    private Set<BookReservation> bookReservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reader id(Long id) {
        this.id = id;
        return this;
    }

    public String getPhone() {
        return this.phone;
    }

    public Reader phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public Reader streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return this.city;
    }

    public Reader city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public Reader state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public Reader zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return this.country;
    }

    public Reader country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AccountStatus getStatus() {
        return this.status;
    }

    public Reader status(AccountStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public Reader modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getUser() {
        return this.user;
    }

    public Reader user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<BookReservation> getBookReservations() {
        return this.bookReservations;
    }

    public Reader bookReservations(Set<BookReservation> bookReservations) {
        this.setBookReservations(bookReservations);
        return this;
    }

    public Reader addBookReservation(BookReservation bookReservation) {
        this.bookReservations.add(bookReservation);
        bookReservation.setReader(this);
        return this;
    }

    public Reader removeBookReservation(BookReservation bookReservation) {
        this.bookReservations.remove(bookReservation);
        bookReservation.setReader(null);
        return this;
    }

    public void setBookReservations(Set<BookReservation> bookReservations) {
        if (this.bookReservations != null) {
            this.bookReservations.forEach(i -> i.setReader(null));
        }
        if (bookReservations != null) {
            bookReservations.forEach(i -> i.setReader(this));
        }
        this.bookReservations = bookReservations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reader)) {
            return false;
        }
        return id != null && id.equals(((Reader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reader{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", status='" + getStatus() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
