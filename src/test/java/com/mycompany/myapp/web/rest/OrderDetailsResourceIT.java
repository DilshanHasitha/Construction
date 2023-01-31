package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Item;
import com.mycompany.myapp.domain.OrderDetails;
import com.mycompany.myapp.domain.Orders;
import com.mycompany.myapp.repository.OrderDetailsRepository;
import com.mycompany.myapp.service.criteria.OrderDetailsCriteria;
import java.math.BigDecimal;
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
 * Integration tests for the {@link OrderDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderDetailsResourceIT {

    private static final Double DEFAULT_ORDER_QTY = 1D;
    private static final Double UPDATED_ORDER_QTY = 2D;
    private static final Double SMALLER_ORDER_QTY = 1D - 1D;

    private static final BigDecimal DEFAULT_REVISED_ITEM_SALES_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVISED_ITEM_SALES_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_REVISED_ITEM_SALES_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderDetailsMockMvc;

    private OrderDetails orderDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderDetails createEntity(EntityManager em) {
        OrderDetails orderDetails = new OrderDetails()
            .orderQty(DEFAULT_ORDER_QTY)
            .revisedItemSalesPrice(DEFAULT_REVISED_ITEM_SALES_PRICE)
            .note(DEFAULT_NOTE);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        orderDetails.setItem(item);
        return orderDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderDetails createUpdatedEntity(EntityManager em) {
        OrderDetails orderDetails = new OrderDetails()
            .orderQty(UPDATED_ORDER_QTY)
            .revisedItemSalesPrice(UPDATED_REVISED_ITEM_SALES_PRICE)
            .note(UPDATED_NOTE);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createUpdatedEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        orderDetails.setItem(item);
        return orderDetails;
    }

    @BeforeEach
    public void initTest() {
        orderDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = orderDetailsRepository.findAll().size();
        // Create the OrderDetails
        restOrderDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDetails)))
            .andExpect(status().isCreated());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderQty()).isEqualTo(DEFAULT_ORDER_QTY);
        assertThat(testOrderDetails.getRevisedItemSalesPrice()).isEqualByComparingTo(DEFAULT_REVISED_ITEM_SALES_PRICE);
        assertThat(testOrderDetails.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createOrderDetailsWithExistingId() throws Exception {
        // Create the OrderDetails with an existing ID
        orderDetails.setId(1L);

        int databaseSizeBeforeCreate = orderDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDetails)))
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderDetailsRepository.findAll().size();
        // set the field null
        orderDetails.setOrderQty(null);

        // Create the OrderDetails, which fails.

        restOrderDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDetails)))
            .andExpect(status().isBadRequest());

        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderQty").value(hasItem(DEFAULT_ORDER_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].revisedItemSalesPrice").value(hasItem(sameNumber(DEFAULT_REVISED_ITEM_SALES_PRICE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get the orderDetails
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, orderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderDetails.getId().intValue()))
            .andExpect(jsonPath("$.orderQty").value(DEFAULT_ORDER_QTY.doubleValue()))
            .andExpect(jsonPath("$.revisedItemSalesPrice").value(sameNumber(DEFAULT_REVISED_ITEM_SALES_PRICE)))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getOrderDetailsByIdFiltering() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        Long id = orderDetails.getId();

        defaultOrderDetailsShouldBeFound("id.equals=" + id);
        defaultOrderDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultOrderDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderQty equals to DEFAULT_ORDER_QTY
        defaultOrderDetailsShouldBeFound("orderQty.equals=" + DEFAULT_ORDER_QTY);

        // Get all the orderDetailsList where orderQty equals to UPDATED_ORDER_QTY
        defaultOrderDetailsShouldNotBeFound("orderQty.equals=" + UPDATED_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderQtyIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderQty in DEFAULT_ORDER_QTY or UPDATED_ORDER_QTY
        defaultOrderDetailsShouldBeFound("orderQty.in=" + DEFAULT_ORDER_QTY + "," + UPDATED_ORDER_QTY);

        // Get all the orderDetailsList where orderQty equals to UPDATED_ORDER_QTY
        defaultOrderDetailsShouldNotBeFound("orderQty.in=" + UPDATED_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderQty is not null
        defaultOrderDetailsShouldBeFound("orderQty.specified=true");

        // Get all the orderDetailsList where orderQty is null
        defaultOrderDetailsShouldNotBeFound("orderQty.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderQty is greater than or equal to DEFAULT_ORDER_QTY
        defaultOrderDetailsShouldBeFound("orderQty.greaterThanOrEqual=" + DEFAULT_ORDER_QTY);

        // Get all the orderDetailsList where orderQty is greater than or equal to UPDATED_ORDER_QTY
        defaultOrderDetailsShouldNotBeFound("orderQty.greaterThanOrEqual=" + UPDATED_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderQty is less than or equal to DEFAULT_ORDER_QTY
        defaultOrderDetailsShouldBeFound("orderQty.lessThanOrEqual=" + DEFAULT_ORDER_QTY);

        // Get all the orderDetailsList where orderQty is less than or equal to SMALLER_ORDER_QTY
        defaultOrderDetailsShouldNotBeFound("orderQty.lessThanOrEqual=" + SMALLER_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderQty is less than DEFAULT_ORDER_QTY
        defaultOrderDetailsShouldNotBeFound("orderQty.lessThan=" + DEFAULT_ORDER_QTY);

        // Get all the orderDetailsList where orderQty is less than UPDATED_ORDER_QTY
        defaultOrderDetailsShouldBeFound("orderQty.lessThan=" + UPDATED_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrderQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where orderQty is greater than DEFAULT_ORDER_QTY
        defaultOrderDetailsShouldNotBeFound("orderQty.greaterThan=" + DEFAULT_ORDER_QTY);

        // Get all the orderDetailsList where orderQty is greater than SMALLER_ORDER_QTY
        defaultOrderDetailsShouldBeFound("orderQty.greaterThan=" + SMALLER_ORDER_QTY);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByRevisedItemSalesPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where revisedItemSalesPrice equals to DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldBeFound("revisedItemSalesPrice.equals=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the orderDetailsList where revisedItemSalesPrice equals to UPDATED_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldNotBeFound("revisedItemSalesPrice.equals=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByRevisedItemSalesPriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where revisedItemSalesPrice in DEFAULT_REVISED_ITEM_SALES_PRICE or UPDATED_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldBeFound(
            "revisedItemSalesPrice.in=" + DEFAULT_REVISED_ITEM_SALES_PRICE + "," + UPDATED_REVISED_ITEM_SALES_PRICE
        );

        // Get all the orderDetailsList where revisedItemSalesPrice equals to UPDATED_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldNotBeFound("revisedItemSalesPrice.in=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByRevisedItemSalesPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where revisedItemSalesPrice is not null
        defaultOrderDetailsShouldBeFound("revisedItemSalesPrice.specified=true");

        // Get all the orderDetailsList where revisedItemSalesPrice is null
        defaultOrderDetailsShouldNotBeFound("revisedItemSalesPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByRevisedItemSalesPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where revisedItemSalesPrice is greater than or equal to DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldBeFound("revisedItemSalesPrice.greaterThanOrEqual=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the orderDetailsList where revisedItemSalesPrice is greater than or equal to UPDATED_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldNotBeFound("revisedItemSalesPrice.greaterThanOrEqual=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByRevisedItemSalesPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where revisedItemSalesPrice is less than or equal to DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldBeFound("revisedItemSalesPrice.lessThanOrEqual=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the orderDetailsList where revisedItemSalesPrice is less than or equal to SMALLER_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldNotBeFound("revisedItemSalesPrice.lessThanOrEqual=" + SMALLER_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByRevisedItemSalesPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where revisedItemSalesPrice is less than DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldNotBeFound("revisedItemSalesPrice.lessThan=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the orderDetailsList where revisedItemSalesPrice is less than UPDATED_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldBeFound("revisedItemSalesPrice.lessThan=" + UPDATED_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByRevisedItemSalesPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where revisedItemSalesPrice is greater than DEFAULT_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldNotBeFound("revisedItemSalesPrice.greaterThan=" + DEFAULT_REVISED_ITEM_SALES_PRICE);

        // Get all the orderDetailsList where revisedItemSalesPrice is greater than SMALLER_REVISED_ITEM_SALES_PRICE
        defaultOrderDetailsShouldBeFound("revisedItemSalesPrice.greaterThan=" + SMALLER_REVISED_ITEM_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where note equals to DEFAULT_NOTE
        defaultOrderDetailsShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the orderDetailsList where note equals to UPDATED_NOTE
        defaultOrderDetailsShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultOrderDetailsShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the orderDetailsList where note equals to UPDATED_NOTE
        defaultOrderDetailsShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where note is not null
        defaultOrderDetailsShouldBeFound("note.specified=true");

        // Get all the orderDetailsList where note is null
        defaultOrderDetailsShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderDetailsByNoteContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where note contains DEFAULT_NOTE
        defaultOrderDetailsShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the orderDetailsList where note contains UPDATED_NOTE
        defaultOrderDetailsShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        // Get all the orderDetailsList where note does not contain DEFAULT_NOTE
        defaultOrderDetailsShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the orderDetailsList where note does not contain UPDATED_NOTE
        defaultOrderDetailsShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderDetailsByItemIsEqualToSomething() throws Exception {
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            orderDetailsRepository.saveAndFlush(orderDetails);
            item = ItemResourceIT.createEntity(em);
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        em.persist(item);
        em.flush();
        orderDetails.setItem(item);
        orderDetailsRepository.saveAndFlush(orderDetails);
        Long itemId = item.getId();

        // Get all the orderDetailsList where item equals to itemId
        defaultOrderDetailsShouldBeFound("itemId.equals=" + itemId);

        // Get all the orderDetailsList where item equals to (itemId + 1)
        defaultOrderDetailsShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderDetailsByOrdersIsEqualToSomething() throws Exception {
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            orderDetailsRepository.saveAndFlush(orderDetails);
            orders = OrdersResourceIT.createEntity(em);
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(orders);
        em.flush();
        orderDetails.addOrders(orders);
        orderDetailsRepository.saveAndFlush(orderDetails);
        Long ordersId = orders.getId();

        // Get all the orderDetailsList where orders equals to ordersId
        defaultOrderDetailsShouldBeFound("ordersId.equals=" + ordersId);

        // Get all the orderDetailsList where orders equals to (ordersId + 1)
        defaultOrderDetailsShouldNotBeFound("ordersId.equals=" + (ordersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderDetailsShouldBeFound(String filter) throws Exception {
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderQty").value(hasItem(DEFAULT_ORDER_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].revisedItemSalesPrice").value(hasItem(sameNumber(DEFAULT_REVISED_ITEM_SALES_PRICE))))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderDetailsShouldNotBeFound(String filter) throws Exception {
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderDetails() throws Exception {
        // Get the orderDetails
        restOrderDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();

        // Update the orderDetails
        OrderDetails updatedOrderDetails = orderDetailsRepository.findById(orderDetails.getId()).get();
        // Disconnect from session so that the updates on updatedOrderDetails are not directly saved in db
        em.detach(updatedOrderDetails);
        updatedOrderDetails.orderQty(UPDATED_ORDER_QTY).revisedItemSalesPrice(UPDATED_REVISED_ITEM_SALES_PRICE).note(UPDATED_NOTE);

        restOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderQty()).isEqualTo(UPDATED_ORDER_QTY);
        assertThat(testOrderDetails.getRevisedItemSalesPrice()).isEqualByComparingTo(UPDATED_REVISED_ITEM_SALES_PRICE);
        assertThat(testOrderDetails.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();

        // Update the orderDetails using partial update
        OrderDetails partialUpdatedOrderDetails = new OrderDetails();
        partialUpdatedOrderDetails.setId(orderDetails.getId());

        partialUpdatedOrderDetails.orderQty(UPDATED_ORDER_QTY).note(UPDATED_NOTE);

        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderQty()).isEqualTo(UPDATED_ORDER_QTY);
        assertThat(testOrderDetails.getRevisedItemSalesPrice()).isEqualByComparingTo(DEFAULT_REVISED_ITEM_SALES_PRICE);
        assertThat(testOrderDetails.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();

        // Update the orderDetails using partial update
        OrderDetails partialUpdatedOrderDetails = new OrderDetails();
        partialUpdatedOrderDetails.setId(orderDetails.getId());

        partialUpdatedOrderDetails.orderQty(UPDATED_ORDER_QTY).revisedItemSalesPrice(UPDATED_REVISED_ITEM_SALES_PRICE).note(UPDATED_NOTE);

        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderDetails testOrderDetails = orderDetailsList.get(orderDetailsList.size() - 1);
        assertThat(testOrderDetails.getOrderQty()).isEqualTo(UPDATED_ORDER_QTY);
        assertThat(testOrderDetails.getRevisedItemSalesPrice()).isEqualByComparingTo(UPDATED_REVISED_ITEM_SALES_PRICE);
        assertThat(testOrderDetails.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderDetailsRepository.findAll().size();
        orderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderDetails in the database
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderDetails() throws Exception {
        // Initialize the database
        orderDetailsRepository.saveAndFlush(orderDetails);

        int databaseSizeBeforeDelete = orderDetailsRepository.findAll().size();

        // Delete the orderDetails
        restOrderDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        assertThat(orderDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
