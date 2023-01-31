package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.UserPermission;
import com.mycompany.myapp.repository.UserPermissionRepository;
import com.mycompany.myapp.service.criteria.UserPermissionCriteria;
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
 * Service for executing complex queries for {@link UserPermission} entities in the database.
 * The main input is a {@link UserPermissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserPermission} or a {@link Page} of {@link UserPermission} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserPermissionQueryService extends QueryService<UserPermission> {

    private final Logger log = LoggerFactory.getLogger(UserPermissionQueryService.class);

    private final UserPermissionRepository userPermissionRepository;

    public UserPermissionQueryService(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    /**
     * Return a {@link List} of {@link UserPermission} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserPermission> findByCriteria(UserPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserPermission> specification = createSpecification(criteria);
        return userPermissionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserPermission} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPermission> findByCriteria(UserPermissionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserPermission> specification = createSpecification(criteria);
        return userPermissionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserPermission> specification = createSpecification(criteria);
        return userPermissionRepository.count(specification);
    }

    /**
     * Function to convert {@link UserPermissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserPermission> createSpecification(UserPermissionCriteria criteria) {
        Specification<UserPermission> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserPermission_.id));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAction(), UserPermission_.action));
            }
            if (criteria.getDocument() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocument(), UserPermission_.document));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), UserPermission_.description));
            }
            if (criteria.getUserRoleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserRoleId(),
                            root -> root.join(UserPermission_.userRoles, JoinType.LEFT).get(UserRole_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
