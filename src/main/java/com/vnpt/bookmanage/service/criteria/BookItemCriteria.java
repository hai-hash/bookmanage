package com.vnpt.bookmanage.service.criteria;

import com.vnpt.bookmanage.domain.enumeration.BookFormat;
import com.vnpt.bookmanage.domain.enumeration.BookStatus;
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
 * Criteria class for the {@link com.vnpt.bookmanage.domain.BookItem} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.BookItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /book-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookItemCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BookFormat
     */
    public static class BookFormatFilter extends Filter<BookFormat> {

        public BookFormatFilter() {}

        public BookFormatFilter(BookFormatFilter filter) {
            super(filter);
        }

        @Override
        public BookFormatFilter copy() {
            return new BookFormatFilter(this);
        }
    }

    /**
     * Class for filtering BookStatus
     */
    public static class BookStatusFilter extends Filter<BookStatus> {

        public BookStatusFilter() {}

        public BookStatusFilter(BookStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookStatusFilter copy() {
            return new BookStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private BooleanFilter isReferenceOnly;

    private LocalDateFilter borrowed;

    private LocalDateFilter dueDate;

    private BigDecimalFilter price;

    private BookFormatFilter format;

    private BookStatusFilter status;

    private LocalDateFilter dateOfPurchase;

    private LocalDateFilter publicationDate;

    private LocalDateFilter modifiedDate;

    private LongFilter userId;

    private LongFilter rackId;

    private LongFilter bookId;

    private LongFilter bookReservationId;

    public BookItemCriteria() {}

    public BookItemCriteria(BookItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.barcode = other.barcode == null ? null : other.barcode.copy();
        this.isReferenceOnly = other.isReferenceOnly == null ? null : other.isReferenceOnly.copy();
        this.borrowed = other.borrowed == null ? null : other.borrowed.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.format = other.format == null ? null : other.format.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dateOfPurchase = other.dateOfPurchase == null ? null : other.dateOfPurchase.copy();
        this.publicationDate = other.publicationDate == null ? null : other.publicationDate.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.rackId = other.rackId == null ? null : other.rackId.copy();
        this.bookId = other.bookId == null ? null : other.bookId.copy();
        this.bookReservationId = other.bookReservationId == null ? null : other.bookReservationId.copy();
    }

    @Override
    public BookItemCriteria copy() {
        return new BookItemCriteria(this);
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

    public StringFilter getBarcode() {
        return barcode;
    }

    public StringFilter barcode() {
        if (barcode == null) {
            barcode = new StringFilter();
        }
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    public BooleanFilter getIsReferenceOnly() {
        return isReferenceOnly;
    }

    public BooleanFilter isReferenceOnly() {
        if (isReferenceOnly == null) {
            isReferenceOnly = new BooleanFilter();
        }
        return isReferenceOnly;
    }

    public void setIsReferenceOnly(BooleanFilter isReferenceOnly) {
        this.isReferenceOnly = isReferenceOnly;
    }

    public LocalDateFilter getBorrowed() {
        return borrowed;
    }

    public LocalDateFilter borrowed() {
        if (borrowed == null) {
            borrowed = new LocalDateFilter();
        }
        return borrowed;
    }

    public void setBorrowed(LocalDateFilter borrowed) {
        this.borrowed = borrowed;
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

    public BookFormatFilter getFormat() {
        return format;
    }

    public BookFormatFilter format() {
        if (format == null) {
            format = new BookFormatFilter();
        }
        return format;
    }

    public void setFormat(BookFormatFilter format) {
        this.format = format;
    }

    public BookStatusFilter getStatus() {
        return status;
    }

    public BookStatusFilter status() {
        if (status == null) {
            status = new BookStatusFilter();
        }
        return status;
    }

    public void setStatus(BookStatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getDateOfPurchase() {
        return dateOfPurchase;
    }

    public LocalDateFilter dateOfPurchase() {
        if (dateOfPurchase == null) {
            dateOfPurchase = new LocalDateFilter();
        }
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDateFilter dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public LocalDateFilter getPublicationDate() {
        return publicationDate;
    }

    public LocalDateFilter publicationDate() {
        if (publicationDate == null) {
            publicationDate = new LocalDateFilter();
        }
        return publicationDate;
    }

    public void setPublicationDate(LocalDateFilter publicationDate) {
        this.publicationDate = publicationDate;
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

    public LongFilter getRackId() {
        return rackId;
    }

    public LongFilter rackId() {
        if (rackId == null) {
            rackId = new LongFilter();
        }
        return rackId;
    }

    public void setRackId(LongFilter rackId) {
        this.rackId = rackId;
    }

    public LongFilter getBookId() {
        return bookId;
    }

    public LongFilter bookId() {
        if (bookId == null) {
            bookId = new LongFilter();
        }
        return bookId;
    }

    public void setBookId(LongFilter bookId) {
        this.bookId = bookId;
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
        final BookItemCriteria that = (BookItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(isReferenceOnly, that.isReferenceOnly) &&
            Objects.equals(borrowed, that.borrowed) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(format, that.format) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dateOfPurchase, that.dateOfPurchase) &&
            Objects.equals(publicationDate, that.publicationDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(rackId, that.rackId) &&
            Objects.equals(bookId, that.bookId) &&
            Objects.equals(bookReservationId, that.bookReservationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            barcode,
            isReferenceOnly,
            borrowed,
            dueDate,
            price,
            format,
            status,
            dateOfPurchase,
            publicationDate,
            modifiedDate,
            userId,
            rackId,
            bookId,
            bookReservationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (barcode != null ? "barcode=" + barcode + ", " : "") +
            (isReferenceOnly != null ? "isReferenceOnly=" + isReferenceOnly + ", " : "") +
            (borrowed != null ? "borrowed=" + borrowed + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (format != null ? "format=" + format + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dateOfPurchase != null ? "dateOfPurchase=" + dateOfPurchase + ", " : "") +
            (publicationDate != null ? "publicationDate=" + publicationDate + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (rackId != null ? "rackId=" + rackId + ", " : "") +
            (bookId != null ? "bookId=" + bookId + ", " : "") +
            (bookReservationId != null ? "bookReservationId=" + bookReservationId + ", " : "") +
            "}";
    }
}
