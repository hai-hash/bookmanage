package com.vnpt.bookmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Rack.
 */
@Entity
@Table(name = "rack")
public class Rack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "location_identifier")
    private String locationIdentifier;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private LocalDate modifiedDate;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "rack")
    @JsonIgnoreProperties(value = { "user", "rack", "book", "bookReservation" }, allowSetters = true)
    private Set<BookItem> bookItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rack id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getNumber() {
        return this.number;
    }

    public Rack number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLocationIdentifier() {
        return this.locationIdentifier;
    }

    public Rack locationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
        return this;
    }

    public void setLocationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public Rack modifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Rack isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User getUser() {
        return this.user;
    }

    public Rack user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<BookItem> getBookItems() {
        return this.bookItems;
    }

    public Rack bookItems(Set<BookItem> bookItems) {
        this.setBookItems(bookItems);
        return this;
    }

    public Rack addBookItem(BookItem bookItem) {
        this.bookItems.add(bookItem);
        bookItem.setRack(this);
        return this;
    }

    public Rack removeBookItem(BookItem bookItem) {
        this.bookItems.remove(bookItem);
        bookItem.setRack(null);
        return this;
    }

    public void setBookItems(Set<BookItem> bookItems) {
        if (this.bookItems != null) {
            this.bookItems.forEach(i -> i.setRack(null));
        }
        if (bookItems != null) {
            bookItems.forEach(i -> i.setRack(this));
        }
        this.bookItems = bookItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rack)) {
            return false;
        }
        return id != null && id.equals(((Rack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rack{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            ", locationIdentifier='" + getLocationIdentifier() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
