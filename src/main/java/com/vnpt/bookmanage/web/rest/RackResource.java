package com.vnpt.bookmanage.web.rest;

import com.vnpt.bookmanage.domain.Rack;
import com.vnpt.bookmanage.repository.RackRepository;
import com.vnpt.bookmanage.service.RackQueryService;
import com.vnpt.bookmanage.service.RackService;
import com.vnpt.bookmanage.service.criteria.RackCriteria;
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
 * REST controller for managing {@link com.vnpt.bookmanage.domain.Rack}.
 */
@RestController
@RequestMapping("/api")
public class RackResource {

    private final Logger log = LoggerFactory.getLogger(RackResource.class);

    private static final String ENTITY_NAME = "rack";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RackService rackService;

    private final RackRepository rackRepository;

    private final RackQueryService rackQueryService;

    public RackResource(RackService rackService, RackRepository rackRepository, RackQueryService rackQueryService) {
        this.rackService = rackService;
        this.rackRepository = rackRepository;
        this.rackQueryService = rackQueryService;
    }

    /**
     * {@code POST  /racks} : Create a new rack.
     *
     * @param rack the rack to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rack, or with status {@code 400 (Bad Request)} if the rack has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/racks")
    public ResponseEntity<Rack> createRack(@Valid @RequestBody Rack rack) throws URISyntaxException {
        log.debug("REST request to save Rack : {}", rack);
        if (rack.getId() != null) {
            throw new BadRequestAlertException("A new rack cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rack result = rackService.save(rack);
        return ResponseEntity
            .created(new URI("/api/racks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /racks/:id} : Updates an existing rack.
     *
     * @param id the id of the rack to save.
     * @param rack the rack to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rack,
     * or with status {@code 400 (Bad Request)} if the rack is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rack couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/racks/{id}")
    public ResponseEntity<Rack> updateRack(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Rack rack)
        throws URISyntaxException {
        log.debug("REST request to update Rack : {}, {}", id, rack);
        if (rack.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rack.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rack result = rackService.save(rack);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rack.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /racks/:id} : Partial updates given fields of an existing rack, field will ignore if it is null
     *
     * @param id the id of the rack to save.
     * @param rack the rack to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rack,
     * or with status {@code 400 (Bad Request)} if the rack is not valid,
     * or with status {@code 404 (Not Found)} if the rack is not found,
     * or with status {@code 500 (Internal Server Error)} if the rack couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/racks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Rack> partialUpdateRack(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rack rack
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rack partially : {}, {}", id, rack);
        if (rack.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rack.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rack> result = rackService.partialUpdate(rack);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rack.getId().toString())
        );
    }

    /**
     * {@code GET  /racks} : get all the racks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of racks in body.
     */
    @GetMapping("/racks")
    public ResponseEntity<List<Rack>> getAllRacks(RackCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Racks by criteria: {}", criteria);
        Page<Rack> page = rackQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /racks/count} : count all the racks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/racks/count")
    public ResponseEntity<Long> countRacks(RackCriteria criteria) {
        log.debug("REST request to count Racks by criteria: {}", criteria);
        return ResponseEntity.ok().body(rackQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /racks/:id} : get the "id" rack.
     *
     * @param id the id of the rack to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rack, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/racks/{id}")
    public ResponseEntity<Rack> getRack(@PathVariable Long id) {
        log.debug("REST request to get Rack : {}", id);
        Optional<Rack> rack = rackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rack);
    }

    /**
     * {@code DELETE  /racks/:id} : delete the "id" rack.
     *
     * @param id the id of the rack to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/racks/{id}")
    public ResponseEntity<Void> deleteRack(@PathVariable Long id) {
        log.debug("REST request to delete Rack : {}", id);
        rackService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
