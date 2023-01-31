package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RatingType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RatingType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingTypeRepository extends JpaRepository<RatingType, Long>, JpaSpecificationExecutor<RatingType> {}
