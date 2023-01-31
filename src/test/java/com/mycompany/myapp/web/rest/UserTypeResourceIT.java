package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserType;
import com.mycompany.myapp.repository.UserTypeRepository;
import com.mycompany.myapp.service.criteria.UserTypeCriteria;
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
 * Integration tests for the {@link UserTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_USER_ROLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/user-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserTypeMockMvc;

    private UserType userType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserType createEntity(EntityManager em) {
        UserType userType = new UserType().code(DEFAULT_CODE).userRole(DEFAULT_USER_ROLE).isActive(DEFAULT_IS_ACTIVE);
        return userType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserType createUpdatedEntity(EntityManager em) {
        UserType userType = new UserType().code(UPDATED_CODE).userRole(UPDATED_USER_ROLE).isActive(UPDATED_IS_ACTIVE);
        return userType;
    }

    @BeforeEach
    public void initTest() {
        userType = createEntity(em);
    }

    @Test
    @Transactional
    void createUserType() throws Exception {
        int databaseSizeBeforeCreate = userTypeRepository.findAll().size();
        // Create the UserType
        restUserTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isCreated());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeCreate + 1);
        UserType testUserType = userTypeList.get(userTypeList.size() - 1);
        assertThat(testUserType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUserType.getUserRole()).isEqualTo(DEFAULT_USER_ROLE);
        assertThat(testUserType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createUserTypeWithExistingId() throws Exception {
        // Create the UserType with an existing ID
        userType.setId(1L);

        int databaseSizeBeforeCreate = userTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isBadRequest());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTypeRepository.findAll().size();
        // set the field null
        userType.setUserRole(null);

        // Create the UserType, which fails.

        restUserTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isBadRequest());

        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserTypes() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList
        restUserTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].userRole").value(hasItem(DEFAULT_USER_ROLE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get the userType
        restUserTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, userType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.userRole").value(DEFAULT_USER_ROLE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getUserTypesByIdFiltering() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        Long id = userType.getId();

        defaultUserTypeShouldBeFound("id.equals=" + id);
        defaultUserTypeShouldNotBeFound("id.notEquals=" + id);

        defaultUserTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultUserTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where code equals to DEFAULT_CODE
        defaultUserTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the userTypeList where code equals to UPDATED_CODE
        defaultUserTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUserTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultUserTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the userTypeList where code equals to UPDATED_CODE
        defaultUserTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUserTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where code is not null
        defaultUserTypeShouldBeFound("code.specified=true");

        // Get all the userTypeList where code is null
        defaultUserTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where code contains DEFAULT_CODE
        defaultUserTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the userTypeList where code contains UPDATED_CODE
        defaultUserTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUserTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where code does not contain DEFAULT_CODE
        defaultUserTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the userTypeList where code does not contain UPDATED_CODE
        defaultUserTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUserTypesByUserRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where userRole equals to DEFAULT_USER_ROLE
        defaultUserTypeShouldBeFound("userRole.equals=" + DEFAULT_USER_ROLE);

        // Get all the userTypeList where userRole equals to UPDATED_USER_ROLE
        defaultUserTypeShouldNotBeFound("userRole.equals=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllUserTypesByUserRoleIsInShouldWork() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where userRole in DEFAULT_USER_ROLE or UPDATED_USER_ROLE
        defaultUserTypeShouldBeFound("userRole.in=" + DEFAULT_USER_ROLE + "," + UPDATED_USER_ROLE);

        // Get all the userTypeList where userRole equals to UPDATED_USER_ROLE
        defaultUserTypeShouldNotBeFound("userRole.in=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllUserTypesByUserRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where userRole is not null
        defaultUserTypeShouldBeFound("userRole.specified=true");

        // Get all the userTypeList where userRole is null
        defaultUserTypeShouldNotBeFound("userRole.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTypesByUserRoleContainsSomething() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where userRole contains DEFAULT_USER_ROLE
        defaultUserTypeShouldBeFound("userRole.contains=" + DEFAULT_USER_ROLE);

        // Get all the userTypeList where userRole contains UPDATED_USER_ROLE
        defaultUserTypeShouldNotBeFound("userRole.contains=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllUserTypesByUserRoleNotContainsSomething() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where userRole does not contain DEFAULT_USER_ROLE
        defaultUserTypeShouldNotBeFound("userRole.doesNotContain=" + DEFAULT_USER_ROLE);

        // Get all the userTypeList where userRole does not contain UPDATED_USER_ROLE
        defaultUserTypeShouldBeFound("userRole.doesNotContain=" + UPDATED_USER_ROLE);
    }

    @Test
    @Transactional
    void getAllUserTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultUserTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the userTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUserTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultUserTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the userTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUserTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList where isActive is not null
        defaultUserTypeShouldBeFound("isActive.specified=true");

        // Get all the userTypeList where isActive is null
        defaultUserTypeShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserTypeShouldBeFound(String filter) throws Exception {
        restUserTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].userRole").value(hasItem(DEFAULT_USER_ROLE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restUserTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserTypeShouldNotBeFound(String filter) throws Exception {
        restUserTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserType() throws Exception {
        // Get the userType
        restUserTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();

        // Update the userType
        UserType updatedUserType = userTypeRepository.findById(userType.getId()).get();
        // Disconnect from session so that the updates on updatedUserType are not directly saved in db
        em.detach(updatedUserType);
        updatedUserType.code(UPDATED_CODE).userRole(UPDATED_USER_ROLE).isActive(UPDATED_IS_ACTIVE);

        restUserTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserType))
            )
            .andExpect(status().isOk());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
        UserType testUserType = userTypeList.get(userTypeList.size() - 1);
        assertThat(testUserType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserType.getUserRole()).isEqualTo(UPDATED_USER_ROLE);
        assertThat(testUserType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingUserType() throws Exception {
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();
        userType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserType() throws Exception {
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();
        userType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserType() throws Exception {
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();
        userType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserTypeWithPatch() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();

        // Update the userType using partial update
        UserType partialUpdatedUserType = new UserType();
        partialUpdatedUserType.setId(userType.getId());

        partialUpdatedUserType.code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);

        restUserTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserType))
            )
            .andExpect(status().isOk());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
        UserType testUserType = userTypeList.get(userTypeList.size() - 1);
        assertThat(testUserType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserType.getUserRole()).isEqualTo(DEFAULT_USER_ROLE);
        assertThat(testUserType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateUserTypeWithPatch() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();

        // Update the userType using partial update
        UserType partialUpdatedUserType = new UserType();
        partialUpdatedUserType.setId(userType.getId());

        partialUpdatedUserType.code(UPDATED_CODE).userRole(UPDATED_USER_ROLE).isActive(UPDATED_IS_ACTIVE);

        restUserTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserType))
            )
            .andExpect(status().isOk());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
        UserType testUserType = userTypeList.get(userTypeList.size() - 1);
        assertThat(testUserType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserType.getUserRole()).isEqualTo(UPDATED_USER_ROLE);
        assertThat(testUserType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingUserType() throws Exception {
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();
        userType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserType() throws Exception {
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();
        userType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserType() throws Exception {
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();
        userType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        int databaseSizeBeforeDelete = userTypeRepository.findAll().size();

        // Delete the userType
        restUserTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, userType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
