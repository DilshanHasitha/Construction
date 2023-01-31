package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Item;
import com.mycompany.myapp.repository.ItemRepository;
import com.mycompany.myapp.service.criteria.ItemCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Item} entities in the database.
 * The main input is a {@link ItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Item} or a {@link Page} of {@link Item} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemQueryService extends QueryService<Item> {

    private final Logger log = LoggerFactory.getLogger(ItemQueryService.class);

    private final ItemRepository itemRepository;

    public ItemQueryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Return a {@link List} of {@link Item} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Item> findByCriteria(ItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Item} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Item> findByCriteria(ItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Item> createSpecification(ItemCriteria criteria) {
        Specification<Item> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Item_.id));
            }
            if (criteria.getItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemPrice(), Item_.itemPrice));
            }
            if (criteria.getItemCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemCost(), Item_.itemCost));
            }
            if (criteria.getBannerText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBannerText(), Item_.bannerText));
            }
            if (criteria.getSpecialPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpecialPrice(), Item_.specialPrice));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Item_.isActive));
            }
            if (criteria.getMinQTY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinQTY(), Item_.minQTY));
            }
            if (criteria.getMaxQTY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxQTY(), Item_.maxQTY));
            }
            if (criteria.getSteps() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSteps(), Item_.steps));
            }
            if (criteria.getLongDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongDescription(), Item_.longDescription));
            }
            if (criteria.getLeadTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeadTime(), Item_.leadTime));
            }
            if (criteria.getReorderQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReorderQty(), Item_.reorderQty));
            }
            if (criteria.getItemBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemBarcode(), Item_.itemBarcode));
            }
            if (criteria.getMasterItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMasterItemId(),
                            root -> root.join(Item_.masterItem, JoinType.LEFT).get(MasterItem_.id)
                        )
                    );
            }
            if (criteria.getUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUnitId(), root -> root.join(Item_.unit, JoinType.LEFT).get(UnitOfMeasure_.id))
                    );
            }
            if (criteria.getExUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getExUserId(), root -> root.join(Item_.exUser, JoinType.LEFT).get(ExUser_.id))
                    );
            }
            if (criteria.getRatingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRatingId(), root -> root.join(Item_.ratings, JoinType.LEFT).get(Rating_.id))
                    );
            }
            if (criteria.getCertificateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCertificateId(),
                            root -> root.join(Item_.certificates, JoinType.LEFT).get(Certificate_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
