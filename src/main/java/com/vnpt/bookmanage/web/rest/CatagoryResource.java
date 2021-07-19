package com.vnpt.bookmanage.web.rest;

import com.vnpt.bookmanage.domain.Catagory;
import com.vnpt.bookmanage.repository.CatagoryRepository;
import com.vnpt.bookmanage.service.CatagoryQueryService;
import com.vnpt.bookmanage.service.CatagoryService;
import com.vnpt.bookmanage.service.criteria.CatagoryCriteria;
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
 * REST controller for managing {@link com.vnpt.bookmanage.domain.Catagory}.
 */
@RestController
@RequestMapping("/api")
public class CatagoryResource {

    private final Logger log = LoggerFactory.getLogger(CatagoryResource.class);

    private static final String ENTITY_NAME = "catagory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatagoryService catagoryService;

    private final CatagoryRepository catagoryRepository;

    private final CatagoryQueryService catagoryQueryService;

    public CatagoryResource(
        CatagoryService catagoryService,
        CatagoryRepository catagoryRepository,
        CatagoryQueryService catagoryQueryService
    ) {
        this.catagoryService = catagoryService;
        this.catagoryRepository = catagoryRepository;
        this.catagoryQueryService = catagoryQueryService;
    }

    /**
     * {@code POST  /catagories} : Create a new catagory.
     *
     * @param catagory the catagory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catagory, or with status {@code 400 (Bad Request)} if the catagory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catagories")
    public ResponseEntity<Catagory> createCatagory(@Valid @RequestBody Catagory catagory) throws URISyntaxException {
        log.debug("REST request to save Catagory : {}", catagory);
        if (catagory.getId() != null) {
            throw new BadRequestAlertException("A new catagory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Catagory result = catagoryService.save(catagory);
        return ResponseEntity
            .created(new URI("/api/catagories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catagories/:id} : Updates an existing catagory.
     *
     * @param id the id of the catagory to save.
     * @param catagory the catagory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catagory,
     * or with status {@code 400 (Bad Request)} if the catagory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catagory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catagories/{id}")
    public ResponseEntity<Catagory> updateCatagory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Catagory catagory
    ) throws URISyntaxException {
        log.debug("REST request to update Catagory : {}, {}", id, catagory);
        if (catagory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catagory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catagoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Catagory result = catagoryService.save(catagory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, catagory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catagories/:id} : Partial updates given fields of an existing catagory, field will ignore if it is null
     *
     * @param id the id of the catagory to save.
     * @param catagory the catagory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catagory,
     * or with status {@code 400 (Bad Request)} if the catagory is not valid,
     * or with status {@code 404 (Not Found)} if the catagory is not found,
     * or with status {@code 500 (Internal Server Error)} if the catagory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catagories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Catagory> partialUpdateCatagory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Catagory catagory
    ) throws URISyntaxException {
        log.debug("REST request to partial update Catagory partially : {}, {}", id, catagory);
        if (catagory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catagory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catagoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Catagory> result = catagoryService.partialUpdate(catagory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, catagory.getId().toString())
        );
    }

    /**
     * {@code GET  /catagories} : get all the catagories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catagories in body.
     */
    @GetMapping("/catagories")
    public ResponseEntity<List<Catagory>> getAllCatagories(CatagoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Catagories by criteria: {}", criteria);
        Page<Catagory> page = catagoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catagories/count} : count all the catagories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/catagories/count")
    public ResponseEntity<Long> countCatagories(CatagoryCriteria criteria) {
        log.debug("REST request to count Catagories by criteria: {}", criteria);
        return ResponseEntity.ok().body(catagoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /catagories/:id} : get the "id" catagory.
     *
     * @param id the id of the catagory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catagory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catagories/{id}")
    public ResponseEntity<Catagory> getCatagory(@PathVariable Long id) {
        log.debug("REST request to get Catagory : {}", id);
        Optional<Catagory> catagory = catagoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catagory);
    }

    /**
     * {@code DELETE  /catagories/:id} : delete the "id" catagory.
     *
     * @param id the id of the catagory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catagories/{id}")
    public ResponseEntity<Void> deleteCatagory(@PathVariable Long id) {
        log.debug("REST request to delete Catagory : {}", id);
        catagoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
