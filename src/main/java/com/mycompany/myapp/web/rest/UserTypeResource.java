package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserType;
import com.mycompany.myapp.repository.UserTypeRepository;
import com.mycompany.myapp.service.UserTypeQueryService;
import com.mycompany.myapp.service.UserTypeService;
import com.mycompany.myapp.service.criteria.UserTypeCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserType}.
 */
@RestController
@RequestMapping("/api")
public class UserTypeResource {

    private final Logger log = LoggerFactory.getLogger(UserTypeResource.class);

    private static final String ENTITY_NAME = "userType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTypeService userTypeService;

    private final UserTypeRepository userTypeRepository;

    private final UserTypeQueryService userTypeQueryService;

    public UserTypeResource(
        UserTypeService userTypeService,
        UserTypeRepository userTypeRepository,
        UserTypeQueryService userTypeQueryService
    ) {
        this.userTypeService = userTypeService;
        this.userTypeRepository = userTypeRepository;
        this.userTypeQueryService = userTypeQueryService;
    }

    /**
     * {@code POST  /user-types} : Create a new userType.
     *
     * @param userType the userType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userType, or with status {@code 400 (Bad Request)} if the userType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-types")
    public ResponseEntity<UserType> createUserType(@Valid @RequestBody UserType userType) throws URISyntaxException {
        log.debug("REST request to save UserType : {}", userType);
        if (userType.getId() != null) {
            throw new BadRequestAlertException("A new userType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserType result = userTypeService.save(userType);
        return ResponseEntity
            .created(new URI("/api/user-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-types/:id} : Updates an existing userType.
     *
     * @param id the id of the userType to save.
     * @param userType the userType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userType,
     * or with status {@code 400 (Bad Request)} if the userType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-types/{id}")
    public ResponseEntity<UserType> updateUserType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserType userType
    ) throws URISyntaxException {
        log.debug("REST request to update UserType : {}, {}", id, userType);
        if (userType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserType result = userTypeService.update(userType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-types/:id} : Partial updates given fields of an existing userType, field will ignore if it is null
     *
     * @param id the id of the userType to save.
     * @param userType the userType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userType,
     * or with status {@code 400 (Bad Request)} if the userType is not valid,
     * or with status {@code 404 (Not Found)} if the userType is not found,
     * or with status {@code 500 (Internal Server Error)} if the userType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserType> partialUpdateUserType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserType userType
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserType partially : {}, {}", id, userType);
        if (userType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserType> result = userTypeService.partialUpdate(userType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userType.getId().toString())
        );
    }

    /**
     * {@code GET  /user-types} : get all the userTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTypes in body.
     */
    @GetMapping("/user-types")
    public ResponseEntity<List<UserType>> getAllUserTypes(
        UserTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserTypes by criteria: {}", criteria);
        Page<UserType> page = userTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-types/count} : count all the userTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-types/count")
    public ResponseEntity<Long> countUserTypes(UserTypeCriteria criteria) {
        log.debug("REST request to count UserTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(userTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-types/:id} : get the "id" userType.
     *
     * @param id the id of the userType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-types/{id}")
    public ResponseEntity<UserType> getUserType(@PathVariable Long id) {
        log.debug("REST request to get UserType : {}", id);
        Optional<UserType> userType = userTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userType);
    }

    /**
     * {@code DELETE  /user-types/:id} : delete the "id" userType.
     *
     * @param id the id of the userType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-types/{id}")
    public ResponseEntity<Void> deleteUserType(@PathVariable Long id) {
        log.debug("REST request to delete UserType : {}", id);
        userTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
