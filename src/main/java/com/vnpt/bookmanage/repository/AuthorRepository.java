package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.Author;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Author entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    @Query("select author from Author author where author.user.login = ?#{principal.username}")
    List<Author> findByUserIsCurrentUser();
}
