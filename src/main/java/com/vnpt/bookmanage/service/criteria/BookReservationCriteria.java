package com.vnpt.bookmanage.service.criteria;

import com.vnpt.bookmanage.domain.enumeration.ReservationStatus;
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
 * Criteria class for the {@link com.vnpt.bookmanage.domain.BookReservation} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.BookReservationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /book-reservations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookReservationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReservationStatus
     */
    public static class ReservationStatusFilter extends Filter<ReservationStatus> {

        public ReservationStatusFilter() {}

        public ReservationStatusFilter(ReservationStatusFilter filter) {
            super(filter);
        }

        @Override
        public ReservationStatusFilter copy() {
            return new ReservationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter creationDate;

    private ReservationStatusFilter status;

    private LongFilter bookItemId;

    private LongFilter readerId;

    private LongFilter bookLendingDetailsId;

    public BookReservationCriteria() {}

    public BookReservationCriteria(BookReservationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.bookItemId = other.bookItemId == null ? null : other.bookItemId.copy();
        this.readerId = other.readerId == null ? null : other.readerId.copy();
        this.bookLendingDetailsId = other.bookLendingDetailsId == null ? null : other.bookLendingDetailsId.copy();
    }

    @Override
    public BookReservationCriteria copy() {
        return new BookReservationCriteria(this);
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

    public LocalDateFilter getCreationDate() {
        return creationDate;
    }

    public LocalDateFilter creationDate() {
        if (creationDate == null) {
            creationDate = new LocalDateFilter();
        }
        return creationDate;
    }

    public void setCreationDate(LocalDateFilter creationDate) {
        this.creationDate = creationDate;
    }

    public ReservationStatusFilter getStatus() {
        return status;
    }

    public ReservationStatusFilter status() {
        if (status == null) {
            status = new ReservationStatusFilter();
        }
        return status;
    }

    public void setStatus(ReservationStatusFilter status) {
        this.status = status;
    }

    public LongFilter getBookItemId() {
        return bookItemId;
    }

    public LongFilter bookItemId() {
        if (bookItemId == null) {
            bookItemId = new LongFilter();
        }
        return bookItemId;
    }

    public void setBookItemId(LongFilter bookItemId) {
        this.bookItemId = bookItemId;
    }

    public LongFilter getReaderId() {
        return readerId;
    }

    public LongFilter readerId() {
        if (readerId == null) {
            readerId = new LongFilter();
        }
        return readerId;
    }

    public void setReaderId(LongFilter readerId) {
        this.readerId = readerId;
    }

    public LongFilter getBookLendingDetailsId() {
        return bookLendingDetailsId;
    }

    public LongFilter bookLendingDetailsId() {
        if (bookLendingDetailsId == null) {
            bookLendingDetailsId = new LongFilter();
        }
        return bookLendingDetailsId;
    }

    public void setBookLendingDetailsId(LongFilter bookLendingDetailsId) {
        this.bookLendingDetailsId = bookLendingDetailsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookReservationCriteria that = (BookReservationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(bookItemId, that.bookItemId) &&
            Objects.equals(readerId, that.readerId) &&
            Objects.equals(bookLendingDetailsId, that.bookLendingDetailsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, status, bookItemId, readerId, bookLendingDetailsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookReservationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (bookItemId != null ? "bookItemId=" + bookItemId + ", " : "") +
            (readerId != null ? "readerId=" + readerId + ", " : "") +
            (bookLendingDetailsId != null ? "bookLendingDetailsId=" + bookLendingDetailsId + ", " : "") +
            "}";
    }
}
