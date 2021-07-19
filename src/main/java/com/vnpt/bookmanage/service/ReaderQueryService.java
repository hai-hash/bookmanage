package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.*; // for static metamodels
import com.vnpt.bookmanage.domain.Reader;
import com.vnpt.bookmanage.repository.ReaderRepository;
import com.vnpt.bookmanage.service.criteria.ReaderCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Reader} entities in the database.
 * The main input is a {@link ReaderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Reader} or a {@link Page} of {@link Reader} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReaderQueryService extends QueryService<Reader> {

    private final Logger log = LoggerFactory.getLogger(ReaderQueryService.class);

    private final ReaderRepository readerRepository;

    public ReaderQueryService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    /**
     * Return a {@link List} of {@link Reader} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Reader> findByCriteria(ReaderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reader> specification = createSpecification(criteria);
        return readerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Reader} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Reader> findByCriteria(ReaderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reader> specification = createSpecification(criteria);
        return readerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReaderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reader> specification = createSpecification(criteria);
        return readerRepository.count(specification);
    }

    /**
     * Function to convert {@link ReaderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reader> createSpecification(ReaderCriteria criteria) {
        Specification<Reader> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reader_.id));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Reader_.phone));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Reader_.streetAddress));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Reader_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Reader_.state));
            }
            if (criteria.getZipCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipCode(), Reader_.zipCode));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Reader_.country));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Reader_.status));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Reader_.modifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Reader_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getBookReservationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookReservationId(),
                            root -> root.join(Reader_.bookReservations, JoinType.LEFT).get(BookReservation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
