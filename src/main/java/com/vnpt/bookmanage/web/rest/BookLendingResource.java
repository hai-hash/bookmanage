package com.vnpt.bookmanage.web.rest;

import com.vnpt.bookmanage.domain.BookLending;
import com.vnpt.bookmanage.repository.BookLendingRepository;
import com.vnpt.bookmanage.service.BookLendingQueryService;
import com.vnpt.bookmanage.service.BookLendingService;
import com.vnpt.bookmanage.service.criteria.BookLendingCriteria;
import com.vnpt.bookmanage.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.vnpt.bookmanage.domain.BookLending}.
 */
@RestController
@RequestMapping("/api")
public class BookLendingResource {

    private final Logger log = LoggerFactory.getLogger(BookLendingResource.class);

    private static final String ENTITY_NAME = "bookLending";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookLendingService bookLendingService;

    private final BookLendingRepository bookLendingRepository;

    private final BookLendingQueryService bookLendingQueryService;

    public BookLendingResource(
        BookLendingService bookLendingService,
        BookLendingRepository bookLendingRepository,
        BookLendingQueryService bookLendingQueryService
    ) {
        this.bookLendingService = bookLendingService;
        this.bookLendingRepository = bookLendingRepository;
        this.bookLendingQueryService = bookLendingQueryService;
    }

    /**
     * {@code POST  /book-lendings} : Create a new bookLending.
     *
     * @param bookLending the bookLending to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookLending, or with status {@code 400 (Bad Request)} if the bookLending has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-lendings")
    public ResponseEntity<BookLending> createBookLending(@Valid @RequestBody BookLending bookLending) throws URISyntaxException {
        log.debug("REST request to save BookLending : {}", bookLending);
        if (bookLending.getId() != null) {
            throw new BadRequestAlertException("A new bookLending cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookLending result = bookLendingService.save(bookLending);
        return ResponseEntity
            .created(new URI("/api/book-lendings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-lendings/:id} : Updates an existing bookLending.
     *
     * @param id the id of the bookLending to save.
     * @param bookLending the bookLending to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookLending,
     * or with status {@code 400 (Bad Request)} if the bookLending is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookLending couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-lendings/{id}")
    public ResponseEntity<BookLending> updateBookLending(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookLending bookLending
    ) throws URISyntaxException {
        log.debug("REST request to update BookLending : {}, {}", id, bookLending);
        if (bookLending.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookLending.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookLendingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookLending result = bookLendingService.save(bookLending);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookLending.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /book-lendings/:id} : Partial updates given fields of an existing bookLending, field will ignore if it is null
     *
     * @param id the id of the bookLending to save.
     * @param bookLending the bookLending to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookLending,
     * or with status {@code 400 (Bad Request)} if the bookLending is not valid,
     * or with status {@code 404 (Not Found)} if the bookLending is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookLending couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/book-lendings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BookLending> partialUpdateBookLending(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookLending bookLending
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookLending partially : {}, {}", id, bookLending);
        if (bookLending.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookLending.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookLendingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookLending> result = bookLendingService.partialUpdate(bookLending);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookLending.getId().toString())
        );
    }

    /**
     * {@code GET  /book-lendings} : get all the bookLendings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookLendings in body.
     */
    @GetMapping("/book-lendings")
    public ResponseEntity<List<BookLending>> getAllBookLendings(BookLendingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BookLendings by criteria: {}", criteria);
        Page<BookLending> page = bookLendingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-lendings/count} : count all the bookLendings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/book-lendings/count")
    public ResponseEntity<Long> countBookLendings(BookLendingCriteria criteria) {
        log.debug("REST request to count BookLendings by criteria: {}", criteria);
        return ResponseEntity.ok().body(bookLendingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /book-lendings/:id} : get the "id" bookLending.
     *
     * @param id the id of the bookLending to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookLending, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-lendings/{id}")
    public ResponseEntity<BookLending> getBookLending(@PathVariable Long id) {
        log.debug("REST request to get BookLending : {}", id);
        Optional<BookLending> bookLending = bookLendingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookLending);
    }

    /**
     * {@code DELETE  /book-lendings/:id} : delete the "id" bookLending.
     *
     * @param id the id of the bookLending to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-lendings/{id}")
    public ResponseEntity<Void> deleteBookLending(@PathVariable Long id) {
        log.debug("REST request to delete BookLending : {}", id);
        bookLendingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
