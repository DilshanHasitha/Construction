package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BOQDetails;
import com.mycompany.myapp.domain.BOQs;
import com.mycompany.myapp.domain.MasterItem;
import com.mycompany.myapp.domain.UnitOfMeasure;
import com.mycompany.myapp.repository.BOQDetailsRepository;
import com.mycompany.myapp.service.BOQDetailsService;
import com.mycompany.myapp.service.criteria.BOQDetailsCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link BOQDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BOQDetailsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ORDER_PLACED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_PLACED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ORDER_PLACED_ON = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_QTY = 1D;
    private static final Double UPDATED_QTY = 2D;
    private static final Double SMALLER_QTY = 1D - 1D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/boq-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BOQDetailsRepository bOQDetailsRepository;

    @Mock
    private BOQDetailsRepository bOQDetailsRepositoryMock;

    @Mock
    private BOQDetailsService bOQDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBOQDetailsMockMvc;

    private BOQDetails bOQDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BOQDetails createEntity(EntityManager em) {
        BOQDetails bOQDetails = new BOQDetails()
            .code(DEFAULT_CODE)
            .orderPlacedOn(DEFAULT_ORDER_PLACED_ON)
            .qty(DEFAULT_QTY)
            .isActive(DEFAULT_IS_ACTIVE);
        return bOQDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BOQDetails createUpdatedEntity(EntityManager em) {
        BOQDetails bOQDetails = new BOQDetails()
            .code(UPDATED_CODE)
            .orderPlacedOn(UPDATED_ORDER_PLACED_ON)
            .qty(UPDATED_QTY)
            .isActive(UPDATED_IS_ACTIVE);
        return bOQDetails;
    }

    @BeforeEach
    public void initTest() {
        bOQDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createBOQDetails() throws Exception {
        int databaseSizeBeforeCreate = bOQDetailsRepository.findAll().size();
        // Create the BOQDetails
        restBOQDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bOQDetails)))
            .andExpect(status().isCreated());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BOQDetails testBOQDetails = bOQDetailsList.get(bOQDetailsList.size() - 1);
        assertThat(testBOQDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBOQDetails.getOrderPlacedOn()).isEqualTo(DEFAULT_ORDER_PLACED_ON);
        assertThat(testBOQDetails.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testBOQDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createBOQDetailsWithExistingId() throws Exception {
        // Create the BOQDetails with an existing ID
        bOQDetails.setId(1L);

        int databaseSizeBeforeCreate = bOQDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBOQDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bOQDetails)))
            .andExpect(status().isBadRequest());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bOQDetailsRepository.findAll().size();
        // set the field null
        bOQDetails.setCode(null);

        // Create the BOQDetails, which fails.

        restBOQDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bOQDetails)))
            .andExpect(status().isBadRequest());

        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBOQDetails() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList
        restBOQDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bOQDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].orderPlacedOn").value(hasItem(DEFAULT_ORDER_PLACED_ON.toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBOQDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bOQDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBOQDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bOQDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBOQDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bOQDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBOQDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bOQDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBOQDetails() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get the bOQDetails
        restBOQDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, bOQDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bOQDetails.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.orderPlacedOn").value(DEFAULT_ORDER_PLACED_ON.toString()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getBOQDetailsByIdFiltering() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        Long id = bOQDetails.getId();

        defaultBOQDetailsShouldBeFound("id.equals=" + id);
        defaultBOQDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultBOQDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBOQDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultBOQDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBOQDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where code equals to DEFAULT_CODE
        defaultBOQDetailsShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the bOQDetailsList where code equals to UPDATED_CODE
        defaultBOQDetailsShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBOQDetailsShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the bOQDetailsList where code equals to UPDATED_CODE
        defaultBOQDetailsShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where code is not null
        defaultBOQDetailsShouldBeFound("code.specified=true");

        // Get all the bOQDetailsList where code is null
        defaultBOQDetailsShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBOQDetailsByCodeContainsSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where code contains DEFAULT_CODE
        defaultBOQDetailsShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the bOQDetailsList where code contains UPDATED_CODE
        defaultBOQDetailsShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where code does not contain DEFAULT_CODE
        defaultBOQDetailsShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the bOQDetailsList where code does not contain UPDATED_CODE
        defaultBOQDetailsShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByOrderPlacedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where orderPlacedOn equals to DEFAULT_ORDER_PLACED_ON
        defaultBOQDetailsShouldBeFound("orderPlacedOn.equals=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the bOQDetailsList where orderPlacedOn equals to UPDATED_ORDER_PLACED_ON
        defaultBOQDetailsShouldNotBeFound("orderPlacedOn.equals=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByOrderPlacedOnIsInShouldWork() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where orderPlacedOn in DEFAULT_ORDER_PLACED_ON or UPDATED_ORDER_PLACED_ON
        defaultBOQDetailsShouldBeFound("orderPlacedOn.in=" + DEFAULT_ORDER_PLACED_ON + "," + UPDATED_ORDER_PLACED_ON);

        // Get all the bOQDetailsList where orderPlacedOn equals to UPDATED_ORDER_PLACED_ON
        defaultBOQDetailsShouldNotBeFound("orderPlacedOn.in=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByOrderPlacedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where orderPlacedOn is not null
        defaultBOQDetailsShouldBeFound("orderPlacedOn.specified=true");

        // Get all the bOQDetailsList where orderPlacedOn is null
        defaultBOQDetailsShouldNotBeFound("orderPlacedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllBOQDetailsByOrderPlacedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where orderPlacedOn is greater than or equal to DEFAULT_ORDER_PLACED_ON
        defaultBOQDetailsShouldBeFound("orderPlacedOn.greaterThanOrEqual=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the bOQDetailsList where orderPlacedOn is greater than or equal to UPDATED_ORDER_PLACED_ON
        defaultBOQDetailsShouldNotBeFound("orderPlacedOn.greaterThanOrEqual=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByOrderPlacedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where orderPlacedOn is less than or equal to DEFAULT_ORDER_PLACED_ON
        defaultBOQDetailsShouldBeFound("orderPlacedOn.lessThanOrEqual=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the bOQDetailsList where orderPlacedOn is less than or equal to SMALLER_ORDER_PLACED_ON
        defaultBOQDetailsShouldNotBeFound("orderPlacedOn.lessThanOrEqual=" + SMALLER_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByOrderPlacedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where orderPlacedOn is less than DEFAULT_ORDER_PLACED_ON
        defaultBOQDetailsShouldNotBeFound("orderPlacedOn.lessThan=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the bOQDetailsList where orderPlacedOn is less than UPDATED_ORDER_PLACED_ON
        defaultBOQDetailsShouldBeFound("orderPlacedOn.lessThan=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByOrderPlacedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where orderPlacedOn is greater than DEFAULT_ORDER_PLACED_ON
        defaultBOQDetailsShouldNotBeFound("orderPlacedOn.greaterThan=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the bOQDetailsList where orderPlacedOn is greater than SMALLER_ORDER_PLACED_ON
        defaultBOQDetailsShouldBeFound("orderPlacedOn.greaterThan=" + SMALLER_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where qty equals to DEFAULT_QTY
        defaultBOQDetailsShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the bOQDetailsList where qty equals to UPDATED_QTY
        defaultBOQDetailsShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultBOQDetailsShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the bOQDetailsList where qty equals to UPDATED_QTY
        defaultBOQDetailsShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where qty is not null
        defaultBOQDetailsShouldBeFound("qty.specified=true");

        // Get all the bOQDetailsList where qty is null
        defaultBOQDetailsShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllBOQDetailsByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where qty is greater than or equal to DEFAULT_QTY
        defaultBOQDetailsShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the bOQDetailsList where qty is greater than or equal to UPDATED_QTY
        defaultBOQDetailsShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where qty is less than or equal to DEFAULT_QTY
        defaultBOQDetailsShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the bOQDetailsList where qty is less than or equal to SMALLER_QTY
        defaultBOQDetailsShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where qty is less than DEFAULT_QTY
        defaultBOQDetailsShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the bOQDetailsList where qty is less than UPDATED_QTY
        defaultBOQDetailsShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where qty is greater than DEFAULT_QTY
        defaultBOQDetailsShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the bOQDetailsList where qty is greater than SMALLER_QTY
        defaultBOQDetailsShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultBOQDetailsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the bOQDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBOQDetailsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultBOQDetailsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the bOQDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultBOQDetailsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBOQDetailsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        // Get all the bOQDetailsList where isActive is not null
        defaultBOQDetailsShouldBeFound("isActive.specified=true");

        // Get all the bOQDetailsList where isActive is null
        defaultBOQDetailsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllBOQDetailsByItemIsEqualToSomething() throws Exception {
        MasterItem item;
        if (TestUtil.findAll(em, MasterItem.class).isEmpty()) {
            bOQDetailsRepository.saveAndFlush(bOQDetails);
            item = MasterItemResourceIT.createEntity(em);
        } else {
            item = TestUtil.findAll(em, MasterItem.class).get(0);
        }
        em.persist(item);
        em.flush();
        bOQDetails.setItem(item);
        bOQDetailsRepository.saveAndFlush(bOQDetails);
        Long itemId = item.getId();

        // Get all the bOQDetailsList where item equals to itemId
        defaultBOQDetailsShouldBeFound("itemId.equals=" + itemId);

        // Get all the bOQDetailsList where item equals to (itemId + 1)
        defaultBOQDetailsShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    @Test
    @Transactional
    void getAllBOQDetailsByPerIsEqualToSomething() throws Exception {
        UnitOfMeasure per;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            bOQDetailsRepository.saveAndFlush(bOQDetails);
            per = UnitOfMeasureResourceIT.createEntity(em);
        } else {
            per = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        em.persist(per);
        em.flush();
        bOQDetails.setPer(per);
        bOQDetailsRepository.saveAndFlush(bOQDetails);
        Long perId = per.getId();

        // Get all the bOQDetailsList where per equals to perId
        defaultBOQDetailsShouldBeFound("perId.equals=" + perId);

        // Get all the bOQDetailsList where per equals to (perId + 1)
        defaultBOQDetailsShouldNotBeFound("perId.equals=" + (perId + 1));
    }

    @Test
    @Transactional
    void getAllBOQDetailsByUnitIsEqualToSomething() throws Exception {
        UnitOfMeasure unit;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            bOQDetailsRepository.saveAndFlush(bOQDetails);
            unit = UnitOfMeasureResourceIT.createEntity(em);
        } else {
            unit = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        em.persist(unit);
        em.flush();
        bOQDetails.setUnit(unit);
        bOQDetailsRepository.saveAndFlush(bOQDetails);
        Long unitId = unit.getId();

        // Get all the bOQDetailsList where unit equals to unitId
        defaultBOQDetailsShouldBeFound("unitId.equals=" + unitId);

        // Get all the bOQDetailsList where unit equals to (unitId + 1)
        defaultBOQDetailsShouldNotBeFound("unitId.equals=" + (unitId + 1));
    }

    @Test
    @Transactional
    void getAllBOQDetailsByBoqsIsEqualToSomething() throws Exception {
        BOQs boqs;
        if (TestUtil.findAll(em, BOQs.class).isEmpty()) {
            bOQDetailsRepository.saveAndFlush(bOQDetails);
            boqs = BOQsResourceIT.createEntity(em);
        } else {
            boqs = TestUtil.findAll(em, BOQs.class).get(0);
        }
        em.persist(boqs);
        em.flush();
        bOQDetails.addBoqs(boqs);
        bOQDetailsRepository.saveAndFlush(bOQDetails);
        Long boqsId = boqs.getId();

        // Get all the bOQDetailsList where boqs equals to boqsId
        defaultBOQDetailsShouldBeFound("boqsId.equals=" + boqsId);

        // Get all the bOQDetailsList where boqs equals to (boqsId + 1)
        defaultBOQDetailsShouldNotBeFound("boqsId.equals=" + (boqsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBOQDetailsShouldBeFound(String filter) throws Exception {
        restBOQDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bOQDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].orderPlacedOn").value(hasItem(DEFAULT_ORDER_PLACED_ON.toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restBOQDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBOQDetailsShouldNotBeFound(String filter) throws Exception {
        restBOQDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBOQDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBOQDetails() throws Exception {
        // Get the bOQDetails
        restBOQDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBOQDetails() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();

        // Update the bOQDetails
        BOQDetails updatedBOQDetails = bOQDetailsRepository.findById(bOQDetails.getId()).get();
        // Disconnect from session so that the updates on updatedBOQDetails are not directly saved in db
        em.detach(updatedBOQDetails);
        updatedBOQDetails.code(UPDATED_CODE).orderPlacedOn(UPDATED_ORDER_PLACED_ON).qty(UPDATED_QTY).isActive(UPDATED_IS_ACTIVE);

        restBOQDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBOQDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBOQDetails))
            )
            .andExpect(status().isOk());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
        BOQDetails testBOQDetails = bOQDetailsList.get(bOQDetailsList.size() - 1);
        assertThat(testBOQDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBOQDetails.getOrderPlacedOn()).isEqualTo(UPDATED_ORDER_PLACED_ON);
        assertThat(testBOQDetails.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testBOQDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingBOQDetails() throws Exception {
        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();
        bOQDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBOQDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bOQDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bOQDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBOQDetails() throws Exception {
        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();
        bOQDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bOQDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBOQDetails() throws Exception {
        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();
        bOQDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bOQDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBOQDetailsWithPatch() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();

        // Update the bOQDetails using partial update
        BOQDetails partialUpdatedBOQDetails = new BOQDetails();
        partialUpdatedBOQDetails.setId(bOQDetails.getId());

        partialUpdatedBOQDetails.code(UPDATED_CODE).orderPlacedOn(UPDATED_ORDER_PLACED_ON).isActive(UPDATED_IS_ACTIVE);

        restBOQDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBOQDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBOQDetails))
            )
            .andExpect(status().isOk());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
        BOQDetails testBOQDetails = bOQDetailsList.get(bOQDetailsList.size() - 1);
        assertThat(testBOQDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBOQDetails.getOrderPlacedOn()).isEqualTo(UPDATED_ORDER_PLACED_ON);
        assertThat(testBOQDetails.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testBOQDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateBOQDetailsWithPatch() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();

        // Update the bOQDetails using partial update
        BOQDetails partialUpdatedBOQDetails = new BOQDetails();
        partialUpdatedBOQDetails.setId(bOQDetails.getId());

        partialUpdatedBOQDetails.code(UPDATED_CODE).orderPlacedOn(UPDATED_ORDER_PLACED_ON).qty(UPDATED_QTY).isActive(UPDATED_IS_ACTIVE);

        restBOQDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBOQDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBOQDetails))
            )
            .andExpect(status().isOk());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
        BOQDetails testBOQDetails = bOQDetailsList.get(bOQDetailsList.size() - 1);
        assertThat(testBOQDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBOQDetails.getOrderPlacedOn()).isEqualTo(UPDATED_ORDER_PLACED_ON);
        assertThat(testBOQDetails.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testBOQDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingBOQDetails() throws Exception {
        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();
        bOQDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBOQDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bOQDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bOQDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBOQDetails() throws Exception {
        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();
        bOQDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bOQDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBOQDetails() throws Exception {
        int databaseSizeBeforeUpdate = bOQDetailsRepository.findAll().size();
        bOQDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBOQDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bOQDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BOQDetails in the database
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBOQDetails() throws Exception {
        // Initialize the database
        bOQDetailsRepository.saveAndFlush(bOQDetails);

        int databaseSizeBeforeDelete = bOQDetailsRepository.findAll().size();

        // Delete the bOQDetails
        restBOQDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, bOQDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BOQDetails> bOQDetailsList = bOQDetailsRepository.findAll();
        assertThat(bOQDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
