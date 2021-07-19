package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.Rack;
import com.vnpt.bookmanage.repository.RackRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rack}.
 */
@Service
@Transactional
public class RackService {

    private final Logger log = LoggerFactory.getLogger(RackService.class);

    private final RackRepository rackRepository;

    public RackService(RackRepository rackRepository) {
        this.rackRepository = rackRepository;
    }

    /**
     * Save a rack.
     *
     * @param rack the entity to save.
     * @return the persisted entity.
     */
    public Rack save(Rack rack) {
        log.debug("Request to save Rack : {}", rack);
        return rackRepository.save(rack);
    }

    /**
     * Partially update a rack.
     *
     * @param rack the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rack> partialUpdate(Rack rack) {
        log.debug("Request to partially update Rack : {}", rack);

        return rackRepository
            .findById(rack.getId())
            .map(
                existingRack -> {
                    if (rack.getNumber() != null) {
                        existingRack.setNumber(rack.getNumber());
                    }
                    if (rack.getLocationIdentifier() != null) {
                        existingRack.setLocationIdentifier(rack.getLocationIdentifier());
                    }
                    if (rack.getModifiedDate() != null) {
                        existingRack.setModifiedDate(rack.getModifiedDate());
                    }
                    if (rack.getIsActive() != null) {
                        existingRack.setIsActive(rack.getIsActive());
                    }

                    return existingRack;
                }
            )
            .map(rackRepository::save);
    }

    /**
     * Get all the racks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rack> findAll(Pageable pageable) {
        log.debug("Request to get all Racks");
        return rackRepository.findAll(pageable);
    }

    /**
     * Get one rack by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rack> findOne(Long id) {
        log.debug("Request to get Rack : {}", id);
        return rackRepository.findById(id);
    }

    /**
     * Delete the rack by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rack : {}", id);
        rackRepository.deleteById(id);
    }
}
