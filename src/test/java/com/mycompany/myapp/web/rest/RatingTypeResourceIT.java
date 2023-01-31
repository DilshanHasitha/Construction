package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RatingType;
import com.mycompany.myapp.repository.RatingTypeRepository;
import com.mycompany.myapp.service.criteria.RatingTypeCriteria;
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
 * Integration tests for the {@link RatingTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RatingTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_DESCRIPTION = 1D;
    private static final Double UPDATED_DESCRIPTION = 2D;
    private static final Double SMALLER_DESCRIPTION = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/rating-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RatingTypeRepository ratingTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRatingTypeMockMvc;

    private RatingType ratingType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RatingType createEntity(EntityManager em) {
        RatingType ratingType = new RatingType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return ratingType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RatingType createUpdatedEntity(EntityManager em) {
        RatingType ratingType = new RatingType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return ratingType;
    }

    @BeforeEach
    public void initTest() {
        ratingType = createEntity(em);
    }

    @Test
    @Transactional
    void createRatingType() throws Exception {
        int databaseSizeBeforeCreate = ratingTypeRepository.findAll().size();
        // Create the RatingType
        restRatingTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ratingType)))
            .andExpect(status().isCreated());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RatingType testRatingType = ratingTypeList.get(ratingTypeList.size() - 1);
        assertThat(testRatingType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRatingType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createRatingTypeWithExistingId() throws Exception {
        // Create the RatingType with an existing ID
        ratingType.setId(1L);

        int databaseSizeBeforeCreate = ratingTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ratingType)))
            .andExpect(status().isBadRequest());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRatingTypes() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList
        restRatingTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ratingType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.doubleValue())));
    }

    @Test
    @Transactional
    void getRatingType() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get the ratingType
        restRatingTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, ratingType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ratingType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.doubleValue()));
    }

    @Test
    @Transactional
    void getRatingTypesByIdFiltering() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        Long id = ratingType.getId();

        defaultRatingTypeShouldBeFound("id.equals=" + id);
        defaultRatingTypeShouldNotBeFound("id.notEquals=" + id);

        defaultRatingTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRatingTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultRatingTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRatingTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRatingTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where name equals to DEFAULT_NAME
        defaultRatingTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ratingTypeList where name equals to UPDATED_NAME
        defaultRatingTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRatingTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRatingTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ratingTypeList where name equals to UPDATED_NAME
        defaultRatingTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRatingTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where name is not null
        defaultRatingTypeShouldBeFound("name.specified=true");

        // Get all the ratingTypeList where name is null
        defaultRatingTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRatingTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where name contains DEFAULT_NAME
        defaultRatingTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ratingTypeList where name contains UPDATED_NAME
        defaultRatingTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRatingTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where name does not contain DEFAULT_NAME
        defaultRatingTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ratingTypeList where name does not contain UPDATED_NAME
        defaultRatingTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRatingTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where description equals to DEFAULT_DESCRIPTION
        defaultRatingTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ratingTypeList where description equals to UPDATED_DESCRIPTION
        defaultRatingTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRatingTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRatingTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ratingTypeList where description equals to UPDATED_DESCRIPTION
        defaultRatingTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRatingTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where description is not null
        defaultRatingTypeShouldBeFound("description.specified=true");

        // Get all the ratingTypeList where description is null
        defaultRatingTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRatingTypesByDescriptionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where description is greater than or equal to DEFAULT_DESCRIPTION
        defaultRatingTypeShouldBeFound("description.greaterThanOrEqual=" + DEFAULT_DESCRIPTION);

        // Get all the ratingTypeList where description is greater than or equal to UPDATED_DESCRIPTION
        defaultRatingTypeShouldNotBeFound("description.greaterThanOrEqual=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRatingTypesByDescriptionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where description is less than or equal to DEFAULT_DESCRIPTION
        defaultRatingTypeShouldBeFound("description.lessThanOrEqual=" + DEFAULT_DESCRIPTION);

        // Get all the ratingTypeList where description is less than or equal to SMALLER_DESCRIPTION
        defaultRatingTypeShouldNotBeFound("description.lessThanOrEqual=" + SMALLER_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRatingTypesByDescriptionIsLessThanSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where description is less than DEFAULT_DESCRIPTION
        defaultRatingTypeShouldNotBeFound("description.lessThan=" + DEFAULT_DESCRIPTION);

        // Get all the ratingTypeList where description is less than UPDATED_DESCRIPTION
        defaultRatingTypeShouldBeFound("description.lessThan=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRatingTypesByDescriptionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        // Get all the ratingTypeList where description is greater than DEFAULT_DESCRIPTION
        defaultRatingTypeShouldNotBeFound("description.greaterThan=" + DEFAULT_DESCRIPTION);

        // Get all the ratingTypeList where description is greater than SMALLER_DESCRIPTION
        defaultRatingTypeShouldBeFound("description.greaterThan=" + SMALLER_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRatingTypeShouldBeFound(String filter) throws Exception {
        restRatingTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ratingType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.doubleValue())));

        // Check, that the count call also returns 1
        restRatingTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRatingTypeShouldNotBeFound(String filter) throws Exception {
        restRatingTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRatingTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRatingType() throws Exception {
        // Get the ratingType
        restRatingTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRatingType() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();

        // Update the ratingType
        RatingType updatedRatingType = ratingTypeRepository.findById(ratingType.getId()).get();
        // Disconnect from session so that the updates on updatedRatingType are not directly saved in db
        em.detach(updatedRatingType);
        updatedRatingType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRatingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRatingType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRatingType))
            )
            .andExpect(status().isOk());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
        RatingType testRatingType = ratingTypeList.get(ratingTypeList.size() - 1);
        assertThat(testRatingType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRatingType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingRatingType() throws Exception {
        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();
        ratingType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRatingType() throws Exception {
        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();
        ratingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ratingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRatingType() throws Exception {
        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();
        ratingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ratingType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRatingTypeWithPatch() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();

        // Update the ratingType using partial update
        RatingType partialUpdatedRatingType = new RatingType();
        partialUpdatedRatingType.setId(ratingType.getId());

        partialUpdatedRatingType.name(UPDATED_NAME);

        restRatingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRatingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRatingType))
            )
            .andExpect(status().isOk());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
        RatingType testRatingType = ratingTypeList.get(ratingTypeList.size() - 1);
        assertThat(testRatingType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRatingType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateRatingTypeWithPatch() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();

        // Update the ratingType using partial update
        RatingType partialUpdatedRatingType = new RatingType();
        partialUpdatedRatingType.setId(ratingType.getId());

        partialUpdatedRatingType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRatingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRatingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRatingType))
            )
            .andExpect(status().isOk());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
        RatingType testRatingType = ratingTypeList.get(ratingTypeList.size() - 1);
        assertThat(testRatingType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRatingType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingRatingType() throws Exception {
        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();
        ratingType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ratingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ratingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRatingType() throws Exception {
        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();
        ratingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ratingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRatingType() throws Exception {
        int databaseSizeBeforeUpdate = ratingTypeRepository.findAll().size();
        ratingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ratingType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RatingType in the database
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRatingType() throws Exception {
        // Initialize the database
        ratingTypeRepository.saveAndFlush(ratingType);

        int databaseSizeBeforeDelete = ratingTypeRepository.findAll().size();

        // Delete the ratingType
        restRatingTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, ratingType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RatingType> ratingTypeList = ratingTypeRepository.findAll();
        assertThat(ratingTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
