package com.vnpt.bookmanage.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.vnpt.bookmanage.domain.BookLendingDetails} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.BookLendingDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /book-lending-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookLendingDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dueDate;

    private LocalDateFilter returnDate;

    private BigDecimalFilter price;

    private LongFilter bookReservationId;

    private LongFilter bookLendingId;

    public BookLendingDetailsCriteria() {}

    public BookLendingDetailsCriteria(BookLendingDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.returnDate = other.returnDate == null ? null : other.returnDate.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.bookReservationId = other.bookReservationId == null ? null : other.bookReservationId.copy();
        this.bookLendingId = other.bookLendingId == null ? null : other.bookLendingId.copy();
    }

    @Override
    public BookLendingDetailsCriteria copy() {
        return new BookLendingDetailsCriteria(this);
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

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public LocalDateFilter dueDate() {
        if (dueDate == null) {
            dueDate = new LocalDateFilter();
        }
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateFilter getReturnDate() {
        return returnDate;
    }

    public LocalDateFilter returnDate() {
        if (returnDate == null) {
            returnDate = new LocalDateFilter();
        }
        return returnDate;
    }

    public void setReturnDate(LocalDateFilter returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
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

    public LongFilter getBookLendingId() {
        return bookLendingId;
    }

    public LongFilter bookLendingId() {
        if (bookLendingId == null) {
            bookLendingId = new LongFilter();
        }
        return bookLendingId;
    }

    public void setBookLendingId(LongFilter bookLendingId) {
        this.bookLendingId = bookLendingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookLendingDetailsCriteria that = (BookLendingDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(returnDate, that.returnDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(bookReservationId, that.bookReservationId) &&
            Objects.equals(bookLendingId, that.bookLendingId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dueDate, returnDate, price, bookReservationId, bookLendingId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookLendingDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (returnDate != null ? "returnDate=" + returnDate + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (bookReservationId != null ? "bookReservationId=" + bookReservationId + ", " : "") +
            (bookLendingId != null ? "bookLendingId=" + bookLendingId + ", " : "") +
            "}";
    }
}
