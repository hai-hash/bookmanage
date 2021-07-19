package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.Catagory;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Catagory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatagoryRepository extends JpaRepository<Catagory, Long>, JpaSpecificationExecutor<Catagory> {
    @Query("select catagory from Catagory catagory where catagory.user.login = ?#{principal.username}")
    List<Catagory> findByUserIsCurrentUser();
}
