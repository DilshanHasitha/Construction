package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserPermission;
import com.mycompany.myapp.domain.UserRole;
import com.mycompany.myapp.repository.UserPermissionRepository;
import com.mycompany.myapp.service.criteria.UserPermissionCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserPermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserPermissionResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPermissionMockMvc;

    private UserPermission userPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPermission createEntity(EntityManager em) {
        UserPermission userPermission = new UserPermission()
            .action(DEFAULT_ACTION)
            .document(DEFAULT_DOCUMENT)
            .description(DEFAULT_DESCRIPTION);
        return userPermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPermission createUpdatedEntity(EntityManager em) {
        UserPermission userPermission = new UserPermission()
            .action(UPDATED_ACTION)
            .document(UPDATED_DOCUMENT)
            .description(UPDATED_DESCRIPTION);
        return userPermission;
    }

    @BeforeEach
    public void initTest() {
        userPermission = createEntity(em);
    }

    @Test
    @Transactional
    void createUserPermission() throws Exception {
        int databaseSizeBeforeCreate = userPermissionRepository.findAll().size();
        // Create the UserPermission
        restUserPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isCreated());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeCreate + 1);
        UserPermission testUserPermission = userPermissionList.get(userPermissionList.size() - 1);
        assertThat(testUserPermission.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testUserPermission.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testUserPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createUserPermissionWithExistingId() throws Exception {
        // Create the UserPermission with an existing ID
        userPermission.setId(1L);

        int databaseSizeBeforeCreate = userPermissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPermissionRepository.findAll().size();
        // set the field null
        userPermission.setAction(null);

        // Create the UserPermission, which fails.

        restUserPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPermissionRepository.findAll().size();
        // set the field null
        userPermission.setDocument(null);

        // Create the UserPermission, which fails.

        restUserPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPermissionRepository.findAll().size();
        // set the field null
        userPermission.setDescription(null);

        // Create the UserPermission, which fails.

        restUserPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserPermissions() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getUserPermission() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get the userPermission
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, userPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPermission.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.document").value(DEFAULT_DOCUMENT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getUserPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        Long id = userPermission.getId();

        defaultUserPermissionShouldBeFound("id.equals=" + id);
        defaultUserPermissionShouldNotBeFound("id.notEquals=" + id);

        defaultUserPermissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserPermissionShouldNotBeFound("id.greaterThan=" + id);

        defaultUserPermissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserPermissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where action equals to DEFAULT_ACTION
        defaultUserPermissionShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the userPermissionList where action equals to UPDATED_ACTION
        defaultUserPermissionShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultUserPermissionShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the userPermissionList where action equals to UPDATED_ACTION
        defaultUserPermissionShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where action is not null
        defaultUserPermissionShouldBeFound("action.specified=true");

        // Get all the userPermissionList where action is null
        defaultUserPermissionShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPermissionsByActionContainsSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where action contains DEFAULT_ACTION
        defaultUserPermissionShouldBeFound("action.contains=" + DEFAULT_ACTION);

        // Get all the userPermissionList where action contains UPDATED_ACTION
        defaultUserPermissionShouldNotBeFound("action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByActionNotContainsSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where action does not contain DEFAULT_ACTION
        defaultUserPermissionShouldNotBeFound("action.doesNotContain=" + DEFAULT_ACTION);

        // Get all the userPermissionList where action does not contain UPDATED_ACTION
        defaultUserPermissionShouldBeFound("action.doesNotContain=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where document equals to DEFAULT_DOCUMENT
        defaultUserPermissionShouldBeFound("document.equals=" + DEFAULT_DOCUMENT);

        // Get all the userPermissionList where document equals to UPDATED_DOCUMENT
        defaultUserPermissionShouldNotBeFound("document.equals=" + UPDATED_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDocumentIsInShouldWork() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where document in DEFAULT_DOCUMENT or UPDATED_DOCUMENT
        defaultUserPermissionShouldBeFound("document.in=" + DEFAULT_DOCUMENT + "," + UPDATED_DOCUMENT);

        // Get all the userPermissionList where document equals to UPDATED_DOCUMENT
        defaultUserPermissionShouldNotBeFound("document.in=" + UPDATED_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDocumentIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where document is not null
        defaultUserPermissionShouldBeFound("document.specified=true");

        // Get all the userPermissionList where document is null
        defaultUserPermissionShouldNotBeFound("document.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDocumentContainsSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where document contains DEFAULT_DOCUMENT
        defaultUserPermissionShouldBeFound("document.contains=" + DEFAULT_DOCUMENT);

        // Get all the userPermissionList where document contains UPDATED_DOCUMENT
        defaultUserPermissionShouldNotBeFound("document.contains=" + UPDATED_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDocumentNotContainsSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where document does not contain DEFAULT_DOCUMENT
        defaultUserPermissionShouldNotBeFound("document.doesNotContain=" + DEFAULT_DOCUMENT);

        // Get all the userPermissionList where document does not contain UPDATED_DOCUMENT
        defaultUserPermissionShouldBeFound("document.doesNotContain=" + UPDATED_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where description equals to DEFAULT_DESCRIPTION
        defaultUserPermissionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the userPermissionList where description equals to UPDATED_DESCRIPTION
        defaultUserPermissionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUserPermissionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the userPermissionList where description equals to UPDATED_DESCRIPTION
        defaultUserPermissionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where description is not null
        defaultUserPermissionShouldBeFound("description.specified=true");

        // Get all the userPermissionList where description is null
        defaultUserPermissionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where description contains DEFAULT_DESCRIPTION
        defaultUserPermissionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the userPermissionList where description contains UPDATED_DESCRIPTION
        defaultUserPermissionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissionList where description does not contain DEFAULT_DESCRIPTION
        defaultUserPermissionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the userPermissionList where description does not contain UPDATED_DESCRIPTION
        defaultUserPermissionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUserPermissionsByUserRoleIsEqualToSomething() throws Exception {
        UserRole userRole;
        if (TestUtil.findAll(em, UserRole.class).isEmpty()) {
            userPermissionRepository.saveAndFlush(userPermission);
            userRole = UserRoleResourceIT.createEntity(em);
        } else {
            userRole = TestUtil.findAll(em, UserRole.class).get(0);
        }
        em.persist(userRole);
        em.flush();
        userPermission.addUserRole(userRole);
        userPermissionRepository.saveAndFlush(userPermission);
        Long userRoleId = userRole.getId();

        // Get all the userPermissionList where userRole equals to userRoleId
        defaultUserPermissionShouldBeFound("userRoleId.equals=" + userRoleId);

        // Get all the userPermissionList where userRole equals to (userRoleId + 1)
        defaultUserPermissionShouldNotBeFound("userRoleId.equals=" + (userRoleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserPermissionShouldBeFound(String filter) throws Exception {
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserPermissionShouldNotBeFound(String filter) throws Exception {
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserPermission() throws Exception {
        // Get the userPermission
        restUserPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserPermission() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();

        // Update the userPermission
        UserPermission updatedUserPermission = userPermissionRepository.findById(userPermission.getId()).get();
        // Disconnect from session so that the updates on updatedUserPermission are not directly saved in db
        em.detach(updatedUserPermission);
        updatedUserPermission.action(UPDATED_ACTION).document(UPDATED_DOCUMENT).description(UPDATED_DESCRIPTION);

        restUserPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserPermission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserPermission))
            )
            .andExpect(status().isOk());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
        UserPermission testUserPermission = userPermissionList.get(userPermissionList.size() - 1);
        assertThat(testUserPermission.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testUserPermission.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testUserPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingUserPermission() throws Exception {
        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();
        userPermission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPermission.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPermission() throws Exception {
        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();
        userPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPermission() throws Exception {
        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();
        userPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPermission)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPermissionWithPatch() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();

        // Update the userPermission using partial update
        UserPermission partialUpdatedUserPermission = new UserPermission();
        partialUpdatedUserPermission.setId(userPermission.getId());

        partialUpdatedUserPermission.document(UPDATED_DOCUMENT).description(UPDATED_DESCRIPTION);

        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPermission))
            )
            .andExpect(status().isOk());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
        UserPermission testUserPermission = userPermissionList.get(userPermissionList.size() - 1);
        assertThat(testUserPermission.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testUserPermission.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testUserPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateUserPermissionWithPatch() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();

        // Update the userPermission using partial update
        UserPermission partialUpdatedUserPermission = new UserPermission();
        partialUpdatedUserPermission.setId(userPermission.getId());

        partialUpdatedUserPermission.action(UPDATED_ACTION).document(UPDATED_DOCUMENT).description(UPDATED_DESCRIPTION);

        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPermission))
            )
            .andExpect(status().isOk());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
        UserPermission testUserPermission = userPermissionList.get(userPermissionList.size() - 1);
        assertThat(testUserPermission.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testUserPermission.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testUserPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingUserPermission() throws Exception {
        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();
        userPermission.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPermission() throws Exception {
        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();
        userPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPermission() throws Exception {
        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();
        userPermission.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userPermission))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPermission() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        int databaseSizeBeforeDelete = userPermissionRepository.findAll().size();

        // Delete the userPermission
        restUserPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPermission> userPermissionList = userPermissionRepository.findAll();
        assertThat(userPermissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
