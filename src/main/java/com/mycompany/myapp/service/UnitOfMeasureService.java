package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UnitOfMeasure;
import com.mycompany.myapp.repository.UnitOfMeasureRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UnitOfMeasure}.
 */
@Service
@Transactional
public class UnitOfMeasureService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureService.class);

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureService(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    /**
     * Save a unitOfMeasure.
     *
     * @param unitOfMeasure the entity to save.
     * @return the persisted entity.
     */
    public UnitOfMeasure save(UnitOfMeasure unitOfMeasure) {
        log.debug("Request to save UnitOfMeasure : {}", unitOfMeasure);
        return unitOfMeasureRepository.save(unitOfMeasure);
    }

    /**
     * Update a unitOfMeasure.
     *
     * @param unitOfMeasure the entity to save.
     * @return the persisted entity.
     */
    public UnitOfMeasure update(UnitOfMeasure unitOfMeasure) {
        log.debug("Request to update UnitOfMeasure : {}", unitOfMeasure);
        return unitOfMeasureRepository.save(unitOfMeasure);
    }

    /**
     * Partially update a unitOfMeasure.
     *
     * @param unitOfMeasure the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UnitOfMeasure> partialUpdate(UnitOfMeasure unitOfMeasure) {
        log.debug("Request to partially update UnitOfMeasure : {}", unitOfMeasure);

        return unitOfMeasureRepository
            .findById(unitOfMeasure.getId())
            .map(existingUnitOfMeasure -> {
                if (unitOfMeasure.getUnitOfMeasureCode() != null) {
                    existingUnitOfMeasure.setUnitOfMeasureCode(unitOfMeasure.getUnitOfMeasureCode());
                }
                if (unitOfMeasure.getUnitOfMeasureDescription() != null) {
                    existingUnitOfMeasure.setUnitOfMeasureDescription(unitOfMeasure.getUnitOfMeasureDescription());
                }
                if (unitOfMeasure.getIsActive() != null) {
                    existingUnitOfMeasure.setIsActive(unitOfMeasure.getIsActive());
                }

                return existingUnitOfMeasure;
            })
            .map(unitOfMeasureRepository::save);
    }

    /**
     * Get all the unitOfMeasures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitOfMeasure> findAll(Pageable pageable) {
        log.debug("Request to get all UnitOfMeasures");
        return unitOfMeasureRepository.findAll(pageable);
    }

    /**
     * Get one unitOfMeasure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UnitOfMeasure> findOne(Long id) {
        log.debug("Request to get UnitOfMeasure : {}", id);
        return unitOfMeasureRepository.findById(id);
    }

    /**
     * Delete the unitOfMeasure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UnitOfMeasure : {}", id);
        unitOfMeasureRepository.deleteById(id);
    }
}
