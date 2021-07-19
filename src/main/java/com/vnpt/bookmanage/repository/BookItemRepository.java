package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.BookItem;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BookItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long>, JpaSpecificationExecutor<BookItem> {
    @Query("select bookItem from BookItem bookItem where bookItem.user.login = ?#{principal.username}")
    List<BookItem> findByUserIsCurrentUser();
}
