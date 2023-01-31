package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MasterItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MasterItem entity.
 */
@Repository
public interface MasterItemRepository extends JpaRepository<MasterItem, Long>, JpaSpecificationExecutor<MasterItem> {
    default Optional<MasterItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MasterItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MasterItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct masterItem from MasterItem masterItem left join fetch masterItem.exUser",
        countQuery = "select count(distinct masterItem) from MasterItem masterItem"
    )
    Page<MasterItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct masterItem from MasterItem masterItem left join fetch masterItem.exUser")
    List<MasterItem> findAllWithToOneRelationships();

    @Query("select masterItem from MasterItem masterItem left join fetch masterItem.exUser where masterItem.id =:id")
    Optional<MasterItem> findOneWithToOneRelationships(@Param("id") Long id);
}
