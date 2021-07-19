package com.vnpt.bookmanage.service;

import com.vnpt.bookmanage.domain.*; // for static metamodels
import com.vnpt.bookmanage.domain.BookItem;
import com.vnpt.bookmanage.repository.BookItemRepository;
import com.vnpt.bookmanage.service.criteria.BookItemCriteria;
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
 * Service for executing complex queries for {@link BookItem} entities in the database.
 * The main input is a {@link BookItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookItem} or a {@link Page} of {@link BookItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookItemQueryService extends QueryService<BookItem> {

    private final Logger log = LoggerFactory.getLogger(BookItemQueryService.class);

    private final BookItemRepository bookItemRepository;

    public BookItemQueryService(BookItemRepository bookItemRepository) {
        this.bookItemRepository = bookItemRepository;
    }

    /**
     * Return a {@link List} of {@link BookItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookItem> findByCriteria(BookItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BookItem> specification = createSpecification(criteria);
        return bookItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BookItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookItem> findByCriteria(BookItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BookItem> specification = createSpecification(criteria);
        return bookItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BookItem> specification = createSpecification(criteria);
        return bookItemRepository.count(specification);
    }

    /**
     * Function to convert {@link BookItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BookItem> createSpecification(BookItemCriteria criteria) {
        Specification<BookItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BookItem_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), BookItem_.barcode));
            }
            if (criteria.getIsReferenceOnly() != null) {
                specification = specification.and(buildSpecification(criteria.getIsReferenceOnly(), BookItem_.isReferenceOnly));
            }
            if (criteria.getBorrowed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBorrowed(), BookItem_.borrowed));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), BookItem_.dueDate));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), BookItem_.price));
            }
            if (criteria.getFormat() != null) {
                specification = specification.and(buildSpecification(criteria.getFormat(), BookItem_.format));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), BookItem_.status));
            }
            if (criteria.getDateOfPurchase() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfPurchase(), BookItem_.dateOfPurchase));
            }
            if (criteria.getPublicationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublicationDate(), BookItem_.publicationDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), BookItem_.modifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(BookItem_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getRackId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRackId(), root -> root.join(BookItem_.rack, JoinType.LEFT).get(Rack_.id))
                    );
            }
            if (criteria.getBookId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBookId(), root -> root.join(BookItem_.book, JoinType.LEFT).get(Book_.id))
                    );
            }
            if (criteria.getBookReservationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookReservationId(),
                            root -> root.join(BookItem_.bookReservation, JoinType.LEFT).get(BookReservation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
