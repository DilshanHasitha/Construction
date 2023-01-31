package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MasterItem;
import com.mycompany.myapp.repository.MasterItemRepository;
import com.mycompany.myapp.service.criteria.MasterItemCriteria;
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
 * Service for executing complex queries for {@link MasterItem} entities in the database.
 * The main input is a {@link MasterItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MasterItem} or a {@link Page} of {@link MasterItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MasterItemQueryService extends QueryService<MasterItem> {

    private final Logger log = LoggerFactory.getLogger(MasterItemQueryService.class);

    private final MasterItemRepository masterItemRepository;

    public MasterItemQueryService(MasterItemRepository masterItemRepository) {
        this.masterItemRepository = masterItemRepository;
    }

    /**
     * Return a {@link List} of {@link MasterItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MasterItem> findByCriteria(MasterItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MasterItem> specification = createSpecification(criteria);
        return masterItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MasterItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MasterItem> findByCriteria(MasterItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MasterItem> specification = createSpecification(criteria);
        return masterItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MasterItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MasterItem> specification = createSpecification(criteria);
        return masterItemRepository.count(specification);
    }

    /**
     * Function to convert {@link MasterItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MasterItem> createSpecification(MasterItemCriteria criteria) {
        Specification<MasterItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MasterItem_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), MasterItem_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MasterItem_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MasterItem_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), MasterItem_.isActive));
            }
            if (criteria.getExUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getExUserId(), root -> root.join(MasterItem_.exUser, JoinType.LEFT).get(ExUser_.id))
                    );
            }
        }
        return specification;
    }
}
