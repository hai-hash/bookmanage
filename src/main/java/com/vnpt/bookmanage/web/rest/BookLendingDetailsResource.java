package com.vnpt.bookmanage.web.rest;

import com.vnpt.bookmanage.domain.BookLendingDetails;
import com.vnpt.bookmanage.repository.BookLendingDetailsRepository;
import com.vnpt.bookmanage.service.BookLendingDetailsQueryService;
import com.vnpt.bookmanage.service.BookLendingDetailsService;
import com.vnpt.bookmanage.service.criteria.BookLendingDetailsCriteria;
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
 * REST controller for managing {@link com.vnpt.bookmanage.domain.BookLendingDetails}.
 */
@RestController
@RequestMapping("/api")
public class BookLendingDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BookLendingDetailsResource.class);

    private static final String ENTITY_NAME = "bookLendingDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookLendingDetailsService bookLendingDetailsService;

    private final BookLendingDetailsRepository bookLendingDetailsRepository;

    private final BookLendingDetailsQueryService bookLendingDetailsQueryService;

    public BookLendingDetailsResource(
        BookLendingDetailsService bookLendingDetailsService,
        BookLendingDetailsRepository bookLendingDetailsRepository,
        BookLendingDetailsQueryService bookLendingDetailsQueryService
    ) {
        this.bookLendingDetailsService = bookLendingDetailsService;
        this.bookLendingDetailsRepository = bookLendingDetailsRepository;
        this.bookLendingDetailsQueryService = bookLendingDetailsQueryService;
    }

    /**
     * {@code POST  /book-lending-details} : Create a new bookLendingDetails.
     *
     * @param bookLendingDetails the bookLendingDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookLendingDetails, or with status {@code 400 (Bad Request)} if the bookLendingDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-lending-details")
    public ResponseEntity<BookLendingDetails> createBookLendingDetails(@Valid @RequestBody BookLendingDetails bookLendingDetails)
        throws URISyntaxException {
        log.debug("REST request to save BookLendingDetails : {}", bookLendingDetails);
        if (bookLendingDetails.getId() != null) {
            throw new BadRequestAlertException("A new bookLendingDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookLendingDetails result = bookLendingDetailsService.save(bookLendingDetails);
        return ResponseEntity
            .created(new URI("/api/book-lending-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-lending-details/:id} : Updates an existing bookLendingDetails.
     *
     * @param id the id of the bookLendingDetails to save.
     * @param bookLendingDetails the bookLendingDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookLendingDetails,
     * or with status {@code 400 (Bad Request)} if the bookLendingDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookLendingDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-lending-details/{id}")
    public ResponseEntity<BookLendingDetails> updateBookLendingDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookLendingDetails bookLendingDetails
    ) throws URISyntaxException {
        log.debug("REST request to update BookLendingDetails : {}, {}", id, bookLendingDetails);
        if (bookLendingDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookLendingDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookLendingDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookLendingDetails result = bookLendingDetailsService.save(bookLendingDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookLendingDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /book-lending-details/:id} : Partial updates given fields of an existing bookLendingDetails, field will ignore if it is null
     *
     * @param id the id of the bookLendingDetails to save.
     * @param bookLendingDetails the bookLendingDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookLendingDetails,
     * or with status {@code 400 (Bad Request)} if the bookLendingDetails is not valid,
     * or with status {@code 404 (Not Found)} if the bookLendingDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookLendingDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/book-lending-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BookLendingDetails> partialUpdateBookLendingDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookLendingDetails bookLendingDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookLendingDetails partially : {}, {}", id, bookLendingDetails);
        if (bookLendingDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookLendingDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookLendingDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookLendingDetails> result = bookLendingDetailsService.partialUpdate(bookLendingDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookLendingDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /book-lending-details} : get all the bookLendingDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookLendingDetails in body.
     */
    @GetMapping("/book-lending-details")
    public ResponseEntity<List<BookLendingDetails>> getAllBookLendingDetails(BookLendingDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BookLendingDetails by criteria: {}", criteria);
        Page<BookLendingDetails> page = bookLendingDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-lending-details/count} : count all the bookLendingDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/book-lending-details/count")
    public ResponseEntity<Long> countBookLendingDetails(BookLendingDetailsCriteria criteria) {
        log.debug("REST request to count BookLendingDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(bookLendingDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /book-lending-details/:id} : get the "id" bookLendingDetails.
     *
     * @param id the id of the bookLendingDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookLendingDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-lending-details/{id}")
    public ResponseEntity<BookLendingDetails> getBookLendingDetails(@PathVariable Long id) {
        log.debug("REST request to get BookLendingDetails : {}", id);
        Optional<BookLendingDetails> bookLendingDetails = bookLendingDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookLendingDetails);
    }

    /**
     * {@code DELETE  /book-lending-details/:id} : delete the "id" bookLendingDetails.
     *
     * @param id the id of the bookLendingDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-lending-details/{id}")
    public ResponseEntity<Void> deleteBookLendingDetails(@PathVariable Long id) {
        log.debug("REST request to delete BookLendingDetails : {}", id);
        bookLendingDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
