package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.BookLendingDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BookLendingDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookLendingDetailsRepository
    extends JpaRepository<BookLendingDetails, Long>, JpaSpecificationExecutor<BookLendingDetails> {}
