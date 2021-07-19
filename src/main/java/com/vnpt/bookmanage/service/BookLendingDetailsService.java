package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.BookLendingDetails;
import com.vnpt.bookmanage.repository.BookLendingDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookLendingDetails}.
 */
@Service
@Transactional
public class BookLendingDetailsService {

    private final Logger log = LoggerFactory.getLogger(BookLendingDetailsService.class);

    private final BookLendingDetailsRepository bookLendingDetailsRepository;

    public BookLendingDetailsService(BookLendingDetailsRepository bookLendingDetailsRepository) {
        this.bookLendingDetailsRepository = bookLendingDetailsRepository;
    }

    /**
     * Save a bookLendingDetails.
     *
     * @param bookLendingDetails the entity to save.
     * @return the persisted entity.
     */
    public BookLendingDetails save(BookLendingDetails bookLendingDetails) {
        log.debug("Request to save BookLendingDetails : {}", bookLendingDetails);
        return bookLendingDetailsRepository.save(bookLendingDetails);
    }

    /**
     * Partially update a bookLendingDetails.
     *
     * @param bookLendingDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookLendingDetails> partialUpdate(BookLendingDetails bookLendingDetails) {
        log.debug("Request to partially update BookLendingDetails : {}", bookLendingDetails);

        return bookLendingDetailsRepository
            .findById(bookLendingDetails.getId())
            .map(
                existingBookLendingDetails -> {
                    if (bookLendingDetails.getDueDate() != null) {
                        existingBookLendingDetails.setDueDate(bookLendingDetails.getDueDate());
                    }
                    if (bookLendingDetails.getReturnDate() != null) {
                        existingBookLendingDetails.setReturnDate(bookLendingDetails.getReturnDate());
                    }
                    if (bookLendingDetails.getPrice() != null) {
                        existingBookLendingDetails.setPrice(bookLendingDetails.getPrice());
                    }

                    return existingBookLendingDetails;
                }
            )
            .map(bookLendingDetailsRepository::save);
    }

    /**
     * Get all the bookLendingDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookLendingDetails> findAll(Pageable pageable) {
        log.debug("Request to get all BookLendingDetails");
        return bookLendingDetailsRepository.findAll(pageable);
    }

    /**
     * Get one bookLendingDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookLendingDetails> findOne(Long id) {
        log.debug("Request to get BookLendingDetails : {}", id);
        return bookLendingDetailsRepository.findById(id);
    }

    /**
     * Delete the bookLendingDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BookLendingDetails : {}", id);
        bookLendingDetailsRepository.deleteById(id);
    }
}
