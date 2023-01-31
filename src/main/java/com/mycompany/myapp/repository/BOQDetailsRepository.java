package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BOQDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BOQDetails entity.
 */
@Repository
public interface BOQDetailsRepository extends JpaRepository<BOQDetails, Long>, JpaSpecificationExecutor<BOQDetails> {
    default Optional<BOQDetails> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BOQDetails> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BOQDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct bOQDetails from BOQDetails bOQDetails left join fetch bOQDetails.item left join fetch bOQDetails.per left join fetch bOQDetails.unit",
        countQuery = "select count(distinct bOQDetails) from BOQDetails bOQDetails"
    )
    Page<BOQDetails> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct bOQDetails from BOQDetails bOQDetails left join fetch bOQDetails.item left join fetch bOQDetails.per left join fetch bOQDetails.unit"
    )
    List<BOQDetails> findAllWithToOneRelationships();

    @Query(
        "select bOQDetails from BOQDetails bOQDetails left join fetch bOQDetails.item left join fetch bOQDetails.per left join fetch bOQDetails.unit where bOQDetails.id =:id"
    )
    Optional<BOQDetails> findOneWithToOneRelationships(@Param("id") Long id);
}
