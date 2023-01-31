package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CertificateType;
import com.mycompany.myapp.repository.CertificateTypeRepository;
import com.mycompany.myapp.service.CertificateTypeQueryService;
import com.mycompany.myapp.service.CertificateTypeService;
import com.mycompany.myapp.service.criteria.CertificateTypeCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CertificateType}.
 */
@RestController
@RequestMapping("/api")
public class CertificateTypeResource {

    private final Logger log = LoggerFactory.getLogger(CertificateTypeResource.class);

    private static final String ENTITY_NAME = "certificateType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CertificateTypeService certificateTypeService;

    private final CertificateTypeRepository certificateTypeRepository;

    private final CertificateTypeQueryService certificateTypeQueryService;

    public CertificateTypeResource(
        CertificateTypeService certificateTypeService,
        CertificateTypeRepository certificateTypeRepository,
        CertificateTypeQueryService certificateTypeQueryService
    ) {
        this.certificateTypeService = certificateTypeService;
        this.certificateTypeRepository = certificateTypeRepository;
        this.certificateTypeQueryService = certificateTypeQueryService;
    }

    /**
     * {@code POST  /certificate-types} : Create a new certificateType.
     *
     * @param certificateType the certificateType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new certificateType, or with status {@code 400 (Bad Request)} if the certificateType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/certificate-types")
    public ResponseEntity<CertificateType> createCertificateType(@Valid @RequestBody CertificateType certificateType)
        throws URISyntaxException {
        log.debug("REST request to save CertificateType : {}", certificateType);
        if (certificateType.getId() != null) {
            throw new BadRequestAlertException("A new certificateType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CertificateType result = certificateTypeService.save(certificateType);
        return ResponseEntity
            .created(new URI("/api/certificate-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /certificate-types/:id} : Updates an existing certificateType.
     *
     * @param id the id of the certificateType to save.
     * @param certificateType the certificateType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated certificateType,
     * or with status {@code 400 (Bad Request)} if the certificateType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the certificateType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/certificate-types/{id}")
    public ResponseEntity<CertificateType> updateCertificateType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CertificateType certificateType
    ) throws URISyntaxException {
        log.debug("REST request to update CertificateType : {}, {}", id, certificateType);
        if (certificateType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, certificateType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!certificateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CertificateType result = certificateTypeService.update(certificateType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, certificateType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /certificate-types/:id} : Partial updates given fields of an existing certificateType, field will ignore if it is null
     *
     * @param id the id of the certificateType to save.
     * @param certificateType the certificateType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated certificateType,
     * or with status {@code 400 (Bad Request)} if the certificateType is not valid,
     * or with status {@code 404 (Not Found)} if the certificateType is not found,
     * or with status {@code 500 (Internal Server Error)} if the certificateType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/certificate-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CertificateType> partialUpdateCertificateType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CertificateType certificateType
    ) throws URISyntaxException {
        log.debug("REST request to partial update CertificateType partially : {}, {}", id, certificateType);
        if (certificateType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, certificateType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!certificateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CertificateType> result = certificateTypeService.partialUpdate(certificateType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, certificateType.getId().toString())
        );
    }

    /**
     * {@code GET  /certificate-types} : get all the certificateTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of certificateTypes in body.
     */
    @GetMapping("/certificate-types")
    public ResponseEntity<List<CertificateType>> getAllCertificateTypes(
        CertificateTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CertificateTypes by criteria: {}", criteria);
        Page<CertificateType> page = certificateTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /certificate-types/count} : count all the certificateTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/certificate-types/count")
    public ResponseEntity<Long> countCertificateTypes(CertificateTypeCriteria criteria) {
        log.debug("REST request to count CertificateTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(certificateTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /certificate-types/:id} : get the "id" certificateType.
     *
     * @param id the id of the certificateType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the certificateType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/certificate-types/{id}")
    public ResponseEntity<CertificateType> getCertificateType(@PathVariable Long id) {
        log.debug("REST request to get CertificateType : {}", id);
        Optional<CertificateType> certificateType = certificateTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(certificateType);
    }

    /**
     * {@code DELETE  /certificate-types/:id} : delete the "id" certificateType.
     *
     * @param id the id of the certificateType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/certificate-types/{id}")
    public ResponseEntity<Void> deleteCertificateType(@PathVariable Long id) {
        log.debug("REST request to delete CertificateType : {}", id);
        certificateTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
