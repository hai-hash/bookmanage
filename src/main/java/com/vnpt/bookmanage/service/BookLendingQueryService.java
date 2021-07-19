package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.*; // for static metamodels
import com.vnpt.bookmanage.domain.BookLending;
import com.vnpt.bookmanage.repository.BookLendingRepository;
import com.vnpt.bookmanage.service.criteria.BookLendingCriteria;
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
 * Service for executing complex queries for {@link BookLending} entities in the database.
 * The main input is a {@link BookLendingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookLending} or a {@link Page} of {@link BookLending} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookLendingQueryService extends QueryService<BookLending> {

    private final Logger log = LoggerFactory.getLogger(BookLendingQueryService.class);

    private final BookLendingRepository bookLendingRepository;

    public BookLendingQueryService(BookLendingRepository bookLendingRepository) {
        this.bookLendingRepository = bookLendingRepository;
    }

    /**
     * Return a {@link List} of {@link BookLending} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookLending> findByCriteria(BookLendingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BookLending> specification = createSpecification(criteria);
        return bookLendingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BookLending} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookLending> findByCriteria(BookLendingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BookLending> specification = createSpecification(criteria);
        return bookLendingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookLendingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BookLending> specification = createSpecification(criteria);
        return bookLendingRepository.count(specification);
    }

    /**
     * Function to convert {@link BookLendingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BookLending> createSpecification(BookLendingCriteria criteria) {
        Specification<BookLending> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BookLending_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), BookLending_.creationDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), BookLending_.status));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BookLending_.description));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(BookLending_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getBookLendingDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookLendingDetailsId(),
                            root -> root.join(BookLending_.bookLendingDetails, JoinType.LEFT).get(BookLendingDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
