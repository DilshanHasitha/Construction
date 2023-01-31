package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BOQDetails;
import com.mycompany.myapp.repository.BOQDetailsRepository;
import com.mycompany.myapp.service.criteria.BOQDetailsCriteria;
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
 * Service for executing complex queries for {@link BOQDetails} entities in the database.
 * The main input is a {@link BOQDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BOQDetails} or a {@link Page} of {@link BOQDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BOQDetailsQueryService extends QueryService<BOQDetails> {

    private final Logger log = LoggerFactory.getLogger(BOQDetailsQueryService.class);

    private final BOQDetailsRepository bOQDetailsRepository;

    public BOQDetailsQueryService(BOQDetailsRepository bOQDetailsRepository) {
        this.bOQDetailsRepository = bOQDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link BOQDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BOQDetails> findByCriteria(BOQDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BOQDetails> specification = createSpecification(criteria);
        return bOQDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BOQDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BOQDetails> findByCriteria(BOQDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BOQDetails> specification = createSpecification(criteria);
        return bOQDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BOQDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BOQDetails> specification = createSpecification(criteria);
        return bOQDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link BOQDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BOQDetails> createSpecification(BOQDetailsCriteria criteria) {
        Specification<BOQDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BOQDetails_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), BOQDetails_.code));
            }
            if (criteria.getOrderPlacedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderPlacedOn(), BOQDetails_.orderPlacedOn));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), BOQDetails_.qty));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BOQDetails_.isActive));
            }
            if (criteria.getItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getItemId(), root -> root.join(BOQDetails_.item, JoinType.LEFT).get(MasterItem_.id))
                    );
            }
            if (criteria.getPerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPerId(), root -> root.join(BOQDetails_.per, JoinType.LEFT).get(UnitOfMeasure_.id))
                    );
            }
            if (criteria.getUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUnitId(), root -> root.join(BOQDetails_.unit, JoinType.LEFT).get(UnitOfMeasure_.id))
                    );
            }
            if (criteria.getBoqsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBoqsId(), root -> root.join(BOQDetails_.boqs, JoinType.LEFT).get(BOQs_.id))
                    );
            }
        }
        return specification;
    }
}
