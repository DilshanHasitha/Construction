package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.BOQs;
import com.mycompany.myapp.repository.BOQsRepository;
import com.mycompany.myapp.service.criteria.BOQsCriteria;
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
 * Service for executing complex queries for {@link BOQs} entities in the database.
 * The main input is a {@link BOQsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BOQs} or a {@link Page} of {@link BOQs} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BOQsQueryService extends QueryService<BOQs> {

    private final Logger log = LoggerFactory.getLogger(BOQsQueryService.class);

    private final BOQsRepository bOQsRepository;

    public BOQsQueryService(BOQsRepository bOQsRepository) {
        this.bOQsRepository = bOQsRepository;
    }

    /**
     * Return a {@link List} of {@link BOQs} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BOQs> findByCriteria(BOQsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BOQs> specification = createSpecification(criteria);
        return bOQsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link BOQs} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BOQs> findByCriteria(BOQsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BOQs> specification = createSpecification(criteria);
        return bOQsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BOQsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BOQs> specification = createSpecification(criteria);
        return bOQsRepository.count(specification);
    }

    /**
     * Function to convert {@link BOQsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BOQs> createSpecification(BOQsCriteria criteria) {
        Specification<BOQs> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BOQs_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), BOQs_.code));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), BOQs_.isActive));
            }
            if (criteria.getConstructorsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConstructorsId(),
                            root -> root.join(BOQs_.constructors, JoinType.LEFT).get(ExUser_.id)
                        )
                    );
            }
            if (criteria.getBoqDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBoqDetailsId(),
                            root -> root.join(BOQs_.boqDetails, JoinType.LEFT).get(BOQDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
