package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.Reader;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long>, JpaSpecificationExecutor<Reader> {
    @Query("select reader from Reader reader where reader.user.login = ?#{principal.username}")
    List<Reader> findByUserIsCurrentUser();
}
