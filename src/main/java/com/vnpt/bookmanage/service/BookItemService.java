package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.BookItem;
import com.vnpt.bookmanage.repository.BookItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookItem}.
 */
@Service
@Transactional
public class BookItemService {

    private final Logger log = LoggerFactory.getLogger(BookItemService.class);

    private final BookItemRepository bookItemRepository;

    public BookItemService(BookItemRepository bookItemRepository) {
        this.bookItemRepository = bookItemRepository;
    }

    /**
     * Save a bookItem.
     *
     * @param bookItem the entity to save.
     * @return the persisted entity.
     */
    public BookItem save(BookItem bookItem) {
        log.debug("Request to save BookItem : {}", bookItem);
        return bookItemRepository.save(bookItem);
    }

    /**
     * Partially update a bookItem.
     *
     * @param bookItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookItem> partialUpdate(BookItem bookItem) {
        log.debug("Request to partially update BookItem : {}", bookItem);

        return bookItemRepository
            .findById(bookItem.getId())
            .map(
                existingBookItem -> {
                    if (bookItem.getBarcode() != null) {
                        existingBookItem.setBarcode(bookItem.getBarcode());
                    }
                    if (bookItem.getIsReferenceOnly() != null) {
                        existingBookItem.setIsReferenceOnly(bookItem.getIsReferenceOnly());
                    }
                    if (bookItem.getBorrowed() != null) {
                        existingBookItem.setBorrowed(bookItem.getBorrowed());
                    }
                    if (bookItem.getDueDate() != null) {
                        existingBookItem.setDueDate(bookItem.getDueDate());
                    }
                    if (bookItem.getPrice() != null) {
                        existingBookItem.setPrice(bookItem.getPrice());
                    }
                    if (bookItem.getFormat() != null) {
                        existingBookItem.setFormat(bookItem.getFormat());
                    }
                    if (bookItem.getStatus() != null) {
                        existingBookItem.setStatus(bookItem.getStatus());
                    }
                    if (bookItem.getDateOfPurchase() != null) {
                        existingBookItem.setDateOfPurchase(bookItem.getDateOfPurchase());
                    }
                    if (bookItem.getPublicationDate() != null) {
                        existingBookItem.setPublicationDate(bookItem.getPublicationDate());
                    }
                    if (bookItem.getModifiedDate() != null) {
                        existingBookItem.setModifiedDate(bookItem.getModifiedDate());
                    }

                    return existingBookItem;
                }
            )
            .map(bookItemRepository::save);
    }

    /**
     * Get all the bookItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookItem> findAll(Pageable pageable) {
        log.debug("Request to get all BookItems");
        return bookItemRepository.findAll(pageable);
    }

    /**
     *  Get all the bookItems where BookReservation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookItem> findAllWhereBookReservationIsNull() {
        log.debug("Request to get all bookItems where BookReservation is null");
        return StreamSupport
            .stream(bookItemRepository.findAll().spliterator(), false)
            .filter(bookItem -> bookItem.getBookReservation() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one bookItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookItem> findOne(Long id) {
        log.debug("Request to get BookItem : {}", id);
        return bookItemRepository.findById(id);
    }

    /**
     * Delete the bookItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BookItem : {}", id);
        bookItemRepository.deleteById(id);
    }
}
