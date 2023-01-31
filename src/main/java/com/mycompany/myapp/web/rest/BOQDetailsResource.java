package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BOQDetails;
import com.mycompany.myapp.repository.BOQDetailsRepository;
import com.mycompany.myapp.service.BOQDetailsQueryService;
import com.mycompany.myapp.service.BOQDetailsService;
import com.mycompany.myapp.service.criteria.BOQDetailsCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BOQDetails}.
 */
@RestController
@RequestMapping("/api")
public class BOQDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BOQDetailsResource.class);

    private static final String ENTITY_NAME = "bOQDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BOQDetailsService bOQDetailsService;

    private final BOQDetailsRepository bOQDetailsRepository;

    private final BOQDetailsQueryService bOQDetailsQueryService;

    public BOQDetailsResource(
        BOQDetailsService bOQDetailsService,
        BOQDetailsRepository bOQDetailsRepository,
        BOQDetailsQueryService bOQDetailsQueryService
    ) {
        this.bOQDetailsService = bOQDetailsService;
        this.bOQDetailsRepository = bOQDetailsRepository;
        this.bOQDetailsQueryService = bOQDetailsQueryService;
    }

    /**
     * {@code POST  /boq-details} : Create a new bOQDetails.
     *
     * @param bOQDetails the bOQDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bOQDetails, or with status {@code 400 (Bad Request)} if the bOQDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boq-details")
    public ResponseEntity<BOQDetails> createBOQDetails(@Valid @RequestBody BOQDetails bOQDetails) throws URISyntaxException {
        log.debug("REST request to save BOQDetails : {}", bOQDetails);
        if (bOQDetails.getId() != null) {
            throw new BadRequestAlertException("A new bOQDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BOQDetails result = bOQDetailsService.save(bOQDetails);
        return ResponseEntity
            .created(new URI("/api/boq-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /boq-details/:id} : Updates an existing bOQDetails.
     *
     * @param id the id of the bOQDetails to save.
     * @param bOQDetails the bOQDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bOQDetails,
     * or with status {@code 400 (Bad Request)} if the bOQDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bOQDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boq-details/{id}")
    public ResponseEntity<BOQDetails> updateBOQDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BOQDetails bOQDetails
    ) throws URISyntaxException {
        log.debug("REST request to update BOQDetails : {}, {}", id, bOQDetails);
        if (bOQDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bOQDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bOQDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BOQDetails result = bOQDetailsService.update(bOQDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bOQDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /boq-details/:id} : Partial updates given fields of an existing bOQDetails, field will ignore if it is null
     *
     * @param id the id of the bOQDetails to save.
     * @param bOQDetails the bOQDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bOQDetails,
     * or with status {@code 400 (Bad Request)} if the bOQDetails is not valid,
     * or with status {@code 404 (Not Found)} if the bOQDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the bOQDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/boq-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BOQDetails> partialUpdateBOQDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BOQDetails bOQDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update BOQDetails partially : {}, {}", id, bOQDetails);
        if (bOQDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bOQDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bOQDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BOQDetails> result = bOQDetailsService.partialUpdate(bOQDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bOQDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /boq-details} : get all the bOQDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bOQDetails in body.
     */
    @GetMapping("/boq-details")
    public ResponseEntity<List<BOQDetails>> getAllBOQDetails(
        BOQDetailsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BOQDetails by criteria: {}", criteria);
        Page<BOQDetails> page = bOQDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /boq-details/count} : count all the bOQDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/boq-details/count")
    public ResponseEntity<Long> countBOQDetails(BOQDetailsCriteria criteria) {
        log.debug("REST request to count BOQDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(bOQDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /boq-details/:id} : get the "id" bOQDetails.
     *
     * @param id the id of the bOQDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bOQDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boq-details/{id}")
    public ResponseEntity<BOQDetails> getBOQDetails(@PathVariable Long id) {
        log.debug("REST request to get BOQDetails : {}", id);
        Optional<BOQDetails> bOQDetails = bOQDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bOQDetails);
    }

    /**
     * {@code DELETE  /boq-details/:id} : delete the "id" bOQDetails.
     *
     * @param id the id of the bOQDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boq-details/{id}")
    public ResponseEntity<Void> deleteBOQDetails(@PathVariable Long id) {
        log.debug("REST request to delete BOQDetails : {}", id);
        bOQDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
