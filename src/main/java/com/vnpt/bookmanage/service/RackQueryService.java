package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.*; // for static metamodels
import com.vnpt.bookmanage.domain.Rack;
import com.vnpt.bookmanage.repository.RackRepository;
import com.vnpt.bookmanage.service.criteria.RackCriteria;
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
 * Service for executing complex queries for {@link Rack} entities in the database.
 * The main input is a {@link RackCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Rack} or a {@link Page} of {@link Rack} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RackQueryService extends QueryService<Rack> {

    private final Logger log = LoggerFactory.getLogger(RackQueryService.class);

    private final RackRepository rackRepository;

    public RackQueryService(RackRepository rackRepository) {
        this.rackRepository = rackRepository;
    }

    /**
     * Return a {@link List} of {@link Rack} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Rack> findByCriteria(RackCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rack> specification = createSpecification(criteria);
        return rackRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Rack} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Rack> findByCriteria(RackCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rack> specification = createSpecification(criteria);
        return rackRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RackCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rack> specification = createSpecification(criteria);
        return rackRepository.count(specification);
    }

    /**
     * Function to convert {@link RackCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rack> createSpecification(RackCriteria criteria) {
        Specification<Rack> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rack_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Rack_.number));
            }
            if (criteria.getLocationIdentifier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationIdentifier(), Rack_.locationIdentifier));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Rack_.modifiedDate));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Rack_.isActive));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Rack_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getBookItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBookItemId(), root -> root.join(Rack_.bookItems, JoinType.LEFT).get(BookItem_.id))
                    );
            }
        }
        return specification;
    }
}
