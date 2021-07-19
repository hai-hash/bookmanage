package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.Reader;
import com.vnpt.bookmanage.repository.ReaderRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reader}.
 */
@Service
@Transactional
public class ReaderService {

    private final Logger log = LoggerFactory.getLogger(ReaderService.class);

    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    /**
     * Save a reader.
     *
     * @param reader the entity to save.
     * @return the persisted entity.
     */
    public Reader save(Reader reader) {
        log.debug("Request to save Reader : {}", reader);
        return readerRepository.save(reader);
    }

    /**
     * Partially update a reader.
     *
     * @param reader the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Reader> partialUpdate(Reader reader) {
        log.debug("Request to partially update Reader : {}", reader);

        return readerRepository
            .findById(reader.getId())
            .map(
                existingReader -> {
                    if (reader.getPhone() != null) {
                        existingReader.setPhone(reader.getPhone());
                    }
                    if (reader.getStreetAddress() != null) {
                        existingReader.setStreetAddress(reader.getStreetAddress());
                    }
                    if (reader.getCity() != null) {
                        existingReader.setCity(reader.getCity());
                    }
                    if (reader.getState() != null) {
                        existingReader.setState(reader.getState());
                    }
                    if (reader.getZipCode() != null) {
                        existingReader.setZipCode(reader.getZipCode());
                    }
                    if (reader.getCountry() != null) {
                        existingReader.setCountry(reader.getCountry());
                    }
                    if (reader.getStatus() != null) {
                        existingReader.setStatus(reader.getStatus());
                    }
                    if (reader.getModifiedDate() != null) {
                        existingReader.setModifiedDate(reader.getModifiedDate());
                    }

                    return existingReader;
                }
            )
            .map(readerRepository::save);
    }

    /**
     * Get all the readers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Reader> findAll(Pageable pageable) {
        log.debug("Request to get all Readers");
        return readerRepository.findAll(pageable);
    }

    /**
     * Get one reader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Reader> findOne(Long id) {
        log.debug("Request to get Reader : {}", id);
        return readerRepository.findById(id);
    }

    /**
     * Delete the reader by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reader : {}", id);
        readerRepository.deleteById(id);
    }
}
