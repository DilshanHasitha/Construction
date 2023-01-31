package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.domain.OrderDetails;
import com.mycompany.myapp.domain.OrderStatus;
import com.mycompany.myapp.domain.Orders;
import com.mycompany.myapp.repository.OrdersRepository;
import com.mycompany.myapp.service.OrdersService;
import com.mycompany.myapp.service.criteria.OrdersCriteria;
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
 * Integration tests for the {@link OrdersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrdersResourceIT {

    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_ORDER_PLACED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_PLACED_ON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ORDER_PLACED_ON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdersRepository ordersRepository;

    @Mock
    private OrdersRepository ordersRepositoryMock;

    @Mock
    private OrdersService ordersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdersMockMvc;

    private Orders orders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createEntity(EntityManager em) {
        Orders orders = new Orders()
            .orderID(DEFAULT_ORDER_ID)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
            .orderPlacedOn(DEFAULT_ORDER_PLACED_ON)
            .note(DEFAULT_NOTE);
        return orders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createUpdatedEntity(EntityManager em) {
        Orders orders = new Orders()
            .orderID(UPDATED_ORDER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .orderPlacedOn(UPDATED_ORDER_PLACED_ON)
            .note(UPDATED_NOTE);
        return orders;
    }

    @BeforeEach
    public void initTest() {
        orders = createEntity(em);
    }

    @Test
    @Transactional
    void createOrders() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();
        // Create the Orders
        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderID()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrders.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testOrders.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testOrders.getOrderPlacedOn()).isEqualTo(DEFAULT_ORDER_PLACED_ON);
        assertThat(testOrders.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createOrdersWithExistingId() throws Exception {
        // Create the Orders with an existing ID
        orders.setId(1L);

        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setOrderID(null);

        // Create the Orders, which fails.

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setCustomerName(null);

        // Create the Orders, which fails.

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderID").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].orderPlacedOn").value(hasItem(DEFAULT_ORDER_PLACED_ON.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(ordersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ordersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ordersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ordersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL_ID, orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.orderID").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.orderPlacedOn").value(DEFAULT_ORDER_PLACED_ON.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        Long id = orders.getId();

        defaultOrdersShouldBeFound("id.equals=" + id);
        defaultOrdersShouldNotBeFound("id.notEquals=" + id);

        defaultOrdersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrdersShouldNotBeFound("id.greaterThan=" + id);

        defaultOrdersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrdersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderIDIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderID equals to DEFAULT_ORDER_ID
        defaultOrdersShouldBeFound("orderID.equals=" + DEFAULT_ORDER_ID);

        // Get all the ordersList where orderID equals to UPDATED_ORDER_ID
        defaultOrdersShouldNotBeFound("orderID.equals=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderIDIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderID in DEFAULT_ORDER_ID or UPDATED_ORDER_ID
        defaultOrdersShouldBeFound("orderID.in=" + DEFAULT_ORDER_ID + "," + UPDATED_ORDER_ID);

        // Get all the ordersList where orderID equals to UPDATED_ORDER_ID
        defaultOrdersShouldNotBeFound("orderID.in=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderID is not null
        defaultOrdersShouldBeFound("orderID.specified=true");

        // Get all the ordersList where orderID is null
        defaultOrdersShouldNotBeFound("orderID.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderIDContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderID contains DEFAULT_ORDER_ID
        defaultOrdersShouldBeFound("orderID.contains=" + DEFAULT_ORDER_ID);

        // Get all the ordersList where orderID contains UPDATED_ORDER_ID
        defaultOrdersShouldNotBeFound("orderID.contains=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderIDNotContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderID does not contain DEFAULT_ORDER_ID
        defaultOrdersShouldNotBeFound("orderID.doesNotContain=" + DEFAULT_ORDER_ID);

        // Get all the ordersList where orderID does not contain UPDATED_ORDER_ID
        defaultOrdersShouldBeFound("orderID.doesNotContain=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllOrdersByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultOrdersShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the ordersList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultOrdersShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultOrdersShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the ordersList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultOrdersShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName is not null
        defaultOrdersShouldBeFound("customerName.specified=true");

        // Get all the ordersList where customerName is null
        defaultOrdersShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultOrdersShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the ordersList where customerName contains UPDATED_CUSTOMER_NAME
        defaultOrdersShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultOrdersShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the ordersList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultOrdersShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllOrdersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where isActive equals to DEFAULT_IS_ACTIVE
        defaultOrdersShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the ordersList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrdersShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOrdersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultOrdersShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the ordersList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrdersShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOrdersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where isActive is not null
        defaultOrdersShouldBeFound("isActive.specified=true");

        // Get all the ordersList where isActive is null
        defaultOrdersShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderPlacedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderPlacedOn equals to DEFAULT_ORDER_PLACED_ON
        defaultOrdersShouldBeFound("orderPlacedOn.equals=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the ordersList where orderPlacedOn equals to UPDATED_ORDER_PLACED_ON
        defaultOrdersShouldNotBeFound("orderPlacedOn.equals=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderPlacedOnIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderPlacedOn in DEFAULT_ORDER_PLACED_ON or UPDATED_ORDER_PLACED_ON
        defaultOrdersShouldBeFound("orderPlacedOn.in=" + DEFAULT_ORDER_PLACED_ON + "," + UPDATED_ORDER_PLACED_ON);

        // Get all the ordersList where orderPlacedOn equals to UPDATED_ORDER_PLACED_ON
        defaultOrdersShouldNotBeFound("orderPlacedOn.in=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderPlacedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderPlacedOn is not null
        defaultOrdersShouldBeFound("orderPlacedOn.specified=true");

        // Get all the ordersList where orderPlacedOn is null
        defaultOrdersShouldNotBeFound("orderPlacedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderPlacedOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderPlacedOn is greater than or equal to DEFAULT_ORDER_PLACED_ON
        defaultOrdersShouldBeFound("orderPlacedOn.greaterThanOrEqual=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the ordersList where orderPlacedOn is greater than or equal to UPDATED_ORDER_PLACED_ON
        defaultOrdersShouldNotBeFound("orderPlacedOn.greaterThanOrEqual=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderPlacedOnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderPlacedOn is less than or equal to DEFAULT_ORDER_PLACED_ON
        defaultOrdersShouldBeFound("orderPlacedOn.lessThanOrEqual=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the ordersList where orderPlacedOn is less than or equal to SMALLER_ORDER_PLACED_ON
        defaultOrdersShouldNotBeFound("orderPlacedOn.lessThanOrEqual=" + SMALLER_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderPlacedOnIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderPlacedOn is less than DEFAULT_ORDER_PLACED_ON
        defaultOrdersShouldNotBeFound("orderPlacedOn.lessThan=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the ordersList where orderPlacedOn is less than UPDATED_ORDER_PLACED_ON
        defaultOrdersShouldBeFound("orderPlacedOn.lessThan=" + UPDATED_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderPlacedOnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderPlacedOn is greater than DEFAULT_ORDER_PLACED_ON
        defaultOrdersShouldNotBeFound("orderPlacedOn.greaterThan=" + DEFAULT_ORDER_PLACED_ON);

        // Get all the ordersList where orderPlacedOn is greater than SMALLER_ORDER_PLACED_ON
        defaultOrdersShouldBeFound("orderPlacedOn.greaterThan=" + SMALLER_ORDER_PLACED_ON);
    }

    @Test
    @Transactional
    void getAllOrdersByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note equals to DEFAULT_NOTE
        defaultOrdersShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the ordersList where note equals to UPDATED_NOTE
        defaultOrdersShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrdersByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultOrdersShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the ordersList where note equals to UPDATED_NOTE
        defaultOrdersShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrdersByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note is not null
        defaultOrdersShouldBeFound("note.specified=true");

        // Get all the ordersList where note is null
        defaultOrdersShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByNoteContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note contains DEFAULT_NOTE
        defaultOrdersShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the ordersList where note contains UPDATED_NOTE
        defaultOrdersShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrdersByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where note does not contain DEFAULT_NOTE
        defaultOrdersShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the ordersList where note does not contain UPDATED_NOTE
        defaultOrdersShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrdersByExUserIsEqualToSomething() throws Exception {
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            exUser = ExUserResourceIT.createEntity(em);
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        em.persist(exUser);
        em.flush();
        orders.setExUser(exUser);
        ordersRepository.saveAndFlush(orders);
        Long exUserId = exUser.getId();

        // Get all the ordersList where exUser equals to exUserId
        defaultOrdersShouldBeFound("exUserId.equals=" + exUserId);

        // Get all the ordersList where exUser equals to (exUserId + 1)
        defaultOrdersShouldNotBeFound("exUserId.equals=" + (exUserId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStatusIsEqualToSomething() throws Exception {
        OrderStatus orderStatus;
        if (TestUtil.findAll(em, OrderStatus.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            orderStatus = OrderStatusResourceIT.createEntity(em);
        } else {
            orderStatus = TestUtil.findAll(em, OrderStatus.class).get(0);
        }
        em.persist(orderStatus);
        em.flush();
        orders.setOrderStatus(orderStatus);
        ordersRepository.saveAndFlush(orders);
        Long orderStatusId = orderStatus.getId();

        // Get all the ordersList where orderStatus equals to orderStatusId
        defaultOrdersShouldBeFound("orderStatusId.equals=" + orderStatusId);

        // Get all the ordersList where orderStatus equals to (orderStatusId + 1)
        defaultOrdersShouldNotBeFound("orderStatusId.equals=" + (orderStatusId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByOrderDetailsIsEqualToSomething() throws Exception {
        OrderDetails orderDetails;
        if (TestUtil.findAll(em, OrderDetails.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            orderDetails = OrderDetailsResourceIT.createEntity(em);
        } else {
            orderDetails = TestUtil.findAll(em, OrderDetails.class).get(0);
        }
        em.persist(orderDetails);
        em.flush();
        orders.addOrderDetails(orderDetails);
        ordersRepository.saveAndFlush(orders);
        Long orderDetailsId = orderDetails.getId();

        // Get all the ordersList where orderDetails equals to orderDetailsId
        defaultOrdersShouldBeFound("orderDetailsId.equals=" + orderDetailsId);

        // Get all the ordersList where orderDetails equals to (orderDetailsId + 1)
        defaultOrdersShouldNotBeFound("orderDetailsId.equals=" + (orderDetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrdersShouldBeFound(String filter) throws Exception {
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderID").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].orderPlacedOn").value(hasItem(DEFAULT_ORDER_PLACED_ON.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrdersShouldNotBeFound(String filter) throws Exception {
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = ordersRepository.findById(orders.getId()).get();
        // Disconnect from session so that the updates on updatedOrders are not directly saved in db
        em.detach(updatedOrders);
        updatedOrders
            .orderID(UPDATED_ORDER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .orderPlacedOn(UPDATED_ORDER_PLACED_ON)
            .note(UPDATED_NOTE);

        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderID()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrders.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testOrders.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOrders.getOrderPlacedOn()).isEqualTo(UPDATED_ORDER_PLACED_ON);
        assertThat(testOrders.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdersWithPatch() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders using partial update
        Orders partialUpdatedOrders = new Orders();
        partialUpdatedOrders.setId(orders.getId());

        partialUpdatedOrders.orderID(UPDATED_ORDER_ID).isActive(UPDATED_IS_ACTIVE).note(UPDATED_NOTE);

        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderID()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrders.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testOrders.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOrders.getOrderPlacedOn()).isEqualTo(DEFAULT_ORDER_PLACED_ON);
        assertThat(testOrders.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateOrdersWithPatch() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders using partial update
        Orders partialUpdatedOrders = new Orders();
        partialUpdatedOrders.setId(orders.getId());

        partialUpdatedOrders
            .orderID(UPDATED_ORDER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .orderPlacedOn(UPDATED_ORDER_PLACED_ON)
            .note(UPDATED_NOTE);

        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderID()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrders.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testOrders.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testOrders.getOrderPlacedOn()).isEqualTo(UPDATED_ORDER_PLACED_ON);
        assertThat(testOrders.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Delete the orders
        restOrdersMockMvc
            .perform(delete(ENTITY_API_URL_ID, orders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
