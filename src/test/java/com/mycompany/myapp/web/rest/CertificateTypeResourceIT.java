package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CertificateType;
import com.mycompany.myapp.repository.CertificateTypeRepository;
import com.mycompany.myapp.service.criteria.CertificateTypeCriteria;
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
 * Integration tests for the {@link CertificateTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CertificateTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/certificate-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CertificateTypeRepository certificateTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCertificateTypeMockMvc;

    private CertificateType certificateType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CertificateType createEntity(EntityManager em) {
        CertificateType certificateType = new CertificateType().code(DEFAULT_CODE).name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return certificateType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CertificateType createUpdatedEntity(EntityManager em) {
        CertificateType certificateType = new CertificateType().code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        return certificateType;
    }

    @BeforeEach
    public void initTest() {
        certificateType = createEntity(em);
    }

    @Test
    @Transactional
    void createCertificateType() throws Exception {
        int databaseSizeBeforeCreate = certificateTypeRepository.findAll().size();
        // Create the CertificateType
        restCertificateTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isCreated());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CertificateType testCertificateType = certificateTypeList.get(certificateTypeList.size() - 1);
        assertThat(testCertificateType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCertificateType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCertificateType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createCertificateTypeWithExistingId() throws Exception {
        // Create the CertificateType with an existing ID
        certificateType.setId(1L);

        int databaseSizeBeforeCreate = certificateTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCertificateTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = certificateTypeRepository.findAll().size();
        // set the field null
        certificateType.setCode(null);

        // Create the CertificateType, which fails.

        restCertificateTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isBadRequest());

        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCertificateTypes() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList
        restCertificateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getCertificateType() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get the certificateType
        restCertificateTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, certificateType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(certificateType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getCertificateTypesByIdFiltering() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        Long id = certificateType.getId();

        defaultCertificateTypeShouldBeFound("id.equals=" + id);
        defaultCertificateTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCertificateTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCertificateTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCertificateTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCertificateTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where code equals to DEFAULT_CODE
        defaultCertificateTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the certificateTypeList where code equals to UPDATED_CODE
        defaultCertificateTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCertificateTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the certificateTypeList where code equals to UPDATED_CODE
        defaultCertificateTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where code is not null
        defaultCertificateTypeShouldBeFound("code.specified=true");

        // Get all the certificateTypeList where code is null
        defaultCertificateTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllCertificateTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where code contains DEFAULT_CODE
        defaultCertificateTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the certificateTypeList where code contains UPDATED_CODE
        defaultCertificateTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where code does not contain DEFAULT_CODE
        defaultCertificateTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the certificateTypeList where code does not contain UPDATED_CODE
        defaultCertificateTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where name equals to DEFAULT_NAME
        defaultCertificateTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the certificateTypeList where name equals to UPDATED_NAME
        defaultCertificateTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCertificateTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the certificateTypeList where name equals to UPDATED_NAME
        defaultCertificateTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where name is not null
        defaultCertificateTypeShouldBeFound("name.specified=true");

        // Get all the certificateTypeList where name is null
        defaultCertificateTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCertificateTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where name contains DEFAULT_NAME
        defaultCertificateTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the certificateTypeList where name contains UPDATED_NAME
        defaultCertificateTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where name does not contain DEFAULT_NAME
        defaultCertificateTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the certificateTypeList where name does not contain UPDATED_NAME
        defaultCertificateTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCertificateTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the certificateTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultCertificateTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCertificateTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the certificateTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultCertificateTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCertificateTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        // Get all the certificateTypeList where isActive is not null
        defaultCertificateTypeShouldBeFound("isActive.specified=true");

        // Get all the certificateTypeList where isActive is null
        defaultCertificateTypeShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCertificateTypeShouldBeFound(String filter) throws Exception {
        restCertificateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCertificateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCertificateTypeShouldNotBeFound(String filter) throws Exception {
        restCertificateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCertificateTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCertificateType() throws Exception {
        // Get the certificateType
        restCertificateTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCertificateType() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();

        // Update the certificateType
        CertificateType updatedCertificateType = certificateTypeRepository.findById(certificateType.getId()).get();
        // Disconnect from session so that the updates on updatedCertificateType are not directly saved in db
        em.detach(updatedCertificateType);
        updatedCertificateType.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restCertificateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCertificateType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCertificateType))
            )
            .andExpect(status().isOk());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
        CertificateType testCertificateType = certificateTypeList.get(certificateTypeList.size() - 1);
        assertThat(testCertificateType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCertificateType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCertificateType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingCertificateType() throws Exception {
        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();
        certificateType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, certificateType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCertificateType() throws Exception {
        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();
        certificateType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCertificateType() throws Exception {
        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();
        certificateType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCertificateTypeWithPatch() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();

        // Update the certificateType using partial update
        CertificateType partialUpdatedCertificateType = new CertificateType();
        partialUpdatedCertificateType.setId(certificateType.getId());

        partialUpdatedCertificateType.isActive(UPDATED_IS_ACTIVE);

        restCertificateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificateType))
            )
            .andExpect(status().isOk());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
        CertificateType testCertificateType = certificateTypeList.get(certificateTypeList.size() - 1);
        assertThat(testCertificateType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCertificateType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCertificateType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateCertificateTypeWithPatch() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();

        // Update the certificateType using partial update
        CertificateType partialUpdatedCertificateType = new CertificateType();
        partialUpdatedCertificateType.setId(certificateType.getId());

        partialUpdatedCertificateType.code(UPDATED_CODE).name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restCertificateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificateType))
            )
            .andExpect(status().isOk());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
        CertificateType testCertificateType = certificateTypeList.get(certificateTypeList.size() - 1);
        assertThat(testCertificateType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCertificateType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCertificateType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingCertificateType() throws Exception {
        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();
        certificateType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, certificateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCertificateType() throws Exception {
        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();
        certificateType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCertificateType() throws Exception {
        int databaseSizeBeforeUpdate = certificateTypeRepository.findAll().size();
        certificateType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificateType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CertificateType in the database
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCertificateType() throws Exception {
        // Initialize the database
        certificateTypeRepository.saveAndFlush(certificateType);

        int databaseSizeBeforeDelete = certificateTypeRepository.findAll().size();

        // Delete the certificateType
        restCertificateTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, certificateType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CertificateType> certificateTypeList = certificateTypeRepository.findAll();
        assertThat(certificateTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
