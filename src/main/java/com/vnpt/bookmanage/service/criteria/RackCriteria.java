package com.vnpt.bookmanage.service.criteria;

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
 * Criteria class for the {@link com.vnpt.bookmanage.domain.Rack} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.RackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /racks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RackCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter number;

    private StringFilter locationIdentifier;

    private LocalDateFilter modifiedDate;

    private BooleanFilter isActive;

    private LongFilter userId;

    private LongFilter bookItemId;

    public RackCriteria() {}

    public RackCriteria(RackCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.locationIdentifier = other.locationIdentifier == null ? null : other.locationIdentifier.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.bookItemId = other.bookItemId == null ? null : other.bookItemId.copy();
    }

    @Override
    public RackCriteria copy() {
        return new RackCriteria(this);
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

    public IntegerFilter getNumber() {
        return number;
    }

    public IntegerFilter number() {
        if (number == null) {
            number = new IntegerFilter();
        }
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public StringFilter getLocationIdentifier() {
        return locationIdentifier;
    }

    public StringFilter locationIdentifier() {
        if (locationIdentifier == null) {
            locationIdentifier = new StringFilter();
        }
        return locationIdentifier;
    }

    public void setLocationIdentifier(StringFilter locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RackCriteria that = (RackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(locationIdentifier, that.locationIdentifier) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(bookItemId, that.bookItemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, locationIdentifier, modifiedDate, isActive, userId, bookItemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RackCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (locationIdentifier != null ? "locationIdentifier=" + locationIdentifier + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (bookItemId != null ? "bookItemId=" + bookItemId + ", " : "") +
            "}";
    }
}
