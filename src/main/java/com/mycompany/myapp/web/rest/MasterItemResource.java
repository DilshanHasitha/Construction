package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MasterItem;
import com.mycompany.myapp.repository.MasterItemRepository;
import com.mycompany.myapp.service.MasterItemQueryService;
import com.mycompany.myapp.service.MasterItemService;
import com.mycompany.myapp.service.criteria.MasterItemCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MasterItem}.
 */
@RestController
@RequestMapping("/api")
public class MasterItemResource {

    private final Logger log = LoggerFactory.getLogger(MasterItemResource.class);

    private static final String ENTITY_NAME = "masterItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MasterItemService masterItemService;

    private final MasterItemRepository masterItemRepository;

    private final MasterItemQueryService masterItemQueryService;

    public MasterItemResource(
        MasterItemService masterItemService,
        MasterItemRepository masterItemRepository,
        MasterItemQueryService masterItemQueryService
    ) {
        this.masterItemService = masterItemService;
        this.masterItemRepository = masterItemRepository;
        this.masterItemQueryService = masterItemQueryService;
    }

    /**
     * {@code POST  /master-items} : Create a new masterItem.
     *
     * @param masterItem the masterItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterItem, or with status {@code 400 (Bad Request)} if the masterItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/master-items")
    public ResponseEntity<MasterItem> createMasterItem(@Valid @RequestBody MasterItem masterItem) throws URISyntaxException {
        log.debug("REST request to save MasterItem : {}", masterItem);
        if (masterItem.getId() != null) {
            throw new BadRequestAlertException("A new masterItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MasterItem result = masterItemService.save(masterItem);
        return ResponseEntity
            .created(new URI("/api/master-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /master-items/:id} : Updates an existing masterItem.
     *
     * @param id the id of the masterItem to save.
     * @param masterItem the masterItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterItem,
     * or with status {@code 400 (Bad Request)} if the masterItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/master-items/{id}")
    public ResponseEntity<MasterItem> updateMasterItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MasterItem masterItem
    ) throws URISyntaxException {
        log.debug("REST request to update MasterItem : {}, {}", id, masterItem);
        if (masterItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MasterItem result = masterItemService.update(masterItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, masterItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /master-items/:id} : Partial updates given fields of an existing masterItem, field will ignore if it is null
     *
     * @param id the id of the masterItem to save.
     * @param masterItem the masterItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterItem,
     * or with status {@code 400 (Bad Request)} if the masterItem is not valid,
     * or with status {@code 404 (Not Found)} if the masterItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/master-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MasterItem> partialUpdateMasterItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MasterItem masterItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update MasterItem partially : {}, {}", id, masterItem);
        if (masterItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterItem> result = masterItemService.partialUpdate(masterItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, masterItem.getId().toString())
        );
    }

    /**
     * {@code GET  /master-items} : get all the masterItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of masterItems in body.
     */
    @GetMapping("/master-items")
    public ResponseEntity<List<MasterItem>> getAllMasterItems(
        MasterItemCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MasterItems by criteria: {}", criteria);
        Page<MasterItem> page = masterItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /master-items/count} : count all the masterItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/master-items/count")
    public ResponseEntity<Long> countMasterItems(MasterItemCriteria criteria) {
        log.debug("REST request to count MasterItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(masterItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /master-items/:id} : get the "id" masterItem.
     *
     * @param id the id of the masterItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/master-items/{id}")
    public ResponseEntity<MasterItem> getMasterItem(@PathVariable Long id) {
        log.debug("REST request to get MasterItem : {}", id);
        Optional<MasterItem> masterItem = masterItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(masterItem);
    }

    /**
     * {@code DELETE  /master-items/:id} : delete the "id" masterItem.
     *
     * @param id the id of the masterItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/master-items/{id}")
    public ResponseEntity<Void> deleteMasterItem(@PathVariable Long id) {
        log.debug("REST request to delete MasterItem : {}", id);
        masterItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
