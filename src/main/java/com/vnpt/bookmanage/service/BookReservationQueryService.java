package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.*; // for static metamodels
import com.vnpt.bookmanage.domain.BookReservation;
import com.vnpt.bookmanage.repository.BookReservationRepository;
import com.vnpt.bookmanage.service.criteria.BookReservationCriteria;
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
 * Service for executing complex queries for {@link BookReservation} entities in the database.
 * The main input is a {@link BookReservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookReservation} or a {@link Page} of {@link BookReservation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookReservationQueryService extends QueryService<BookReservation> {

    private final Logger log = LoggerFactory.getLogger(BookReservationQueryService.class);

    private final BookReservationRepository bookReservationRepository;

    public BookReservationQueryService(BookReservationRepository bookReservationRepository) {
        this.bookReservationRepository = bookReservationRepository;
    }

    /**
     * Return a {@link List} of {@link BookReservation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookReservation> findByCriteria(BookReservationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BookReservation> specification = createSpecification(criteria);
        return bookReservationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BookReservation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookReservation> findByCriteria(BookReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BookReservation> specification = createSpecification(criteria);
        return bookReservationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookReservationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BookReservation> specification = createSpecification(criteria);
        return bookReservationRepository.count(specification);
    }

    /**
     * Function to convert {@link BookReservationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BookReservation> createSpecification(BookReservationCriteria criteria) {
        Specification<BookReservation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BookReservation_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), BookReservation_.creationDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), BookReservation_.status));
            }
            if (criteria.getBookItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookItemId(),
                            root -> root.join(BookReservation_.bookItem, JoinType.LEFT).get(BookItem_.id)
                        )
                    );
            }
            if (criteria.getReaderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReaderId(),
                            root -> root.join(BookReservation_.reader, JoinType.LEFT).get(Reader_.id)
                        )
                    );
            }
            if (criteria.getBookLendingDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookLendingDetailsId(),
                            root -> root.join(BookReservation_.bookLendingDetails, JoinType.LEFT).get(BookLendingDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
