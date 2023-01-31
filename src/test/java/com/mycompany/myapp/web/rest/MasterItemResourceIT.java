package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.domain.MasterItem;
import com.mycompany.myapp.repository.MasterItemRepository;
import com.mycompany.myapp.service.MasterItemService;
import com.mycompany.myapp.service.criteria.MasterItemCriteria;
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
 * Integration tests for the {@link MasterItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MasterItemResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/master-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MasterItemRepository masterItemRepository;

    @Mock
    private MasterItemRepository masterItemRepositoryMock;

    @Mock
    private MasterItemService masterItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterItemMockMvc;

    private MasterItem masterItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterItem createEntity(EntityManager em) {
        MasterItem masterItem = new MasterItem()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return masterItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterItem createUpdatedEntity(EntityManager em) {
        MasterItem masterItem = new MasterItem()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE);
        return masterItem;
    }

    @BeforeEach
    public void initTest() {
        masterItem = createEntity(em);
    }

    @Test
    @Transactional
    void createMasterItem() throws Exception {
        int databaseSizeBeforeCreate = masterItemRepository.findAll().size();
        // Create the MasterItem
        restMasterItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterItem)))
            .andExpect(status().isCreated());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeCreate + 1);
        MasterItem testMasterItem = masterItemList.get(masterItemList.size() - 1);
        assertThat(testMasterItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMasterItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMasterItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMasterItem.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createMasterItemWithExistingId() throws Exception {
        // Create the MasterItem with an existing ID
        masterItem.setId(1L);

        int databaseSizeBeforeCreate = masterItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterItem)))
            .andExpect(status().isBadRequest());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterItemRepository.findAll().size();
        // set the field null
        masterItem.setCode(null);

        // Create the MasterItem, which fails.

        restMasterItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterItem)))
            .andExpect(status().isBadRequest());

        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterItemRepository.findAll().size();
        // set the field null
        masterItem.setName(null);

        // Create the MasterItem, which fails.

        restMasterItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterItem)))
            .andExpect(status().isBadRequest());

        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMasterItems() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList
        restMasterItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMasterItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(masterItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMasterItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(masterItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMasterItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(masterItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMasterItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(masterItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMasterItem() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get the masterItem
        restMasterItemMockMvc
            .perform(get(ENTITY_API_URL_ID, masterItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterItem.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getMasterItemsByIdFiltering() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        Long id = masterItem.getId();

        defaultMasterItemShouldBeFound("id.equals=" + id);
        defaultMasterItemShouldNotBeFound("id.notEquals=" + id);

        defaultMasterItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMasterItemShouldNotBeFound("id.greaterThan=" + id);

        defaultMasterItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMasterItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMasterItemsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where code equals to DEFAULT_CODE
        defaultMasterItemShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the masterItemList where code equals to UPDATED_CODE
        defaultMasterItemShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterItemsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where code in DEFAULT_CODE or UPDATED_CODE
        defaultMasterItemShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the masterItemList where code equals to UPDATED_CODE
        defaultMasterItemShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterItemsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where code is not null
        defaultMasterItemShouldBeFound("code.specified=true");

        // Get all the masterItemList where code is null
        defaultMasterItemShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterItemsByCodeContainsSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where code contains DEFAULT_CODE
        defaultMasterItemShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the masterItemList where code contains UPDATED_CODE
        defaultMasterItemShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterItemsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where code does not contain DEFAULT_CODE
        defaultMasterItemShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the masterItemList where code does not contain UPDATED_CODE
        defaultMasterItemShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMasterItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where name equals to DEFAULT_NAME
        defaultMasterItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the masterItemList where name equals to UPDATED_NAME
        defaultMasterItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMasterItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the masterItemList where name equals to UPDATED_NAME
        defaultMasterItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where name is not null
        defaultMasterItemShouldBeFound("name.specified=true");

        // Get all the masterItemList where name is null
        defaultMasterItemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterItemsByNameContainsSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where name contains DEFAULT_NAME
        defaultMasterItemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the masterItemList where name contains UPDATED_NAME
        defaultMasterItemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterItemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where name does not contain DEFAULT_NAME
        defaultMasterItemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the masterItemList where name does not contain UPDATED_NAME
        defaultMasterItemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMasterItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where description equals to DEFAULT_DESCRIPTION
        defaultMasterItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the masterItemList where description equals to UPDATED_DESCRIPTION
        defaultMasterItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMasterItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the masterItemList where description equals to UPDATED_DESCRIPTION
        defaultMasterItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where description is not null
        defaultMasterItemShouldBeFound("description.specified=true");

        // Get all the masterItemList where description is null
        defaultMasterItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where description contains DEFAULT_DESCRIPTION
        defaultMasterItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the masterItemList where description contains UPDATED_DESCRIPTION
        defaultMasterItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where description does not contain DEFAULT_DESCRIPTION
        defaultMasterItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the masterItemList where description does not contain UPDATED_DESCRIPTION
        defaultMasterItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMasterItemsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where isActive equals to DEFAULT_IS_ACTIVE
        defaultMasterItemShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the masterItemList where isActive equals to UPDATED_IS_ACTIVE
        defaultMasterItemShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMasterItemsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultMasterItemShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the masterItemList where isActive equals to UPDATED_IS_ACTIVE
        defaultMasterItemShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMasterItemsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        // Get all the masterItemList where isActive is not null
        defaultMasterItemShouldBeFound("isActive.specified=true");

        // Get all the masterItemList where isActive is null
        defaultMasterItemShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllMasterItemsByExUserIsEqualToSomething() throws Exception {
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            masterItemRepository.saveAndFlush(masterItem);
            exUser = ExUserResourceIT.createEntity(em);
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        em.persist(exUser);
        em.flush();
        masterItem.setExUser(exUser);
        masterItemRepository.saveAndFlush(masterItem);
        Long exUserId = exUser.getId();

        // Get all the masterItemList where exUser equals to exUserId
        defaultMasterItemShouldBeFound("exUserId.equals=" + exUserId);

        // Get all the masterItemList where exUser equals to (exUserId + 1)
        defaultMasterItemShouldNotBeFound("exUserId.equals=" + (exUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMasterItemShouldBeFound(String filter) throws Exception {
        restMasterItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restMasterItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMasterItemShouldNotBeFound(String filter) throws Exception {
        restMasterItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMasterItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMasterItem() throws Exception {
        // Get the masterItem
        restMasterItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMasterItem() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();

        // Update the masterItem
        MasterItem updatedMasterItem = masterItemRepository.findById(masterItem.getId()).get();
        // Disconnect from session so that the updates on updatedMasterItem are not directly saved in db
        em.detach(updatedMasterItem);
        updatedMasterItem.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restMasterItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMasterItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMasterItem))
            )
            .andExpect(status().isOk());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
        MasterItem testMasterItem = masterItemList.get(masterItemList.size() - 1);
        assertThat(testMasterItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMasterItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterItem.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingMasterItem() throws Exception {
        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();
        masterItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterItem() throws Exception {
        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();
        masterItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(masterItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterItem() throws Exception {
        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();
        masterItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(masterItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterItemWithPatch() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();

        // Update the masterItem using partial update
        MasterItem partialUpdatedMasterItem = new MasterItem();
        partialUpdatedMasterItem.setId(masterItem.getId());

        partialUpdatedMasterItem.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restMasterItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterItem))
            )
            .andExpect(status().isOk());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
        MasterItem testMasterItem = masterItemList.get(masterItemList.size() - 1);
        assertThat(testMasterItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMasterItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterItem.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateMasterItemWithPatch() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();

        // Update the masterItem using partial update
        MasterItem partialUpdatedMasterItem = new MasterItem();
        partialUpdatedMasterItem.setId(masterItem.getId());

        partialUpdatedMasterItem.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restMasterItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMasterItem))
            )
            .andExpect(status().isOk());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
        MasterItem testMasterItem = masterItemList.get(masterItemList.size() - 1);
        assertThat(testMasterItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMasterItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMasterItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMasterItem.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingMasterItem() throws Exception {
        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();
        masterItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterItem() throws Exception {
        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();
        masterItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(masterItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterItem() throws Exception {
        int databaseSizeBeforeUpdate = masterItemRepository.findAll().size();
        masterItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(masterItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterItem in the database
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterItem() throws Exception {
        // Initialize the database
        masterItemRepository.saveAndFlush(masterItem);

        int databaseSizeBeforeDelete = masterItemRepository.findAll().size();

        // Delete the masterItem
        restMasterItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MasterItem> masterItemList = masterItemRepository.findAll();
        assertThat(masterItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
