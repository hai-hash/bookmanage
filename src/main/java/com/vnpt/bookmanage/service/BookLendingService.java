package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.BookLending;
import com.vnpt.bookmanage.repository.BookLendingRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BookLending}.
 */
@Service
@Transactional
public class BookLendingService {

    private final Logger log = LoggerFactory.getLogger(BookLendingService.class);

    private final BookLendingRepository bookLendingRepository;

    public BookLendingService(BookLendingRepository bookLendingRepository) {
        this.bookLendingRepository = bookLendingRepository;
    }

    /**
     * Save a bookLending.
     *
     * @param bookLending the entity to save.
     * @return the persisted entity.
     */
    public BookLending save(BookLending bookLending) {
        log.debug("Request to save BookLending : {}", bookLending);
        return bookLendingRepository.save(bookLending);
    }

    /**
     * Partially update a bookLending.
     *
     * @param bookLending the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookLending> partialUpdate(BookLending bookLending) {
        log.debug("Request to partially update BookLending : {}", bookLending);

        return bookLendingRepository
            .findById(bookLending.getId())
            .map(
                existingBookLending -> {
                    if (bookLending.getCreationDate() != null) {
                        existingBookLending.setCreationDate(bookLending.getCreationDate());
                    }
                    if (bookLending.getStatus() != null) {
                        existingBookLending.setStatus(bookLending.getStatus());
                    }
                    if (bookLending.getDescription() != null) {
                        existingBookLending.setDescription(bookLending.getDescription());
                    }

                    return existingBookLending;
                }
            )
            .map(bookLendingRepository::save);
    }

    /**
     * Get all the bookLendings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookLending> findAll(Pageable pageable) {
        log.debug("Request to get all BookLendings");
        return bookLendingRepository.findAll(pageable);
    }

    /**
     * Get one bookLending by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookLending> findOne(Long id) {
        log.debug("Request to get BookLending : {}", id);
        return bookLendingRepository.findById(id);
    }

    /**
     * Delete the bookLending by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BookLending : {}", id);
        bookLendingRepository.deleteById(id);
    }
}
