package com.vnpt.bookmanage.service.criteria;

import com.vnpt.bookmanage.domain.enumeration.LendingStatus;
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
 * Criteria class for the {@link com.vnpt.bookmanage.domain.BookLending} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.BookLendingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /book-lendings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookLendingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LendingStatus
     */
    public static class LendingStatusFilter extends Filter<LendingStatus> {

        public LendingStatusFilter() {}

        public LendingStatusFilter(LendingStatusFilter filter) {
            super(filter);
        }

        @Override
        public LendingStatusFilter copy() {
            return new LendingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter creationDate;

    private LendingStatusFilter status;

    private StringFilter description;

    private LongFilter userId;

    private LongFilter bookLendingDetailsId;

    public BookLendingCriteria() {}

    public BookLendingCriteria(BookLendingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.bookLendingDetailsId = other.bookLendingDetailsId == null ? null : other.bookLendingDetailsId.copy();
    }

    @Override
    public BookLendingCriteria copy() {
        return new BookLendingCriteria(this);
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

    public LendingStatusFilter getStatus() {
        return status;
    }

    public LendingStatusFilter status() {
        if (status == null) {
            status = new LendingStatusFilter();
        }
        return status;
    }

    public void setStatus(LendingStatusFilter status) {
        this.status = status;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        final BookLendingCriteria that = (BookLendingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(description, that.description) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(bookLendingDetailsId, that.bookLendingDetailsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, status, description, userId, bookLendingDetailsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookLendingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (bookLendingDetailsId != null ? "bookLendingDetailsId=" + bookLendingDetailsId + ", " : "") +
            "}";
    }
}
