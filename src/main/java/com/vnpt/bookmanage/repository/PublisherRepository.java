package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.Publisher;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Publisher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>, JpaSpecificationExecutor<Publisher> {
    @Query("select publisher from Publisher publisher where publisher.user.login = ?#{principal.username}")
    List<Publisher> findByUserIsCurrentUser();
}
