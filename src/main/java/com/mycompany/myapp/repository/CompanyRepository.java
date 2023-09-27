package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Company;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Company entity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    default Optional<Company> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Company> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Company> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct company from Company company left join fetch company.userType",
        countQuery = "select count(distinct company) from Company company"
    )
    Page<Company> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct company from Company company left join fetch company.userType")
    List<Company> findAllWithToOneRelationships();

    @Query("select company from Company company left join fetch company.userType where company.id =:id")
    Optional<Company> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<Company> findByCode(String code);

    Page<Company> findByUserTypeCode(String code, Pageable pageable);
}
