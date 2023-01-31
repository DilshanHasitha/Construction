package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserPermission;
import com.mycompany.myapp.repository.UserPermissionRepository;
import com.mycompany.myapp.service.UserPermissionQueryService;
import com.mycompany.myapp.service.UserPermissionService;
import com.mycompany.myapp.service.criteria.UserPermissionCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserPermission}.
 */
@RestController
@RequestMapping("/api")
public class UserPermissionResource {

    private final Logger log = LoggerFactory.getLogger(UserPermissionResource.class);

    private static final String ENTITY_NAME = "userPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPermissionService userPermissionService;

    private final UserPermissionRepository userPermissionRepository;

    private final UserPermissionQueryService userPermissionQueryService;

    public UserPermissionResource(
        UserPermissionService userPermissionService,
        UserPermissionRepository userPermissionRepository,
        UserPermissionQueryService userPermissionQueryService
    ) {
        this.userPermissionService = userPermissionService;
        this.userPermissionRepository = userPermissionRepository;
        this.userPermissionQueryService = userPermissionQueryService;
    }

    /**
     * {@code POST  /user-permissions} : Create a new userPermission.
     *
     * @param userPermission the userPermission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPermission, or with status {@code 400 (Bad Request)} if the userPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-permissions")
    public ResponseEntity<UserPermission> createUserPermission(@Valid @RequestBody UserPermission userPermission)
        throws URISyntaxException {
        log.debug("REST request to save UserPermission : {}", userPermission);
        if (userPermission.getId() != null) {
            throw new BadRequestAlertException("A new userPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPermission result = userPermissionService.save(userPermission);
        return ResponseEntity
            .created(new URI("/api/user-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-permissions/:id} : Updates an existing userPermission.
     *
     * @param id the id of the userPermission to save.
     * @param userPermission the userPermission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPermission,
     * or with status {@code 400 (Bad Request)} if the userPermission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPermission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-permissions/{id}")
    public ResponseEntity<UserPermission> updateUserPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserPermission userPermission
    ) throws URISyntaxException {
        log.debug("REST request to update UserPermission : {}, {}", id, userPermission);
        if (userPermission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPermission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserPermission result = userPermissionService.update(userPermission);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userPermission.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-permissions/:id} : Partial updates given fields of an existing userPermission, field will ignore if it is null
     *
     * @param id the id of the userPermission to save.
     * @param userPermission the userPermission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPermission,
     * or with status {@code 400 (Bad Request)} if the userPermission is not valid,
     * or with status {@code 404 (Not Found)} if the userPermission is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPermission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-permissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserPermission> partialUpdateUserPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserPermission userPermission
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserPermission partially : {}, {}", id, userPermission);
        if (userPermission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPermission.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserPermission> result = userPermissionService.partialUpdate(userPermission);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userPermission.getId().toString())
        );
    }

    /**
     * {@code GET  /user-permissions} : get all the userPermissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPermissions in body.
     */
    @GetMapping("/user-permissions")
    public ResponseEntity<List<UserPermission>> getAllUserPermissions(
        UserPermissionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserPermissions by criteria: {}", criteria);
        Page<UserPermission> page = userPermissionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-permissions/count} : count all the userPermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-permissions/count")
    public ResponseEntity<Long> countUserPermissions(UserPermissionCriteria criteria) {
        log.debug("REST request to count UserPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(userPermissionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-permissions/:id} : get the "id" userPermission.
     *
     * @param id the id of the userPermission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPermission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-permissions/{id}")
    public ResponseEntity<UserPermission> getUserPermission(@PathVariable Long id) {
        log.debug("REST request to get UserPermission : {}", id);
        Optional<UserPermission> userPermission = userPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPermission);
    }

    /**
     * {@code DELETE  /user-permissions/:id} : delete the "id" userPermission.
     *
     * @param id the id of the userPermission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-permissions/{id}")
    public ResponseEntity<Void> deleteUserPermission(@PathVariable Long id) {
        log.debug("REST request to delete UserPermission : {}", id);
        userPermissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
