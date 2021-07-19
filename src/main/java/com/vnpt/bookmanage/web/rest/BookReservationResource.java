package com.vnpt.bookmanage.web.rest;

import com.vnpt.bookmanage.domain.BookReservation;
import com.vnpt.bookmanage.repository.BookReservationRepository;
import com.vnpt.bookmanage.service.BookReservationQueryService;
import com.vnpt.bookmanage.service.BookReservationService;
import com.vnpt.bookmanage.service.criteria.BookReservationCriteria;
import com.vnpt.bookmanage.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.vnpt.bookmanage.domain.BookReservation}.
 */
@RestController
@RequestMapping("/api")
public class BookReservationResource {

    private final Logger log = LoggerFactory.getLogger(BookReservationResource.class);

    private static final String ENTITY_NAME = "bookReservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookReservationService bookReservationService;

    private final BookReservationRepository bookReservationRepository;

    private final BookReservationQueryService bookReservationQueryService;

    public BookReservationResource(
        BookReservationService bookReservationService,
        BookReservationRepository bookReservationRepository,
        BookReservationQueryService bookReservationQueryService
    ) {
        this.bookReservationService = bookReservationService;
        this.bookReservationRepository = bookReservationRepository;
        this.bookReservationQueryService = bookReservationQueryService;
    }

    /**
     * {@code POST  /book-reservations} : Create a new bookReservation.
     *
     * @param bookReservation the bookReservation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookReservation, or with status {@code 400 (Bad Request)} if the bookReservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-reservations")
    public ResponseEntity<BookReservation> createBookReservation(@RequestBody BookReservation bookReservation) throws URISyntaxException {
        log.debug("REST request to save BookReservation : {}", bookReservation);
        if (bookReservation.getId() != null) {
            throw new BadRequestAlertException("A new bookReservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookReservation result = bookReservationService.save(bookReservation);
        return ResponseEntity
            .created(new URI("/api/book-reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-reservations/:id} : Updates an existing bookReservation.
     *
     * @param id the id of the bookReservation to save.
     * @param bookReservation the bookReservation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookReservation,
     * or with status {@code 400 (Bad Request)} if the bookReservation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookReservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-reservations/{id}")
    public ResponseEntity<BookReservation> updateBookReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookReservation bookReservation
    ) throws URISyntaxException {
        log.debug("REST request to update BookReservation : {}, {}", id, bookReservation);
        if (bookReservation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookReservation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookReservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookReservation result = bookReservationService.save(bookReservation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookReservation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /book-reservations/:id} : Partial updates given fields of an existing bookReservation, field will ignore if it is null
     *
     * @param id the id of the bookReservation to save.
     * @param bookReservation the bookReservation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookReservation,
     * or with status {@code 400 (Bad Request)} if the bookReservation is not valid,
     * or with status {@code 404 (Not Found)} if the bookReservation is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookReservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/book-reservations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BookReservation> partialUpdateBookReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BookReservation bookReservation
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookReservation partially : {}, {}", id, bookReservation);
        if (bookReservation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookReservation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookReservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookReservation> result = bookReservationService.partialUpdate(bookReservation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookReservation.getId().toString())
        );
    }

    /**
     * {@code GET  /book-reservations} : get all the bookReservations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookReservations in body.
     */
    @GetMapping("/book-reservations")
    public ResponseEntity<List<BookReservation>> getAllBookReservations(BookReservationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BookReservations by criteria: {}", criteria);
        Page<BookReservation> page = bookReservationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-reservations/count} : count all the bookReservations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/book-reservations/count")
    public ResponseEntity<Long> countBookReservations(BookReservationCriteria criteria) {
        log.debug("REST request to count BookReservations by criteria: {}", criteria);
        return ResponseEntity.ok().body(bookReservationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /book-reservations/:id} : get the "id" bookReservation.
     *
     * @param id the id of the bookReservation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookReservation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-reservations/{id}")
    public ResponseEntity<BookReservation> getBookReservation(@PathVariable Long id) {
        log.debug("REST request to get BookReservation : {}", id);
        Optional<BookReservation> bookReservation = bookReservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookReservation);
    }

    /**
     * {@code DELETE  /book-reservations/:id} : delete the "id" bookReservation.
     *
     * @param id the id of the bookReservation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-reservations/{id}")
    public ResponseEntity<Void> deleteBookReservation(@PathVariable Long id) {
        log.debug("REST request to delete BookReservation : {}", id);
        bookReservationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
