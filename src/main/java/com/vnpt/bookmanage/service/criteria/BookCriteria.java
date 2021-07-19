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
 * Criteria class for the {@link com.vnpt.bookmanage.domain.Book} entity. This class is used
 * in {@link com.vnpt.bookmanage.web.rest.BookResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter isbn;

    private StringFilter title;

    private StringFilter subject;

    private StringFilter target;

    private StringFilter language;

    private IntegerFilter numberOfPages;

    private StringFilter imageUrl;

    private LocalDateFilter modifiedDate;

    private LongFilter userId;

    private LongFilter catagoryId;

    private LongFilter publisherId;

    private LongFilter authorId;

    private LongFilter bookItemId;

    public BookCriteria() {}

    public BookCriteria(BookCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.isbn = other.isbn == null ? null : other.isbn.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.target = other.target == null ? null : other.target.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.numberOfPages = other.numberOfPages == null ? null : other.numberOfPages.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.modifiedDate = other.modifiedDate == null ? null : other.modifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.catagoryId = other.catagoryId == null ? null : other.catagoryId.copy();
        this.publisherId = other.publisherId == null ? null : other.publisherId.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.bookItemId = other.bookItemId == null ? null : other.bookItemId.copy();
    }

    @Override
    public BookCriteria copy() {
        return new BookCriteria(this);
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

    public StringFilter getIsbn() {
        return isbn;
    }

    public StringFilter isbn() {
        if (isbn == null) {
            isbn = new StringFilter();
        }
        return isbn;
    }

    public void setIsbn(StringFilter isbn) {
        this.isbn = isbn;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public StringFilter subject() {
        if (subject == null) {
            subject = new StringFilter();
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public StringFilter getTarget() {
        return target;
    }

    public StringFilter target() {
        if (target == null) {
            target = new StringFilter();
        }
        return target;
    }

    public void setTarget(StringFilter target) {
        this.target = target;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public StringFilter language() {
        if (language == null) {
            language = new StringFilter();
        }
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public IntegerFilter getNumberOfPages() {
        return numberOfPages;
    }

    public IntegerFilter numberOfPages() {
        if (numberOfPages == null) {
            numberOfPages = new IntegerFilter();
        }
        return numberOfPages;
    }

    public void setNumberOfPages(IntegerFilter numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
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

    public LongFilter getCatagoryId() {
        return catagoryId;
    }

    public LongFilter catagoryId() {
        if (catagoryId == null) {
            catagoryId = new LongFilter();
        }
        return catagoryId;
    }

    public void setCatagoryId(LongFilter catagoryId) {
        this.catagoryId = catagoryId;
    }

    public LongFilter getPublisherId() {
        return publisherId;
    }

    public LongFilter publisherId() {
        if (publisherId == null) {
            publisherId = new LongFilter();
        }
        return publisherId;
    }

    public void setPublisherId(LongFilter publisherId) {
        this.publisherId = publisherId;
    }

    public LongFilter getAuthorId() {
        return authorId;
    }

    public LongFilter authorId() {
        if (authorId == null) {
            authorId = new LongFilter();
        }
        return authorId;
    }

    public void setAuthorId(LongFilter authorId) {
        this.authorId = authorId;
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
        final BookCriteria that = (BookCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(isbn, that.isbn) &&
            Objects.equals(title, that.title) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(target, that.target) &&
            Objects.equals(language, that.language) &&
            Objects.equals(numberOfPages, that.numberOfPages) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(catagoryId, that.catagoryId) &&
            Objects.equals(publisherId, that.publisherId) &&
            Objects.equals(authorId, that.authorId) &&
            Objects.equals(bookItemId, that.bookItemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            isbn,
            title,
            subject,
            target,
            language,
            numberOfPages,
            imageUrl,
            modifiedDate,
            userId,
            catagoryId,
            publisherId,
            authorId,
            bookItemId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (isbn != null ? "isbn=" + isbn + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (subject != null ? "subject=" + subject + ", " : "") +
            (target != null ? "target=" + target + ", " : "") +
            (language != null ? "language=" + language + ", " : "") +
            (numberOfPages != null ? "numberOfPages=" + numberOfPages + ", " : "") +
            (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
            (modifiedDate != null ? "modifiedDate=" + modifiedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (catagoryId != null ? "catagoryId=" + catagoryId + ", " : "") +
            (publisherId != null ? "publisherId=" + publisherId + ", " : "") +
            (authorId != null ? "authorId=" + authorId + ", " : "") +
            (bookItemId != null ? "bookItemId=" + bookItemId + ", " : "") +
            "}";
    }
}
