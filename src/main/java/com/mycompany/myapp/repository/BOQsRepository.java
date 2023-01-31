package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BOQs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BOQs entity.
 *
 * When extending this class, extend BOQsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface BOQsRepository extends BOQsRepositoryWithBagRelationships, JpaRepository<BOQs, Long>, JpaSpecificationExecutor<BOQs> {
    default Optional<BOQs> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<BOQs> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<BOQs> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct bOQs from BOQs bOQs left join fetch bOQs.constructors",
        countQuery = "select count(distinct bOQs) from BOQs bOQs"
    )
    Page<BOQs> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct bOQs from BOQs bOQs left join fetch bOQs.constructors")
    List<BOQs> findAllWithToOneRelationships();

    @Query("select bOQs from BOQs bOQs left join fetch bOQs.constructors where bOQs.id =:id")
    Optional<BOQs> findOneWithToOneRelationships(@Param("id") Long id);
}
