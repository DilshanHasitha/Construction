package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.BOQDetails;
import com.mycompany.myapp.repository.BOQDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BOQDetails}.
 */
@Service
@Transactional
public class BOQDetailsService {

    private final Logger log = LoggerFactory.getLogger(BOQDetailsService.class);

    private final BOQDetailsRepository bOQDetailsRepository;

    public BOQDetailsService(BOQDetailsRepository bOQDetailsRepository) {
        this.bOQDetailsRepository = bOQDetailsRepository;
    }

    /**
     * Save a bOQDetails.
     *
     * @param bOQDetails the entity to save.
     * @return the persisted entity.
     */
    public BOQDetails save(BOQDetails bOQDetails) {
        log.debug("Request to save BOQDetails : {}", bOQDetails);
        return bOQDetailsRepository.save(bOQDetails);
    }

    /**
     * Update a bOQDetails.
     *
     * @param bOQDetails the entity to save.
     * @return the persisted entity.
     */
    public BOQDetails update(BOQDetails bOQDetails) {
        log.debug("Request to update BOQDetails : {}", bOQDetails);
        return bOQDetailsRepository.save(bOQDetails);
    }

    /**
     * Partially update a bOQDetails.
     *
     * @param bOQDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BOQDetails> partialUpdate(BOQDetails bOQDetails) {
        log.debug("Request to partially update BOQDetails : {}", bOQDetails);

        return bOQDetailsRepository
            .findById(bOQDetails.getId())
            .map(existingBOQDetails -> {
                if (bOQDetails.getCode() != null) {
                    existingBOQDetails.setCode(bOQDetails.getCode());
                }
                if (bOQDetails.getOrderPlacedOn() != null) {
                    existingBOQDetails.setOrderPlacedOn(bOQDetails.getOrderPlacedOn());
                }
                if (bOQDetails.getQty() != null) {
                    existingBOQDetails.setQty(bOQDetails.getQty());
                }
                if (bOQDetails.getIsActive() != null) {
                    existingBOQDetails.setIsActive(bOQDetails.getIsActive());
                }

                return existingBOQDetails;
            })
            .map(bOQDetailsRepository::save);
    }

    /**
     * Get all the bOQDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BOQDetails> findAll(Pageable pageable) {
        log.debug("Request to get all BOQDetails");
        return bOQDetailsRepository.findAll(pageable);
    }

    /**
     * Get all the bOQDetails with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BOQDetails> findAllWithEagerRelationships(Pageable pageable) {
        return bOQDetailsRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one bOQDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BOQDetails> findOne(Long id) {
        log.debug("Request to get BOQDetails : {}", id);
        return bOQDetailsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the bOQDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BOQDetails : {}", id);
        bOQDetailsRepository.deleteById(id);
    }
}
