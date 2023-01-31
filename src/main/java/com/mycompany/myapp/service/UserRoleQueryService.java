package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.UserRole;
import com.mycompany.myapp.repository.UserRoleRepository;
import com.mycompany.myapp.service.criteria.UserRoleCriteria;
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
 * Service for executing complex queries for {@link UserRole} entities in the database.
 * The main input is a {@link UserRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserRole} or a {@link Page} of {@link UserRole} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserRoleQueryService extends QueryService<UserRole> {

    private final Logger log = LoggerFactory.getLogger(UserRoleQueryService.class);

    private final UserRoleRepository userRoleRepository;

    public UserRoleQueryService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Return a {@link List} of {@link UserRole} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserRole> findByCriteria(UserRoleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserRole> specification = createSpecification(criteria);
        return userRoleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserRole} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRole> findByCriteria(UserRoleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserRole> specification = createSpecification(criteria);
        return userRoleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserRoleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserRole> specification = createSpecification(criteria);
        return userRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link UserRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserRole> createSpecification(UserRoleCriteria criteria) {
        Specification<UserRole> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserRole_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), UserRole_.code));
            }
            if (criteria.getUserRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserRole(), UserRole_.userRole));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), UserRole_.isActive));
            }
            if (criteria.getUserPermissionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserPermissionId(),
                            root -> root.join(UserRole_.userPermissions, JoinType.LEFT).get(UserPermission_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
