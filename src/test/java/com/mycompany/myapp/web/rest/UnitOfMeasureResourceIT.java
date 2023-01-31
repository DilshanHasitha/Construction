package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UnitOfMeasure;
import com.mycompany.myapp.repository.UnitOfMeasureRepository;
import com.mycompany.myapp.service.criteria.UnitOfMeasureCriteria;
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
 * Integration tests for the {@link UnitOfMeasureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitOfMeasureResourceIT {

    private static final String DEFAULT_UNIT_OF_MEASURE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_MEASURE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_OF_MEASURE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_MEASURE_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/unit-of-measures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitOfMeasureMockMvc;

    private UnitOfMeasure unitOfMeasure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .unitOfMeasureCode(DEFAULT_UNIT_OF_MEASURE_CODE)
            .unitOfMeasureDescription(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return unitOfMeasure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasure createUpdatedEntity(EntityManager em) {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure()
            .unitOfMeasureCode(UPDATED_UNIT_OF_MEASURE_CODE)
            .unitOfMeasureDescription(UPDATED_UNIT_OF_MEASURE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return unitOfMeasure;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasure = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitOfMeasure() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();
        // Create the UnitOfMeasure
        restUnitOfMeasureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isCreated());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate + 1);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getUnitOfMeasureCode()).isEqualTo(DEFAULT_UNIT_OF_MEASURE_CODE);
        assertThat(testUnitOfMeasure.getUnitOfMeasureDescription()).isEqualTo(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);
        assertThat(testUnitOfMeasure.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createUnitOfMeasureWithExistingId() throws Exception {
        // Create the UnitOfMeasure with an existing ID
        unitOfMeasure.setId(1L);

        int databaseSizeBeforeCreate = unitOfMeasureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnitOfMeasureCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setUnitOfMeasureCode(null);

        // Create the UnitOfMeasure, which fails.

        restUnitOfMeasureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitOfMeasureDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasureRepository.findAll().size();
        // set the field null
        unitOfMeasure.setUnitOfMeasureDescription(null);

        // Create the UnitOfMeasure, which fails.

        restUnitOfMeasureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isBadRequest());

        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasures() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasure.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasureCode").value(hasItem(DEFAULT_UNIT_OF_MEASURE_CODE)))
            .andExpect(jsonPath("$.[*].unitOfMeasureDescription").value(hasItem(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL_ID, unitOfMeasure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasure.getId().intValue()))
            .andExpect(jsonPath("$.unitOfMeasureCode").value(DEFAULT_UNIT_OF_MEASURE_CODE))
            .andExpect(jsonPath("$.unitOfMeasureDescription").value(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getUnitOfMeasuresByIdFiltering() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        Long id = unitOfMeasure.getId();

        defaultUnitOfMeasureShouldBeFound("id.equals=" + id);
        defaultUnitOfMeasureShouldNotBeFound("id.notEquals=" + id);

        defaultUnitOfMeasureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUnitOfMeasureShouldNotBeFound("id.greaterThan=" + id);

        defaultUnitOfMeasureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUnitOfMeasureShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode equals to DEFAULT_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.equals=" + DEFAULT_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode equals to UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.equals=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureCodeIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode in DEFAULT_UNIT_OF_MEASURE_CODE or UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.in=" + DEFAULT_UNIT_OF_MEASURE_CODE + "," + UPDATED_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode equals to UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.in=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode is not null
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.specified=true");

        // Get all the unitOfMeasureList where unitOfMeasureCode is null
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureCodeContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode contains DEFAULT_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.contains=" + DEFAULT_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode contains UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.contains=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureCodeNotContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureCode does not contain DEFAULT_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureCode.doesNotContain=" + DEFAULT_UNIT_OF_MEASURE_CODE);

        // Get all the unitOfMeasureList where unitOfMeasureCode does not contain UPDATED_UNIT_OF_MEASURE_CODE
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureCode.doesNotContain=" + UPDATED_UNIT_OF_MEASURE_CODE);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription equals to DEFAULT_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.equals=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription equals to UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.equals=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription in DEFAULT_UNIT_OF_MEASURE_DESCRIPTION or UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound(
            "unitOfMeasureDescription.in=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION + "," + UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        );

        // Get all the unitOfMeasureList where unitOfMeasureDescription equals to UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.in=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription is not null
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.specified=true");

        // Get all the unitOfMeasureList where unitOfMeasureDescription is null
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureDescriptionContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription contains DEFAULT_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.contains=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription contains UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.contains=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByUnitOfMeasureDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where unitOfMeasureDescription does not contain DEFAULT_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldNotBeFound("unitOfMeasureDescription.doesNotContain=" + DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);

        // Get all the unitOfMeasureList where unitOfMeasureDescription does not contain UPDATED_UNIT_OF_MEASURE_DESCRIPTION
        defaultUnitOfMeasureShouldBeFound("unitOfMeasureDescription.doesNotContain=" + UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where isActive equals to DEFAULT_IS_ACTIVE
        defaultUnitOfMeasureShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the unitOfMeasureList where isActive equals to UPDATED_IS_ACTIVE
        defaultUnitOfMeasureShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultUnitOfMeasureShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the unitOfMeasureList where isActive equals to UPDATED_IS_ACTIVE
        defaultUnitOfMeasureShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasuresByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        // Get all the unitOfMeasureList where isActive is not null
        defaultUnitOfMeasureShouldBeFound("isActive.specified=true");

        // Get all the unitOfMeasureList where isActive is null
        defaultUnitOfMeasureShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUnitOfMeasureShouldBeFound(String filter) throws Exception {
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasure.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasureCode").value(hasItem(DEFAULT_UNIT_OF_MEASURE_CODE)))
            .andExpect(jsonPath("$.[*].unitOfMeasureDescription").value(hasItem(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUnitOfMeasureShouldNotBeFound(String filter) throws Exception {
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUnitOfMeasureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUnitOfMeasure() throws Exception {
        // Get the unitOfMeasure
        restUnitOfMeasureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure
        UnitOfMeasure updatedUnitOfMeasure = unitOfMeasureRepository.findById(unitOfMeasure.getId()).get();
        // Disconnect from session so that the updates on updatedUnitOfMeasure are not directly saved in db
        em.detach(updatedUnitOfMeasure);
        updatedUnitOfMeasure
            .unitOfMeasureCode(UPDATED_UNIT_OF_MEASURE_CODE)
            .unitOfMeasureDescription(UPDATED_UNIT_OF_MEASURE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnitOfMeasure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getUnitOfMeasureCode()).isEqualTo(UPDATED_UNIT_OF_MEASURE_CODE);
        assertThat(testUnitOfMeasure.getUnitOfMeasureDescription()).isEqualTo(UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
        assertThat(testUnitOfMeasure.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitOfMeasureWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure using partial update
        UnitOfMeasure partialUpdatedUnitOfMeasure = new UnitOfMeasure();
        partialUpdatedUnitOfMeasure.setId(unitOfMeasure.getId());

        partialUpdatedUnitOfMeasure.unitOfMeasureCode(UPDATED_UNIT_OF_MEASURE_CODE);

        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getUnitOfMeasureCode()).isEqualTo(UPDATED_UNIT_OF_MEASURE_CODE);
        assertThat(testUnitOfMeasure.getUnitOfMeasureDescription()).isEqualTo(DEFAULT_UNIT_OF_MEASURE_DESCRIPTION);
        assertThat(testUnitOfMeasure.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateUnitOfMeasureWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();

        // Update the unitOfMeasure using partial update
        UnitOfMeasure partialUpdatedUnitOfMeasure = new UnitOfMeasure();
        partialUpdatedUnitOfMeasure.setId(unitOfMeasure.getId());

        partialUpdatedUnitOfMeasure
            .unitOfMeasureCode(UPDATED_UNIT_OF_MEASURE_CODE)
            .unitOfMeasureDescription(UPDATED_UNIT_OF_MEASURE_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);

        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasure))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasure testUnitOfMeasure = unitOfMeasureList.get(unitOfMeasureList.size() - 1);
        assertThat(testUnitOfMeasure.getUnitOfMeasureCode()).isEqualTo(UPDATED_UNIT_OF_MEASURE_CODE);
        assertThat(testUnitOfMeasure.getUnitOfMeasureDescription()).isEqualTo(UPDATED_UNIT_OF_MEASURE_DESCRIPTION);
        assertThat(testUnitOfMeasure.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitOfMeasure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasure))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitOfMeasure() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasureRepository.findAll().size();
        unitOfMeasure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(unitOfMeasure))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasure in the database
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitOfMeasure() throws Exception {
        // Initialize the database
        unitOfMeasureRepository.saveAndFlush(unitOfMeasure);

        int databaseSizeBeforeDelete = unitOfMeasureRepository.findAll().size();

        // Delete the unitOfMeasure
        restUnitOfMeasureMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitOfMeasure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitOfMeasure> unitOfMeasureList = unitOfMeasureRepository.findAll();
        assertThat(unitOfMeasureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
