package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserRole;
import com.mycompany.myapp.repository.UserRoleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserRole}.
 */
@Service
@Transactional
public class UserRoleService {

    private final Logger log = LoggerFactory.getLogger(UserRoleService.class);

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Save a userRole.
     *
     * @param userRole the entity to save.
     * @return the persisted entity.
     */
    public UserRole save(UserRole userRole) {
        log.debug("Request to save UserRole : {}", userRole);
        return userRoleRepository.save(userRole);
    }

    /**
     * Update a userRole.
     *
     * @param userRole the entity to save.
     * @return the persisted entity.
     */
    public UserRole update(UserRole userRole) {
        log.debug("Request to update UserRole : {}", userRole);
        return userRoleRepository.save(userRole);
    }

    /**
     * Partially update a userRole.
     *
     * @param userRole the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserRole> partialUpdate(UserRole userRole) {
        log.debug("Request to partially update UserRole : {}", userRole);

        return userRoleRepository
            .findById(userRole.getId())
            .map(existingUserRole -> {
                if (userRole.getCode() != null) {
                    existingUserRole.setCode(userRole.getCode());
                }
                if (userRole.getUserRole() != null) {
                    existingUserRole.setUserRole(userRole.getUserRole());
                }
                if (userRole.getIsActive() != null) {
                    existingUserRole.setIsActive(userRole.getIsActive());
                }

                return existingUserRole;
            })
            .map(userRoleRepository::save);
    }

    /**
     * Get all the userRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRole> findAll(Pageable pageable) {
        log.debug("Request to get all UserRoles");
        return userRoleRepository.findAll(pageable);
    }

    /**
     * Get all the userRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserRole> findAllWithEagerRelationships(Pageable pageable) {
        return userRoleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one userRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserRole> findOne(Long id) {
        log.debug("Request to get UserRole : {}", id);
        return userRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the userRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserRole : {}", id);
        userRoleRepository.deleteById(id);
    }
}
