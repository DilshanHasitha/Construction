package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.UnitOfMeasure;
import com.mycompany.myapp.repository.UnitOfMeasureRepository;
import com.mycompany.myapp.service.criteria.UnitOfMeasureCriteria;
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
 * Service for executing complex queries for {@link UnitOfMeasure} entities in the database.
 * The main input is a {@link UnitOfMeasureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UnitOfMeasure} or a {@link Page} of {@link UnitOfMeasure} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UnitOfMeasureQueryService extends QueryService<UnitOfMeasure> {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureQueryService.class);

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureQueryService(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    /**
     * Return a {@link List} of {@link UnitOfMeasure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UnitOfMeasure> findByCriteria(UnitOfMeasureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UnitOfMeasure> specification = createSpecification(criteria);
        return unitOfMeasureRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UnitOfMeasure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitOfMeasure> findByCriteria(UnitOfMeasureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UnitOfMeasure> specification = createSpecification(criteria);
        return unitOfMeasureRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UnitOfMeasureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UnitOfMeasure> specification = createSpecification(criteria);
        return unitOfMeasureRepository.count(specification);
    }

    /**
     * Function to convert {@link UnitOfMeasureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UnitOfMeasure> createSpecification(UnitOfMeasureCriteria criteria) {
        Specification<UnitOfMeasure> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UnitOfMeasure_.id));
            }
            if (criteria.getUnitOfMeasureCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getUnitOfMeasureCode(), UnitOfMeasure_.unitOfMeasureCode));
            }
            if (criteria.getUnitOfMeasureDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getUnitOfMeasureDescription(), UnitOfMeasure_.unitOfMeasureDescription)
                    );
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), UnitOfMeasure_.isActive));
            }
        }
        return specification;
    }
}
