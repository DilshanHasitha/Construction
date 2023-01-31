package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Certificate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Certificate entity.
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {
    default Optional<Certificate> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Certificate> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Certificate> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct certificate from Certificate certificate left join fetch certificate.certificateType",
        countQuery = "select count(distinct certificate) from Certificate certificate"
    )
    Page<Certificate> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct certificate from Certificate certificate left join fetch certificate.certificateType")
    List<Certificate> findAllWithToOneRelationships();

    @Query("select certificate from Certificate certificate left join fetch certificate.certificateType where certificate.id =:id")
    Optional<Certificate> findOneWithToOneRelationships(@Param("id") Long id);
}
