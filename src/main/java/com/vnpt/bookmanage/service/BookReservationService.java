package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.BookReservation;
import com.vnpt.bookmanage.repository.BookReservationRepository;
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
 * Service Implementation for managing {@link BookReservation}.
 */
@Service
@Transactional
public class BookReservationService {

    private final Logger log = LoggerFactory.getLogger(BookReservationService.class);

    private final BookReservationRepository bookReservationRepository;

    public BookReservationService(BookReservationRepository bookReservationRepository) {
        this.bookReservationRepository = bookReservationRepository;
    }

    /**
     * Save a bookReservation.
     *
     * @param bookReservation the entity to save.
     * @return the persisted entity.
     */
    public BookReservation save(BookReservation bookReservation) {
        log.debug("Request to save BookReservation : {}", bookReservation);
        return bookReservationRepository.save(bookReservation);
    }

    /**
     * Partially update a bookReservation.
     *
     * @param bookReservation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookReservation> partialUpdate(BookReservation bookReservation) {
        log.debug("Request to partially update BookReservation : {}", bookReservation);

        return bookReservationRepository
            .findById(bookReservation.getId())
            .map(
                existingBookReservation -> {
                    if (bookReservation.getCreationDate() != null) {
                        existingBookReservation.setCreationDate(bookReservation.getCreationDate());
                    }
                    if (bookReservation.getStatus() != null) {
                        existingBookReservation.setStatus(bookReservation.getStatus());
                    }

                    return existingBookReservation;
                }
            )
            .map(bookReservationRepository::save);
    }

    /**
     * Get all the bookReservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookReservation> findAll(Pageable pageable) {
        log.debug("Request to get all BookReservations");
        return bookReservationRepository.findAll(pageable);
    }

    /**
     *  Get all the bookReservations where BookLendingDetails is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookReservation> findAllWhereBookLendingDetailsIsNull() {
        log.debug("Request to get all bookReservations where BookLendingDetails is null");
        return StreamSupport
            .stream(bookReservationRepository.findAll().spliterator(), false)
            .filter(bookReservation -> bookReservation.getBookLendingDetails() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one bookReservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookReservation> findOne(Long id) {
        log.debug("Request to get BookReservation : {}", id);
        return bookReservationRepository.findById(id);
    }

    /**
     * Delete the bookReservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BookReservation : {}", id);
        bookReservationRepository.deleteById(id);
    }
}
