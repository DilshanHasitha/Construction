package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Location;
import com.mycompany.myapp.repository.LocationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Save a location.
     *
     * @param location the entity to save.
     * @return the persisted entity.
     */
    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        return locationRepository.save(location);
    }

    /**
     * Update a location.
     *
     * @param location the entity to save.
     * @return the persisted entity.
     */
    public Location update(Location location) {
        log.debug("Request to update Location : {}", location);
        return locationRepository.save(location);
    }

    /**
     * Partially update a location.
     *
     * @param location the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Location> partialUpdate(Location location) {
        log.debug("Request to partially update Location : {}", location);

        return locationRepository
            .findById(location.getId())
            .map(existingLocation -> {
                if (location.getCode() != null) {
                    existingLocation.setCode(location.getCode());
                }
                if (location.getCity() != null) {
                    existingLocation.setCity(location.getCity());
                }
                if (location.getCountry() != null) {
                    existingLocation.setCountry(location.getCountry());
                }
                if (location.getCountryCode() != null) {
                    existingLocation.setCountryCode(location.getCountryCode());
                }
                if (location.getLat() != null) {
                    existingLocation.setLat(location.getLat());
                }
                if (location.getLon() != null) {
                    existingLocation.setLon(location.getLon());
                }
                if (location.getIsActive() != null) {
                    existingLocation.setIsActive(location.getIsActive());
                }

                return existingLocation;
            })
            .map(locationRepository::save);
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Location> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable);
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
