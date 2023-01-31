package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UnitOfMeasure;
import com.mycompany.myapp.repository.UnitOfMeasureRepository;
import com.mycompany.myapp.service.UnitOfMeasureQueryService;
import com.mycompany.myapp.service.UnitOfMeasureService;
import com.mycompany.myapp.service.criteria.UnitOfMeasureCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UnitOfMeasure}.
 */
@RestController
@RequestMapping("/api")
public class UnitOfMeasureResource {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasureResource.class);

    private static final String ENTITY_NAME = "unitOfMeasure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitOfMeasureService unitOfMeasureService;

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final UnitOfMeasureQueryService unitOfMeasureQueryService;

    public UnitOfMeasureResource(
        UnitOfMeasureService unitOfMeasureService,
        UnitOfMeasureRepository unitOfMeasureRepository,
        UnitOfMeasureQueryService unitOfMeasureQueryService
    ) {
        this.unitOfMeasureService = unitOfMeasureService;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureQueryService = unitOfMeasureQueryService;
    }

    /**
     * {@code POST  /unit-of-measures} : Create a new unitOfMeasure.
     *
     * @param unitOfMeasure the unitOfMeasure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitOfMeasure, or with status {@code 400 (Bad Request)} if the unitOfMeasure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-of-measures")
    public ResponseEntity<UnitOfMeasure> createUnitOfMeasure(@Valid @RequestBody UnitOfMeasure unitOfMeasure) throws URISyntaxException {
        log.debug("REST request to save UnitOfMeasure : {}", unitOfMeasure);
        if (unitOfMeasure.getId() != null) {
            throw new BadRequestAlertException("A new unitOfMeasure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitOfMeasure result = unitOfMeasureService.save(unitOfMeasure);
        return ResponseEntity
            .created(new URI("/api/unit-of-measures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-of-measures/:id} : Updates an existing unitOfMeasure.
     *
     * @param id the id of the unitOfMeasure to save.
     * @param unitOfMeasure the unitOfMeasure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasure,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-of-measures/{id}")
    public ResponseEntity<UnitOfMeasure> updateUnitOfMeasure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitOfMeasure unitOfMeasure
    ) throws URISyntaxException {
        log.debug("REST request to update UnitOfMeasure : {}, {}", id, unitOfMeasure);
        if (unitOfMeasure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitOfMeasure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitOfMeasureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitOfMeasure result = unitOfMeasureService.update(unitOfMeasure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unitOfMeasure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unit-of-measures/:id} : Partial updates given fields of an existing unitOfMeasure, field will ignore if it is null
     *
     * @param id the id of the unitOfMeasure to save.
     * @param unitOfMeasure the unitOfMeasure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitOfMeasure,
     * or with status {@code 400 (Bad Request)} if the unitOfMeasure is not valid,
     * or with status {@code 404 (Not Found)} if the unitOfMeasure is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitOfMeasure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-of-measures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitOfMeasure> partialUpdateUnitOfMeasure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitOfMeasure unitOfMeasure
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitOfMeasure partially : {}, {}", id, unitOfMeasure);
        if (unitOfMeasure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitOfMeasure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitOfMeasureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitOfMeasure> result = unitOfMeasureService.partialUpdate(unitOfMeasure);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unitOfMeasure.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-of-measures} : get all the unitOfMeasures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitOfMeasures in body.
     */
    @GetMapping("/unit-of-measures")
    public ResponseEntity<List<UnitOfMeasure>> getAllUnitOfMeasures(
        UnitOfMeasureCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UnitOfMeasures by criteria: {}", criteria);
        Page<UnitOfMeasure> page = unitOfMeasureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-of-measures/count} : count all the unitOfMeasures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/unit-of-measures/count")
    public ResponseEntity<Long> countUnitOfMeasures(UnitOfMeasureCriteria criteria) {
        log.debug("REST request to count UnitOfMeasures by criteria: {}", criteria);
        return ResponseEntity.ok().body(unitOfMeasureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /unit-of-measures/:id} : get the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitOfMeasure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-of-measures/{id}")
    public ResponseEntity<UnitOfMeasure> getUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to get UnitOfMeasure : {}", id);
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitOfMeasure);
    }

    /**
     * {@code DELETE  /unit-of-measures/:id} : delete the "id" unitOfMeasure.
     *
     * @param id the id of the unitOfMeasure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-of-measures/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasure(@PathVariable Long id) {
        log.debug("REST request to delete UnitOfMeasure : {}", id);
        unitOfMeasureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
