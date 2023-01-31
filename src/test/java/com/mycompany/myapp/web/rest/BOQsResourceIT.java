package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BOQDetails;
import com.mycompany.myapp.domain.BOQs;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.repository.BOQsRepository;
import com.mycompany.myapp.service.BOQsService;
import com.mycompany.myapp.service.criteria.BOQsCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BOQsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BOQsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/bo-qs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BOQsRepository bOQsRepository;

    @Mock
    private BOQsRepository bOQsRepositoryMock;

    @Mock
    private BOQsService bOQsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBOQsMockMvc;

    private BOQs bOQs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BOQs createEntity(EntityManager em) {
        BOQs bOQs = new BOQs().code(DEFAULT_CODE).isActive(DEFAULT_IS_ACTIVE);
        return bOQs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BOQs createUpdatedEntity(EntityManager em) {
        BOQs bOQs = new BOQs().code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);
        return bOQs;
    }

    @BeforeEach
    public void initTest() {
        bOQs = createEntity(em);
    }

    @Test
    @Transactional
    void createBOQs() throws Exception {
        int databaseSizeBeforeCreate = bOQsRepository.findAll().size();
        // Create the BOQs
        restBOQsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bOQs)))
            .andExpect(status().isCreated());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeCreate + 1);
        BOQs testBOQs = bOQsList.get(bOQsList.size() - 1);
        assertThat(testBOQs.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBOQs.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBOQsWithExistingId() throws Exception {
        // Create the BOQs with an existing ID
        bOQs.setId(1L);

        int databaseSizeBeforeCreate = bOQsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBOQsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bOQs)))
            .andExpect(status().isBadRequest());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBOQs() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList
        restBOQsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bOQs.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBOQsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bOQsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBOQsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bOQsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBOQsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bOQsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBOQsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bOQsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBOQs() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get the bOQs
        restBOQsMockMvc
            .perform(get(ENTITY_API_URL_ID, bOQs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bOQs.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBOQsByIdFiltering() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        Long id = bOQs.getId();

        defaultBOQsShouldBeFound("id.equals=" + id);
        defaultBOQsShouldNotBeFound("id.notEquals=" + id);

        defaultBOQsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBOQsShouldNotBeFound("id.greaterThan=" + id);

        defaultBOQsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBOQsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBOQsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where code equals to DEFAULT_CODE
        defaultBOQsShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the bOQsList where code equals to UPDATED_CODE
        defaultBOQsShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBOQsShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the bOQsList where code equals to UPDATED_CODE
        defaultBOQsShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where code is not null
        defaultBOQsShouldBeFound("code.specified=true");

        // Get all the bOQsList where code is null
        defaultBOQsShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBOQsByCodeContainsSomething() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where code contains DEFAULT_CODE
        defaultBOQsShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the bOQsList where code contains UPDATED_CODE
        defaultBOQsShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where code does not contain DEFAULT_CODE
        defaultBOQsShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the bOQsList where code does not contain UPDATED_CODE
        defaultBOQsShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBOQsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the bOQsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBOQsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBOQsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBOQsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the bOQsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBOQsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBOQsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        // Get all the bOQsList where isActive is not null
        defaultBOQsShouldBeFound("isActive.specified=true");

        // Get all the bOQsList where isActive is null
        defaultBOQsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBOQsByConstructorsIsEqualToSomething() throws Exception {
        ExUser constructors;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            bOQsRepository.saveAndFlush(bOQs);
            constructors = ExUserResourceIT.createEntity(em);
        } else {
            constructors = TestUtil.findAll(em, ExUser.class).get(0);
        }
        em.persist(constructors);
        em.flush();
        bOQs.setConstructors(constructors);
        bOQsRepository.saveAndFlush(bOQs);
        Long constructorsId = constructors.getId();

        // Get all the bOQsList where constructors equals to constructorsId
        defaultBOQsShouldBeFound("constructorsId.equals=" + constructorsId);

        // Get all the bOQsList where constructors equals to (constructorsId + 1)
        defaultBOQsShouldNotBeFound("constructorsId.equals=" + (constructorsId + 1));
    }

    @Test
    @Transactional
    void getAllBOQsByBoqDetailsIsEqualToSomething() throws Exception {
        BOQDetails boqDetails;
        if (TestUtil.findAll(em, BOQDetails.class).isEmpty()) {
            bOQsRepository.saveAndFlush(bOQs);
            boqDetails = BOQDetailsResourceIT.createEntity(em);
        } else {
            boqDetails = TestUtil.findAll(em, BOQDetails.class).get(0);
        }
        em.persist(boqDetails);
        em.flush();
        bOQs.addBoqDetails(boqDetails);
        bOQsRepository.saveAndFlush(bOQs);
        Long boqDetailsId = boqDetails.getId();

        // Get all the bOQsList where boqDetails equals to boqDetailsId
        defaultBOQsShouldBeFound("boqDetailsId.equals=" + boqDetailsId);

        // Get all the bOQsList where boqDetails equals to (boqDetailsId + 1)
        defaultBOQsShouldNotBeFound("boqDetailsId.equals=" + (boqDetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBOQsShouldBeFound(String filter) throws Exception {
        restBOQsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bOQs.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBOQsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBOQsShouldNotBeFound(String filter) throws Exception {
        restBOQsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBOQsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBOQs() throws Exception {
        // Get the bOQs
        restBOQsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBOQs() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();

        // Update the bOQs
        BOQs updatedBOQs = bOQsRepository.findById(bOQs.getId()).get();
        // Disconnect from session so that the updates on updatedBOQs are not directly saved in db
        em.detach(updatedBOQs);
        updatedBOQs.code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);

        restBOQsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBOQs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBOQs))
            )
            .andExpect(status().isOk());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
        BOQs testBOQs = bOQsList.get(bOQsList.size() - 1);
        assertThat(testBOQs.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBOQs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBOQs() throws Exception {
        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();
        bOQs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBOQsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bOQs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bOQs))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBOQs() throws Exception {
        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();
        bOQs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bOQs))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBOQs() throws Exception {
        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();
        bOQs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bOQs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBOQsWithPatch() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();

        // Update the bOQs using partial update
        BOQs partialUpdatedBOQs = new BOQs();
        partialUpdatedBOQs.setId(bOQs.getId());

        partialUpdatedBOQs.code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);

        restBOQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBOQs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBOQs))
            )
            .andExpect(status().isOk());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
        BOQs testBOQs = bOQsList.get(bOQsList.size() - 1);
        assertThat(testBOQs.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBOQs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBOQsWithPatch() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();

        // Update the bOQs using partial update
        BOQs partialUpdatedBOQs = new BOQs();
        partialUpdatedBOQs.setId(bOQs.getId());

        partialUpdatedBOQs.code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);

        restBOQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBOQs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBOQs))
            )
            .andExpect(status().isOk());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
        BOQs testBOQs = bOQsList.get(bOQsList.size() - 1);
        assertThat(testBOQs.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBOQs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBOQs() throws Exception {
        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();
        bOQs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBOQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bOQs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bOQs))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBOQs() throws Exception {
        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();
        bOQs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bOQs))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBOQs() throws Exception {
        int databaseSizeBeforeUpdate = bOQsRepository.findAll().size();
        bOQs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bOQs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BOQs in the database
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBOQs() throws Exception {
        // Initialize the database
        bOQsRepository.saveAndFlush(bOQs);

        int databaseSizeBeforeDelete = bOQsRepository.findAll().size();

        // Delete the bOQs
        restBOQsMockMvc
            .perform(delete(ENTITY_API_URL_ID, bOQs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BOQs> bOQsList = bOQsRepository.findAll();
        assertThat(bOQsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
