package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserPermission;
import com.mycompany.myapp.repository.UserPermissionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserPermission}.
 */
@Service
@Transactional
public class UserPermissionService {

    private final Logger log = LoggerFactory.getLogger(UserPermissionService.class);

    private final UserPermissionRepository userPermissionRepository;

    public UserPermissionService(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    /**
     * Save a userPermission.
     *
     * @param userPermission the entity to save.
     * @return the persisted entity.
     */
    public UserPermission save(UserPermission userPermission) {
        log.debug("Request to save UserPermission : {}", userPermission);
        return userPermissionRepository.save(userPermission);
    }

    /**
     * Update a userPermission.
     *
     * @param userPermission the entity to save.
     * @return the persisted entity.
     */
    public UserPermission update(UserPermission userPermission) {
        log.debug("Request to update UserPermission : {}", userPermission);
        return userPermissionRepository.save(userPermission);
    }

    /**
     * Partially update a userPermission.
     *
     * @param userPermission the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserPermission> partialUpdate(UserPermission userPermission) {
        log.debug("Request to partially update UserPermission : {}", userPermission);

        return userPermissionRepository
            .findById(userPermission.getId())
            .map(existingUserPermission -> {
                if (userPermission.getAction() != null) {
                    existingUserPermission.setAction(userPermission.getAction());
                }
                if (userPermission.getDocument() != null) {
                    existingUserPermission.setDocument(userPermission.getDocument());
                }
                if (userPermission.getDescription() != null) {
                    existingUserPermission.setDescription(userPermission.getDescription());
                }

                return existingUserPermission;
            })
            .map(userPermissionRepository::save);
    }

    /**
     * Get all the userPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserPermission> findAll(Pageable pageable) {
        log.debug("Request to get all UserPermissions");
        return userPermissionRepository.findAll(pageable);
    }

    /**
     * Get one userPermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserPermission> findOne(Long id) {
        log.debug("Request to get UserPermission : {}", id);
        return userPermissionRepository.findById(id);
    }

    /**
     * Delete the userPermission by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserPermission : {}", id);
        userPermissionRepository.deleteById(id);
    }
}
