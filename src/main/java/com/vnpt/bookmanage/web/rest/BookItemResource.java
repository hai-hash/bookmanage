package com.vnpt.bookmanage.web.rest;

import com.vnpt.bookmanage.domain.BookItem;
import com.vnpt.bookmanage.repository.BookItemRepository;
import com.vnpt.bookmanage.service.BookItemQueryService;
import com.vnpt.bookmanage.service.BookItemService;
import com.vnpt.bookmanage.service.criteria.BookItemCriteria;
import com.vnpt.bookmanage.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.vnpt.bookmanage.domain.BookItem}.
 */
@RestController
@RequestMapping("/api")
public class BookItemResource {

    private final Logger log = LoggerFactory.getLogger(BookItemResource.class);

    private static final String ENTITY_NAME = "bookItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookItemService bookItemService;

    private final BookItemRepository bookItemRepository;

    private final BookItemQueryService bookItemQueryService;

    public BookItemResource(
        BookItemService bookItemService,
        BookItemRepository bookItemRepository,
        BookItemQueryService bookItemQueryService
    ) {
        this.bookItemService = bookItemService;
        this.bookItemRepository = bookItemRepository;
        this.bookItemQueryService = bookItemQueryService;
    }

    /**
     * {@code POST  /book-items} : Create a new bookItem.
     *
     * @param bookItem the bookItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookItem, or with status {@code 400 (Bad Request)} if the bookItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-items")
    public ResponseEntity<BookItem> createBookItem(@Valid @RequestBody BookItem bookItem) throws URISyntaxException {
        log.debug("REST request to save BookItem : {}", bookItem);
        if (bookItem.getId() != null) {
            throw new BadRequestAlertException("A new bookItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookItem result = bookItemService.save(bookItem);
        return ResponseEntity
            .created(new URI("/api/book-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-items/:id} : Updates an existing bookItem.
     *
     * @param id the id of the bookItem to save.
     * @param bookItem the bookItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookItem,
     * or with status {@code 400 (Bad Request)} if the bookItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-items/{id}")
    public ResponseEntity<BookItem> updateBookItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookItem bookItem
    ) throws URISyntaxException {
        log.debug("REST request to update BookItem : {}, {}", id, bookItem);
        if (bookItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookItem result = bookItemService.save(bookItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /book-items/:id} : Partial updates given fields of an existing bookItem, field will ignore if it is null
     *
     * @param id the id of the bookItem to save.
     * @param bookItem the bookItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookItem,
     * or with status {@code 400 (Bad Request)} if the bookItem is not valid,
     * or with status {@code 404 (Not Found)} if the bookItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/book-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BookItem> partialUpdateBookItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookItem bookItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookItem partially : {}, {}", id, bookItem);
        if (bookItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookItem> result = bookItemService.partialUpdate(bookItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bookItem.getId().toString())
        );
    }

    /**
     * {@code GET  /book-items} : get all the bookItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookItems in body.
     */
    @GetMapping("/book-items")
    public ResponseEntity<List<BookItem>> getAllBookItems(BookItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BookItems by criteria: {}", criteria);
        Page<BookItem> page = bookItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-items/count} : count all the bookItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/book-items/count")
    public ResponseEntity<Long> countBookItems(BookItemCriteria criteria) {
        log.debug("REST request to count BookItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(bookItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /book-items/:id} : get the "id" bookItem.
     *
     * @param id the id of the bookItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-items/{id}")
    public ResponseEntity<BookItem> getBookItem(@PathVariable Long id) {
        log.debug("REST request to get BookItem : {}", id);
        Optional<BookItem> bookItem = bookItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookItem);
    }

    /**
     * {@code DELETE  /book-items/:id} : delete the "id" bookItem.
     *
     * @param id the id of the bookItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-items/{id}")
    public ResponseEntity<Void> deleteBookItem(@PathVariable Long id) {
        log.debug("REST request to delete BookItem : {}", id);
        bookItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
