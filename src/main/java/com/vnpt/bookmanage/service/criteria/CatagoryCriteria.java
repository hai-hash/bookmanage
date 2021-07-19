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
 * Criteria class for the {@link com.vnpt.bookmanage.domain.Catagory} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.CatagoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /catagories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CatagoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter modifiedDate;

    private BooleanFilter isActive;

    private LongFilter userId;

    private LongFilter catalogId;

    private LongFilter bookId;

    public CatagoryCriteria() {}

    public CatagoryCriteria(CatagoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.catalogId = other.catalogId == null ? null : other.catalogId.copy();
        this.bookId = other.bookId == null ? null : other.bookId.copy();
    }

    @Override
    public CatagoryCriteria copy() {
        return new CatagoryCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public LongFilter getCatalogId() {
        return catalogId;
    }

    public LongFilter catalogId() {
        if (catalogId == null) {
            catalogId = new LongFilter();
        }
        return catalogId;
    }

    public void setCatalogId(LongFilter catalogId) {
        this.catalogId = catalogId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CatagoryCriteria that = (CatagoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(catalogId, that.catalogId) &&
            Objects.equals(bookId, that.bookId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modifiedDate, isActive, userId, catalogId, bookId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatagoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (catalogId != null ? "catalogId=" + catalogId + ", " : "") +
            (bookId != null ? "bookId=" + bookId + ", " : "") +
            "}";
    }
}
