package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.*; // for static metamodels
import com.vnpt.bookmanage.domain.Catagory;
import com.vnpt.bookmanage.repository.CatagoryRepository;
import com.vnpt.bookmanage.service.criteria.CatagoryCriteria;
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
 * Service for executing complex queries for {@link Catagory} entities in the database.
 * The main input is a {@link CatagoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Catagory} or a {@link Page} of {@link Catagory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatagoryQueryService extends QueryService<Catagory> {

    private final Logger log = LoggerFactory.getLogger(CatagoryQueryService.class);

    private final CatagoryRepository catagoryRepository;

    public CatagoryQueryService(CatagoryRepository catagoryRepository) {
        this.catagoryRepository = catagoryRepository;
    }

    /**
     * Return a {@link List} of {@link Catagory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Catagory> findByCriteria(CatagoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Catagory> specification = createSpecification(criteria);
        return catagoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Catagory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Catagory> findByCriteria(CatagoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Catagory> specification = createSpecification(criteria);
        return catagoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatagoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Catagory> specification = createSpecification(criteria);
        return catagoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CatagoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Catagory> createSpecification(CatagoryCriteria criteria) {
        Specification<Catagory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Catagory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Catagory_.name));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Catagory_.modifiedDate));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Catagory_.isActive));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Catagory_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCatalogId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCatalogId(), root -> root.join(Catagory_.catalog, JoinType.LEFT).get(Catagory_.id))
                    );
            }
            if (criteria.getBookId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBookId(), root -> root.join(Catagory_.books, JoinType.LEFT).get(Book_.id))
                    );
            }
        }
        return specification;
    }
}
