package com.vnpt.bookmanage.repository;

import com.vnpt.bookmanage.domain.Rack;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RackRepository extends JpaRepository<Rack, Long>, JpaSpecificationExecutor<Rack> {
    @Query("select rack from Rack rack where rack.user.login = ?#{principal.username}")
    List<Rack> findByUserIsCurrentUser();
}
