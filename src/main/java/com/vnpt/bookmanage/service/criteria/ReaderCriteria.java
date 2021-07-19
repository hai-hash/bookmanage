package com.vnpt.bookmanage.service.criteria;

import com.vnpt.bookmanage.domain.enumeration.AccountStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.vnpt.bookmanage.domain.Reader} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.ReaderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /readers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReaderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AccountStatus
     */
    public static class AccountStatusFilter extends Filter<AccountStatus> {

        public AccountStatusFilter() {}

        public AccountStatusFilter(AccountStatusFilter filter) {
            super(filter);
        }

        @Override
        public AccountStatusFilter copy() {
            return new AccountStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter phone;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zipCode;

    private StringFilter country;

    private AccountStatusFilter status;

    private LocalDateFilter modifiedDate;

    private LongFilter userId;

    private LongFilter bookReservationId;

    public ReaderCriteria() {}

    public ReaderCriteria(ReaderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zipCode = other.zipCode == null ? null : other.zipCode.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.bookReservationId = other.bookReservationId == null ? null : other.bookReservationId.copy();
    }

    @Override
    public ReaderCriteria copy() {
        return new ReaderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public StringFilter streetAddress() {
        if (streetAddress == null) {
            streetAddress = new StringFilter();
        }
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public StringFilter zipCode() {
        if (zipCode == null) {
            zipCode = new StringFilter();
        }
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public AccountStatusFilter getStatus() {
        return status;
    }

    public AccountStatusFilter status() {
        if (status == null) {
            status = new AccountStatusFilter();
        }
        return status;
    }

    public void setStatus(AccountStatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getModifiedDate() {
        return modifiedDate;
    }

    public LocalDateFilter modifiedDate() {
        if (modifiedDate == null) {
            modifiedDate = new LocalDateFilter();
        }
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getBookReservationId() {
        return bookReservationId;
    }

    public LongFilter bookReservationId() {
        if (bookReservationId == null) {
            bookReservationId = new LongFilter();
        }
        return bookReservationId;
    }

    public void setBookReservationId(LongFilter bookReservationId) {
        this.bookReservationId = bookReservationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReaderCriteria that = (ReaderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(country, that.country) &&
            Objects.equals(status, that.status) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(bookReservationId, that.bookReservationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, streetAddress, city, state, zipCode, country, status, modifiedDate, userId, bookReservationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReaderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (zipCode != null ? "zipCode=" + zipCode + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (bookReservationId != null ? "bookReservationId=" + bookReservationId + ", " : "") +
            "}";
    }
}
