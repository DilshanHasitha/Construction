package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.RatingType;
import com.mycompany.myapp.repository.RatingTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RatingType}.
 */
@Service
@Transactional
public class RatingTypeService {

    private final Logger log = LoggerFactory.getLogger(RatingTypeService.class);

    private final RatingTypeRepository ratingTypeRepository;

    public RatingTypeService(RatingTypeRepository ratingTypeRepository) {
        this.ratingTypeRepository = ratingTypeRepository;
    }

    /**
     * Save a ratingType.
     *
     * @param ratingType the entity to save.
     * @return the persisted entity.
     */
    public RatingType save(RatingType ratingType) {
        log.debug("Request to save RatingType : {}", ratingType);
        return ratingTypeRepository.save(ratingType);
    }

    /**
     * Update a ratingType.
     *
     * @param ratingType the entity to save.
     * @return the persisted entity.
     */
    public RatingType update(RatingType ratingType) {
        log.debug("Request to update RatingType : {}", ratingType);
        return ratingTypeRepository.save(ratingType);
    }

    /**
     * Partially update a ratingType.
     *
     * @param ratingType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RatingType> partialUpdate(RatingType ratingType) {
        log.debug("Request to partially update RatingType : {}", ratingType);

        return ratingTypeRepository
            .findById(ratingType.getId())
            .map(existingRatingType -> {
                if (ratingType.getName() != null) {
                    existingRatingType.setName(ratingType.getName());
                }
                if (ratingType.getDescription() != null) {
                    existingRatingType.setDescription(ratingType.getDescription());
                }

                return existingRatingType;
            })
            .map(ratingTypeRepository::save);
    }

    /**
     * Get all the ratingTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RatingType> findAll(Pageable pageable) {
        log.debug("Request to get all RatingTypes");
        return ratingTypeRepository.findAll(pageable);
    }

    /**
     * Get one ratingType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RatingType> findOne(Long id) {
        log.debug("Request to get RatingType : {}", id);
        return ratingTypeRepository.findById(id);
    }

    /**
     * Delete the ratingType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RatingType : {}", id);
        ratingTypeRepository.deleteById(id);
    }
}
