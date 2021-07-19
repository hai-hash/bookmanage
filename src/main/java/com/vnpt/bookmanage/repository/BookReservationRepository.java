package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.BookReservation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BookReservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long>, JpaSpecificationExecutor<BookReservation> {}
