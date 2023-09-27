package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.service.dto.ExUserLoginDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExUser entity.
 */
@Repository
public interface ExUserRepository extends JpaRepository<ExUser, Long>, JpaSpecificationExecutor<ExUser> {
    default Optional<ExUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ExUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ExUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct exUser from ExUser exUser left join fetch exUser.userRole left join fetch exUser.company",
        countQuery = "select count(distinct exUser) from ExUser exUser"
    )
    Page<ExUser> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct exUser from ExUser exUser left join fetch exUser.userRole left join fetch exUser.company")
    List<ExUser> findAllWithToOneRelationships();

    @Query("select exUser from ExUser exUser left join fetch exUser.userRole left join fetch exUser.company where exUser.id =:id")
    Optional<ExUser> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "Select NEW com.mycompany.myapp.service.dto.ExUserLoginDTO (exu.id, exu.login, exu.userName, exu.firstName, exu.lastName, exu.email, exu.phone, c.brNumber, c.name, c.code, ut.code) FROM ExUser exu JOIN exu.user u JOIN exu.company c JOIN c.userType ut where u.id =:id"
    )
    Optional<ExUserLoginDTO> findUserWithDetails(@Param("id") Long id);
}
