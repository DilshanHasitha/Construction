package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BOQs;
import com.mycompany.myapp.repository.BOQsRepository;
import com.mycompany.myapp.service.BOQsQueryService;
import com.mycompany.myapp.service.BOQsService;
import com.mycompany.myapp.service.criteria.BOQsCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BOQs}.
 */
@RestController
@RequestMapping("/api")
public class BOQsResource {

    private final Logger log = LoggerFactory.getLogger(BOQsResource.class);

    private static final String ENTITY_NAME = "bOQs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BOQsService bOQsService;

    private final BOQsRepository bOQsRepository;

    private final BOQsQueryService bOQsQueryService;

    public BOQsResource(BOQsService bOQsService, BOQsRepository bOQsRepository, BOQsQueryService bOQsQueryService) {
        this.bOQsService = bOQsService;
        this.bOQsRepository = bOQsRepository;
        this.bOQsQueryService = bOQsQueryService;
    }

    /**
     * {@code POST  /bo-qs} : Create a new bOQs.
     *
     * @param bOQs the bOQs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bOQs, or with status {@code 400 (Bad Request)} if the bOQs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bo-qs")
    public ResponseEntity<BOQs> createBOQs(@RequestBody BOQs bOQs) throws URISyntaxException {
        log.debug("REST request to save BOQs : {}", bOQs);
        if (bOQs.getId() != null) {
            throw new BadRequestAlertException("A new bOQs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BOQs result = bOQsService.save(bOQs);
        return ResponseEntity
            .created(new URI("/api/bo-qs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bo-qs/:id} : Updates an existing bOQs.
     *
     * @param id the id of the bOQs to save.
     * @param bOQs the bOQs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bOQs,
     * or with status {@code 400 (Bad Request)} if the bOQs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bOQs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bo-qs/{id}")
    public ResponseEntity<BOQs> updateBOQs(@PathVariable(value = "id", required = false) final Long id, @RequestBody BOQs bOQs)
        throws URISyntaxException {
        log.debug("REST request to update BOQs : {}, {}", id, bOQs);
        if (bOQs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bOQs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bOQsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BOQs result = bOQsService.update(bOQs);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bOQs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bo-qs/:id} : Partial updates given fields of an existing bOQs, field will ignore if it is null
     *
     * @param id the id of the bOQs to save.
     * @param bOQs the bOQs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bOQs,
     * or with status {@code 400 (Bad Request)} if the bOQs is not valid,
     * or with status {@code 404 (Not Found)} if the bOQs is not found,
     * or with status {@code 500 (Internal Server Error)} if the bOQs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bo-qs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BOQs> partialUpdateBOQs(@PathVariable(value = "id", required = false) final Long id, @RequestBody BOQs bOQs)
        throws URISyntaxException {
        log.debug("REST request to partial update BOQs partially : {}, {}", id, bOQs);
        if (bOQs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bOQs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bOQsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BOQs> result = bOQsService.partialUpdate(bOQs);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bOQs.getId().toString())
        );
    }

    /**
     * {@code GET  /bo-qs} : get all the bOQs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bOQs in body.
     */
    @GetMapping("/bo-qs")
    public ResponseEntity<List<BOQs>> getAllBOQs(BOQsCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get BOQs by criteria: {}", criteria);
        Page<BOQs> page = bOQsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bo-qs/count} : count all the bOQs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bo-qs/count")
    public ResponseEntity<Long> countBOQs(BOQsCriteria criteria) {
        log.debug("REST request to count BOQs by criteria: {}", criteria);
        return ResponseEntity.ok().body(bOQsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bo-qs/:id} : get the "id" bOQs.
     *
     * @param id the id of the bOQs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bOQs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bo-qs/{id}")
    public ResponseEntity<BOQs> getBOQs(@PathVariable Long id) {
        log.debug("REST request to get BOQs : {}", id);
        Optional<BOQs> bOQs = bOQsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bOQs);
    }

    /**
     * {@code DELETE  /bo-qs/:id} : delete the "id" bOQs.
     *
     * @param id the id of the bOQs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bo-qs/{id}")
    public ResponseEntity<Void> deleteBOQs(@PathVariable Long id) {
        log.debug("REST request to delete BOQs : {}", id);
        bOQsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
