package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Rating;
import com.mycompany.myapp.repository.RatingRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rating}.
 */
@Service
@Transactional
public class RatingService {

    private final Logger log = LoggerFactory.getLogger(RatingService.class);

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Save a rating.
     *
     * @param rating the entity to save.
     * @return the persisted entity.
     */
    public Rating save(Rating rating) {
        log.debug("Request to save Rating : {}", rating);
        return ratingRepository.save(rating);
    }

    /**
     * Update a rating.
     *
     * @param rating the entity to save.
     * @return the persisted entity.
     */
    public Rating update(Rating rating) {
        log.debug("Request to update Rating : {}", rating);
        return ratingRepository.save(rating);
    }

    /**
     * Partially update a rating.
     *
     * @param rating the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rating> partialUpdate(Rating rating) {
        log.debug("Request to partially update Rating : {}", rating);

        return ratingRepository
            .findById(rating.getId())
            .map(existingRating -> {
                if (rating.getName() != null) {
                    existingRating.setName(rating.getName());
                }
                if (rating.getRateValue() != null) {
                    existingRating.setRateValue(rating.getRateValue());
                }

                return existingRating;
            })
            .map(ratingRepository::save);
    }

    /**
     * Get all the ratings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rating> findAll(Pageable pageable) {
        log.debug("Request to get all Ratings");
        return ratingRepository.findAll(pageable);
    }

    /**
     * Get one rating by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rating> findOne(Long id) {
        log.debug("Request to get Rating : {}", id);
        return ratingRepository.findById(id);
    }

    /**
     * Delete the rating by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
    }
}
