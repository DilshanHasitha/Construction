package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.RatingType;
import com.mycompany.myapp.repository.RatingTypeRepository;
import com.mycompany.myapp.service.criteria.RatingTypeCriteria;
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
 * Service for executing complex queries for {@link RatingType} entities in the database.
 * The main input is a {@link RatingTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RatingType} or a {@link Page} of {@link RatingType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RatingTypeQueryService extends QueryService<RatingType> {

    private final Logger log = LoggerFactory.getLogger(RatingTypeQueryService.class);

    private final RatingTypeRepository ratingTypeRepository;

    public RatingTypeQueryService(RatingTypeRepository ratingTypeRepository) {
        this.ratingTypeRepository = ratingTypeRepository;
    }

    /**
     * Return a {@link List} of {@link RatingType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RatingType> findByCriteria(RatingTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RatingType> specification = createSpecification(criteria);
        return ratingTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RatingType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RatingType> findByCriteria(RatingTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RatingType> specification = createSpecification(criteria);
        return ratingTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RatingTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RatingType> specification = createSpecification(criteria);
        return ratingTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link RatingTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RatingType> createSpecification(RatingTypeCriteria criteria) {
        Specification<RatingType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RatingType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RatingType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDescription(), RatingType_.description));
            }
        }
        return specification;
    }
}
