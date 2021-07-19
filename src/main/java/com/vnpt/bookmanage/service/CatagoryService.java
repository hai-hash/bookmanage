package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.Catagory;
import com.vnpt.bookmanage.repository.CatagoryRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Catagory}.
 */
@Service
@Transactional
public class CatagoryService {

    private final Logger log = LoggerFactory.getLogger(CatagoryService.class);

    private final CatagoryRepository catagoryRepository;

    public CatagoryService(CatagoryRepository catagoryRepository) {
        this.catagoryRepository = catagoryRepository;
    }

    /**
     * Save a catagory.
     *
     * @param catagory the entity to save.
     * @return the persisted entity.
     */
    public Catagory save(Catagory catagory) {
        log.debug("Request to save Catagory : {}", catagory);
        return catagoryRepository.save(catagory);
    }

    /**
     * Partially update a catagory.
     *
     * @param catagory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Catagory> partialUpdate(Catagory catagory) {
        log.debug("Request to partially update Catagory : {}", catagory);

        return catagoryRepository
            .findById(catagory.getId())
            .map(
                existingCatagory -> {
                    if (catagory.getName() != null) {
                        existingCatagory.setName(catagory.getName());
                    }
                    if (catagory.getModifiedDate() != null) {
                        existingCatagory.setModifiedDate(catagory.getModifiedDate());
                    }
                    if (catagory.getIsActive() != null) {
                        existingCatagory.setIsActive(catagory.getIsActive());
                    }

                    return existingCatagory;
                }
            )
            .map(catagoryRepository::save);
    }

    /**
     * Get all the catagories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Catagory> findAll(Pageable pageable) {
        log.debug("Request to get all Catagories");
        return catagoryRepository.findAll(pageable);
    }

    /**
     * Get one catagory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Catagory> findOne(Long id) {
        log.debug("Request to get Catagory : {}", id);
        return catagoryRepository.findById(id);
    }

    /**
     * Delete the catagory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Catagory : {}", id);
        catagoryRepository.deleteById(id);
    }
}
