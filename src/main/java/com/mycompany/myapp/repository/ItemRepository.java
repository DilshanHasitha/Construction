package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Item entity.
 *
 * When extending this class, extend ItemRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ItemRepository extends ItemRepositoryWithBagRelationships, JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    default Optional<Item> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Item> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Item> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct item from Item item left join fetch item.masterItem left join fetch item.unit left join fetch item.exUser",
        countQuery = "select count(distinct item) from Item item"
    )
    Page<Item> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct item from Item item left join fetch item.masterItem left join fetch item.unit left join fetch item.exUser")
    List<Item> findAllWithToOneRelationships();

    @Query(
        "select item from Item item left join fetch item.masterItem left join fetch item.unit left join fetch item.exUser where item.id =:id"
    )
    Optional<Item> findOneWithToOneRelationships(@Param("id") Long id);
}
