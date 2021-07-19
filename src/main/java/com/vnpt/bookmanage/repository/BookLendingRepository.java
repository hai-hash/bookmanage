package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.BookLending;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BookLending entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookLendingRepository extends JpaRepository<BookLending, Long>, JpaSpecificationExecutor<BookLending> {
    @Query("select bookLending from BookLending bookLending where bookLending.user.login = ?#{principal.username}")
    List<BookLending> findByUserIsCurrentUser();
}
