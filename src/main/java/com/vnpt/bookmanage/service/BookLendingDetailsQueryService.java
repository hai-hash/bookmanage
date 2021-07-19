package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.*; // for static metamodels
import com.vnpt.bookmanage.domain.BookLendingDetails;
import com.vnpt.bookmanage.repository.BookLendingDetailsRepository;
import com.vnpt.bookmanage.service.criteria.BookLendingDetailsCriteria;
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
 * Service for executing complex queries for {@link BookLendingDetails} entities in the database.
 * The main input is a {@link BookLendingDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookLendingDetails} or a {@link Page} of {@link BookLendingDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookLendingDetailsQueryService extends QueryService<BookLendingDetails> {

    private final Logger log = LoggerFactory.getLogger(BookLendingDetailsQueryService.class);

    private final BookLendingDetailsRepository bookLendingDetailsRepository;

    public BookLendingDetailsQueryService(BookLendingDetailsRepository bookLendingDetailsRepository) {
        this.bookLendingDetailsRepository = bookLendingDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link BookLendingDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookLendingDetails> findByCriteria(BookLendingDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BookLendingDetails> specification = createSpecification(criteria);
        return bookLendingDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BookLendingDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookLendingDetails> findByCriteria(BookLendingDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BookLendingDetails> specification = createSpecification(criteria);
        return bookLendingDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookLendingDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BookLendingDetails> specification = createSpecification(criteria);
        return bookLendingDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link BookLendingDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BookLendingDetails> createSpecification(BookLendingDetailsCriteria criteria) {
        Specification<BookLendingDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BookLendingDetails_.id));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), BookLendingDetails_.dueDate));
            }
            if (criteria.getReturnDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReturnDate(), BookLendingDetails_.returnDate));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), BookLendingDetails_.price));
            }
            if (criteria.getBookReservationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookReservationId(),
                            root -> root.join(BookLendingDetails_.bookReservation, JoinType.LEFT).get(BookReservation_.id)
                        )
                    );
            }
            if (criteria.getBookLendingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookLendingId(),
                            root -> root.join(BookLendingDetails_.bookLending, JoinType.LEFT).get(BookLending_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
