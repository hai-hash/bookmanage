package com.vnpt.bookmanage.web.rest;

import com.vnpt.bookmanage.domain.Reader;
import com.vnpt.bookmanage.repository.ReaderRepository;
import com.vnpt.bookmanage.service.ReaderQueryService;
import com.vnpt.bookmanage.service.ReaderService;
import com.vnpt.bookmanage.service.criteria.ReaderCriteria;
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
 * REST controller for managing {@link com.vnpt.bookmanage.domain.Reader}.
 */
@RestController
@RequestMapping("/api")
public class ReaderResource {

    private final Logger log = LoggerFactory.getLogger(ReaderResource.class);

    private static final String ENTITY_NAME = "reader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReaderService readerService;

    private final ReaderRepository readerRepository;

    private final ReaderQueryService readerQueryService;

    public ReaderResource(ReaderService readerService, ReaderRepository readerRepository, ReaderQueryService readerQueryService) {
        this.readerService = readerService;
        this.readerRepository = readerRepository;
        this.readerQueryService = readerQueryService;
    }

    /**
     * {@code POST  /readers} : Create a new reader.
     *
     * @param reader the reader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reader, or with status {@code 400 (Bad Request)} if the reader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/readers")
    public ResponseEntity<Reader> createReader(@Valid @RequestBody Reader reader) throws URISyntaxException {
        log.debug("REST request to save Reader : {}", reader);
        if (reader.getId() != null) {
            throw new BadRequestAlertException("A new reader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reader result = readerService.save(reader);
        return ResponseEntity
            .created(new URI("/api/readers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /readers/:id} : Updates an existing reader.
     *
     * @param id the id of the reader to save.
     * @param reader the reader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reader,
     * or with status {@code 400 (Bad Request)} if the reader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/readers/{id}")
    public ResponseEntity<Reader> updateReader(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Reader reader
    ) throws URISyntaxException {
        log.debug("REST request to update Reader : {}, {}", id, reader);
        if (reader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!readerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Reader result = readerService.save(reader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /readers/:id} : Partial updates given fields of an existing reader, field will ignore if it is null
     *
     * @param id the id of the reader to save.
     * @param reader the reader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reader,
     * or with status {@code 400 (Bad Request)} if the reader is not valid,
     * or with status {@code 404 (Not Found)} if the reader is not found,
     * or with status {@code 500 (Internal Server Error)} if the reader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/readers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Reader> partialUpdateReader(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Reader reader
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reader partially : {}, {}", id, reader);
        if (reader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!readerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reader> result = readerService.partialUpdate(reader);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reader.getId().toString())
        );
    }

    /**
     * {@code GET  /readers} : get all the readers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of readers in body.
     */
    @GetMapping("/readers")
    public ResponseEntity<List<Reader>> getAllReaders(ReaderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Readers by criteria: {}", criteria);
        Page<Reader> page = readerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    @GetMapping("/readers/UserCurrentUser")
    public ResponseEntity<List<Reader>> getAllReaderById(@PathVariable(value = "id", required = false) final Long id){
    	log.debug("REST request to get Readers by Id ",id);
    	List<Reader> readers = readerRepository.findByUserIsCurrentUser();
    	
    	return ResponseEntity.ok().body(readers);
    	
    }

    /**
     * {@code GET  /readers/count} : count all the readers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/readers/count")
    public ResponseEntity<Long> countReaders(ReaderCriteria criteria) {
        log.debug("REST request to count Readers by criteria: {}", criteria);
        return ResponseEntity.ok().body(readerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /readers/:id} : get the "id" reader.
     *
     * @param id the id of the reader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/readers/{id}")
    public ResponseEntity<Reader> getReader(@PathVariable Long id) {
        log.debug("REST request to get Reader : {}", id);
        Optional<Reader> reader = readerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reader);
    }

    /**
     * {@code DELETE  /readers/:id} : delete the "id" reader.
     *
     * @param id the id of the reader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/readers/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        log.debug("REST request to delete Reader : {}", id);
        readerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
