package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.UserType;
import com.mycompany.myapp.repository.UserTypeRepository;
import com.mycompany.myapp.service.criteria.UserTypeCriteria;
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
 * Service for executing complex queries for {@link UserType} entities in the database.
 * The main input is a {@link UserTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserType} or a {@link Page} of {@link UserType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserTypeQueryService extends QueryService<UserType> {

    private final Logger log = LoggerFactory.getLogger(UserTypeQueryService.class);

    private final UserTypeRepository userTypeRepository;

    public UserTypeQueryService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    /**
     * Return a {@link List} of {@link UserType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserType> findByCriteria(UserTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserType> specification = createSpecification(criteria);
        return userTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserType> findByCriteria(UserTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserType> specification = createSpecification(criteria);
        return userTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserType> specification = createSpecification(criteria);
        return userTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link UserTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserType> createSpecification(UserTypeCriteria criteria) {
        Specification<UserType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserType_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), UserType_.code));
            }
            if (criteria.getUserRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserRole(), UserType_.userRole));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), UserType_.isActive));
            }
        }
        return specification;
    }
}
