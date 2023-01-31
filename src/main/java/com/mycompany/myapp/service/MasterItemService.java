package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MasterItem;
import com.mycompany.myapp.repository.MasterItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MasterItem}.
 */
@Service
@Transactional
public class MasterItemService {

    private final Logger log = LoggerFactory.getLogger(MasterItemService.class);

    private final MasterItemRepository masterItemRepository;

    public MasterItemService(MasterItemRepository masterItemRepository) {
        this.masterItemRepository = masterItemRepository;
    }

    /**
     * Save a masterItem.
     *
     * @param masterItem the entity to save.
     * @return the persisted entity.
     */
    public MasterItem save(MasterItem masterItem) {
        log.debug("Request to save MasterItem : {}", masterItem);
        return masterItemRepository.save(masterItem);
    }

    /**
     * Update a masterItem.
     *
     * @param masterItem the entity to save.
     * @return the persisted entity.
     */
    public MasterItem update(MasterItem masterItem) {
        log.debug("Request to update MasterItem : {}", masterItem);
        return masterItemRepository.save(masterItem);
    }

    /**
     * Partially update a masterItem.
     *
     * @param masterItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MasterItem> partialUpdate(MasterItem masterItem) {
        log.debug("Request to partially update MasterItem : {}", masterItem);

        return masterItemRepository
            .findById(masterItem.getId())
            .map(existingMasterItem -> {
                if (masterItem.getCode() != null) {
                    existingMasterItem.setCode(masterItem.getCode());
                }
                if (masterItem.getName() != null) {
                    existingMasterItem.setName(masterItem.getName());
                }
                if (masterItem.getDescription() != null) {
                    existingMasterItem.setDescription(masterItem.getDescription());
                }
                if (masterItem.getIsActive() != null) {
                    existingMasterItem.setIsActive(masterItem.getIsActive());
                }

                return existingMasterItem;
            })
            .map(masterItemRepository::save);
    }

    /**
     * Get all the masterItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MasterItem> findAll(Pageable pageable) {
        log.debug("Request to get all MasterItems");
        return masterItemRepository.findAll(pageable);
    }

    /**
     * Get all the masterItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MasterItem> findAllWithEagerRelationships(Pageable pageable) {
        return masterItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one masterItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MasterItem> findOne(Long id) {
        log.debug("Request to get MasterItem : {}", id);
        return masterItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the masterItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MasterItem : {}", id);
        masterItemRepository.deleteById(id);
    }
}
