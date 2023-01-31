package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RatingType;
import com.mycompany.myapp.repository.RatingTypeRepository;
import com.mycompany.myapp.service.RatingTypeQueryService;
import com.mycompany.myapp.service.RatingTypeService;
import com.mycompany.myapp.service.criteria.RatingTypeCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.RatingType}.
 */
@RestController
@RequestMapping("/api")
public class RatingTypeResource {

    private final Logger log = LoggerFactory.getLogger(RatingTypeResource.class);

    private static final String ENTITY_NAME = "ratingType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RatingTypeService ratingTypeService;

    private final RatingTypeRepository ratingTypeRepository;

    private final RatingTypeQueryService ratingTypeQueryService;

    public RatingTypeResource(
        RatingTypeService ratingTypeService,
        RatingTypeRepository ratingTypeRepository,
        RatingTypeQueryService ratingTypeQueryService
    ) {
        this.ratingTypeService = ratingTypeService;
        this.ratingTypeRepository = ratingTypeRepository;
        this.ratingTypeQueryService = ratingTypeQueryService;
    }

    /**
     * {@code POST  /rating-types} : Create a new ratingType.
     *
     * @param ratingType the ratingType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ratingType, or with status {@code 400 (Bad Request)} if the ratingType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rating-types")
    public ResponseEntity<RatingType> createRatingType(@RequestBody RatingType ratingType) throws URISyntaxException {
        log.debug("REST request to save RatingType : {}", ratingType);
        if (ratingType.getId() != null) {
            throw new BadRequestAlertException("A new ratingType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RatingType result = ratingTypeService.save(ratingType);
        return ResponseEntity
            .created(new URI("/api/rating-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rating-types/:id} : Updates an existing ratingType.
     *
     * @param id the id of the ratingType to save.
     * @param ratingType the ratingType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ratingType,
     * or with status {@code 400 (Bad Request)} if the ratingType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ratingType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rating-types/{id}")
    public ResponseEntity<RatingType> updateRatingType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RatingType ratingType
    ) throws URISyntaxException {
        log.debug("REST request to update RatingType : {}, {}", id, ratingType);
        if (ratingType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ratingType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RatingType result = ratingTypeService.update(ratingType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ratingType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rating-types/:id} : Partial updates given fields of an existing ratingType, field will ignore if it is null
     *
     * @param id the id of the ratingType to save.
     * @param ratingType the ratingType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ratingType,
     * or with status {@code 400 (Bad Request)} if the ratingType is not valid,
     * or with status {@code 404 (Not Found)} if the ratingType is not found,
     * or with status {@code 500 (Internal Server Error)} if the ratingType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rating-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RatingType> partialUpdateRatingType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RatingType ratingType
    ) throws URISyntaxException {
        log.debug("REST request to partial update RatingType partially : {}, {}", id, ratingType);
        if (ratingType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ratingType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RatingType> result = ratingTypeService.partialUpdate(ratingType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ratingType.getId().toString())
        );
    }

    /**
     * {@code GET  /rating-types} : get all the ratingTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ratingTypes in body.
     */
    @GetMapping("/rating-types")
    public ResponseEntity<List<RatingType>> getAllRatingTypes(
        RatingTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RatingTypes by criteria: {}", criteria);
        Page<RatingType> page = ratingTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rating-types/count} : count all the ratingTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rating-types/count")
    public ResponseEntity<Long> countRatingTypes(RatingTypeCriteria criteria) {
        log.debug("REST request to count RatingTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(ratingTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rating-types/:id} : get the "id" ratingType.
     *
     * @param id the id of the ratingType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ratingType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rating-types/{id}")
    public ResponseEntity<RatingType> getRatingType(@PathVariable Long id) {
        log.debug("REST request to get RatingType : {}", id);
        Optional<RatingType> ratingType = ratingTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ratingType);
    }

    /**
     * {@code DELETE  /rating-types/:id} : delete the "id" ratingType.
     *
     * @param id the id of the ratingType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rating-types/{id}")
    public ResponseEntity<Void> deleteRatingType(@PathVariable Long id) {
        log.debug("REST request to delete RatingType : {}", id);
        ratingTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
