package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserType;
import com.mycompany.myapp.repository.UserTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserType}.
 */
@Service
@Transactional
public class UserTypeService {

    private final Logger log = LoggerFactory.getLogger(UserTypeService.class);

    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    /**
     * Save a userType.
     *
     * @param userType the entity to save.
     * @return the persisted entity.
     */
    public UserType save(UserType userType) {
        log.debug("Request to save UserType : {}", userType);
        return userTypeRepository.save(userType);
    }

    /**
     * Update a userType.
     *
     * @param userType the entity to save.
     * @return the persisted entity.
     */
    public UserType update(UserType userType) {
        log.debug("Request to update UserType : {}", userType);
        return userTypeRepository.save(userType);
    }

    /**
     * Partially update a userType.
     *
     * @param userType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserType> partialUpdate(UserType userType) {
        log.debug("Request to partially update UserType : {}", userType);

        return userTypeRepository
            .findById(userType.getId())
            .map(existingUserType -> {
                if (userType.getCode() != null) {
                    existingUserType.setCode(userType.getCode());
                }
                if (userType.getUserRole() != null) {
                    existingUserType.setUserRole(userType.getUserRole());
                }
                if (userType.getIsActive() != null) {
                    existingUserType.setIsActive(userType.getIsActive());
                }

                return existingUserType;
            })
            .map(userTypeRepository::save);
    }

    /**
     * Get all the userTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserType> findAll(Pageable pageable) {
        log.debug("Request to get all UserTypes");
        return userTypeRepository.findAll(pageable);
    }

    /**
     * Get one userType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserType> findOne(Long id) {
        log.debug("Request to get UserType : {}", id);
        return userTypeRepository.findById(id);
    }

    /**
     * Delete the userType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserType : {}", id);
        userTypeRepository.deleteById(id);
    }
}
