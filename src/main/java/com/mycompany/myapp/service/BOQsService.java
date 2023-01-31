package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BOQs;
import com.mycompany.myapp.repository.BOQsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BOQs}.
 */
@Service
@Transactional
public class BOQsService {

    private final Logger log = LoggerFactory.getLogger(BOQsService.class);

    private final BOQsRepository bOQsRepository;

    public BOQsService(BOQsRepository bOQsRepository) {
        this.bOQsRepository = bOQsRepository;
    }

    /**
     * Save a bOQs.
     *
     * @param bOQs the entity to save.
     * @return the persisted entity.
     */
    public BOQs save(BOQs bOQs) {
        log.debug("Request to save BOQs : {}", bOQs);
        return bOQsRepository.save(bOQs);
    }

    /**
     * Update a bOQs.
     *
     * @param bOQs the entity to save.
     * @return the persisted entity.
     */
    public BOQs update(BOQs bOQs) {
        log.debug("Request to update BOQs : {}", bOQs);
        return bOQsRepository.save(bOQs);
    }

    /**
     * Partially update a bOQs.
     *
     * @param bOQs the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BOQs> partialUpdate(BOQs bOQs) {
        log.debug("Request to partially update BOQs : {}", bOQs);

        return bOQsRepository
            .findById(bOQs.getId())
            .map(existingBOQs -> {
                if (bOQs.getCode() != null) {
                    existingBOQs.setCode(bOQs.getCode());
                }
                if (bOQs.getIsActive() != null) {
                    existingBOQs.setIsActive(bOQs.getIsActive());
                }

                return existingBOQs;
            })
            .map(bOQsRepository::save);
    }

    /**
     * Get all the bOQs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BOQs> findAll(Pageable pageable) {
        log.debug("Request to get all BOQs");
        return bOQsRepository.findAll(pageable);
    }

    /**
     * Get all the bOQs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BOQs> findAllWithEagerRelationships(Pageable pageable) {
        return bOQsRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one bOQs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BOQs> findOne(Long id) {
        log.debug("Request to get BOQs : {}", id);
        return bOQsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the bOQs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BOQs : {}", id);
        bOQsRepository.deleteById(id);
    }
}
