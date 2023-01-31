package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long>, JpaSpecificationExecutor<UserType> {}
