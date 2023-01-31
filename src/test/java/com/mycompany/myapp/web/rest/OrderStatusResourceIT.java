package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OrderStatus;
import com.mycompany.myapp.repository.OrderStatusRepository;
import com.mycompany.myapp.service.criteria.OrderStatusCriteria;
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
 * Integration tests for the {@link OrderStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderStatusResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/order-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderStatusMockMvc;

    private OrderStatus orderStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStatus createEntity(EntityManager em) {
        OrderStatus orderStatus = new OrderStatus().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).isActive(DEFAULT_IS_ACTIVE);
        return orderStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderStatus createUpdatedEntity(EntityManager em) {
        OrderStatus orderStatus = new OrderStatus().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);
        return orderStatus;
    }

    @BeforeEach
    public void initTest() {
        orderStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderStatus() throws Exception {
        int databaseSizeBeforeCreate = orderStatusRepository.findAll().size();
        // Create the OrderStatus
        restOrderStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStatus)))
            .andExpect(status().isCreated());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeCreate + 1);
        OrderStatus testOrderStatus = orderStatusList.get(orderStatusList.size() - 1);
        assertThat(testOrderStatus.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrderStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrderStatus.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createOrderStatusWithExistingId() throws Exception {
        // Create the OrderStatus with an existing ID
        orderStatus.setId(1L);

        int databaseSizeBeforeCreate = orderStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStatus)))
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderStatusRepository.findAll().size();
        // set the field null
        orderStatus.setCode(null);

        // Create the OrderStatus, which fails.

        restOrderStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStatus)))
            .andExpect(status().isBadRequest());

        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderStatusRepository.findAll().size();
        // set the field null
        orderStatus.setDescription(null);

        // Create the OrderStatus, which fails.

        restOrderStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStatus)))
            .andExpect(status().isBadRequest());

        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderStatuses() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getOrderStatus() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get the orderStatus
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, orderStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderStatus.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getOrderStatusesByIdFiltering() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        Long id = orderStatus.getId();

        defaultOrderStatusShouldBeFound("id.equals=" + id);
        defaultOrderStatusShouldNotBeFound("id.notEquals=" + id);

        defaultOrderStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where code equals to DEFAULT_CODE
        defaultOrderStatusShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the orderStatusList where code equals to UPDATED_CODE
        defaultOrderStatusShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOrderStatusShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the orderStatusList where code equals to UPDATED_CODE
        defaultOrderStatusShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where code is not null
        defaultOrderStatusShouldBeFound("code.specified=true");

        // Get all the orderStatusList where code is null
        defaultOrderStatusShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderStatusesByCodeContainsSomething() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where code contains DEFAULT_CODE
        defaultOrderStatusShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the orderStatusList where code contains UPDATED_CODE
        defaultOrderStatusShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where code does not contain DEFAULT_CODE
        defaultOrderStatusShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the orderStatusList where code does not contain UPDATED_CODE
        defaultOrderStatusShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where description equals to DEFAULT_DESCRIPTION
        defaultOrderStatusShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the orderStatusList where description equals to UPDATED_DESCRIPTION
        defaultOrderStatusShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrderStatusShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the orderStatusList where description equals to UPDATED_DESCRIPTION
        defaultOrderStatusShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where description is not null
        defaultOrderStatusShouldBeFound("description.specified=true");

        // Get all the orderStatusList where description is null
        defaultOrderStatusShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderStatusesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where description contains DEFAULT_DESCRIPTION
        defaultOrderStatusShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the orderStatusList where description contains UPDATED_DESCRIPTION
        defaultOrderStatusShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where description does not contain DEFAULT_DESCRIPTION
        defaultOrderStatusShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the orderStatusList where description does not contain UPDATED_DESCRIPTION
        defaultOrderStatusShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where isActive equals to DEFAULT_IS_ACTIVE
        defaultOrderStatusShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the orderStatusList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrderStatusShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultOrderStatusShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the orderStatusList where isActive equals to UPDATED_IS_ACTIVE
        defaultOrderStatusShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllOrderStatusesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        // Get all the orderStatusList where isActive is not null
        defaultOrderStatusShouldBeFound("isActive.specified=true");

        // Get all the orderStatusList where isActive is null
        defaultOrderStatusShouldNotBeFound("isActive.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderStatusShouldBeFound(String filter) throws Exception {
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderStatusShouldNotBeFound(String filter) throws Exception {
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderStatus() throws Exception {
        // Get the orderStatus
        restOrderStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderStatus() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();

        // Update the orderStatus
        OrderStatus updatedOrderStatus = orderStatusRepository.findById(orderStatus.getId()).get();
        // Disconnect from session so that the updates on updatedOrderStatus are not directly saved in db
        em.detach(updatedOrderStatus);
        updatedOrderStatus.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restOrderStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
        OrderStatus testOrderStatus = orderStatusList.get(orderStatusList.size() - 1);
        assertThat(testOrderStatus.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrderStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrderStatus.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingOrderStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();
        orderStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();
        orderStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();
        orderStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderStatusWithPatch() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();

        // Update the orderStatus using partial update
        OrderStatus partialUpdatedOrderStatus = new OrderStatus();
        partialUpdatedOrderStatus.setId(orderStatus.getId());

        partialUpdatedOrderStatus.code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);

        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
        OrderStatus testOrderStatus = orderStatusList.get(orderStatusList.size() - 1);
        assertThat(testOrderStatus.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrderStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrderStatus.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateOrderStatusWithPatch() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();

        // Update the orderStatus using partial update
        OrderStatus partialUpdatedOrderStatus = new OrderStatus();
        partialUpdatedOrderStatus.setId(orderStatus.getId());

        partialUpdatedOrderStatus.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).isActive(UPDATED_IS_ACTIVE);

        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
        OrderStatus testOrderStatus = orderStatusList.get(orderStatusList.size() - 1);
        assertThat(testOrderStatus.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrderStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrderStatus.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();
        orderStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();
        orderStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderStatusRepository.findAll().size();
        orderStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderStatus in the database
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderStatus() throws Exception {
        // Initialize the database
        orderStatusRepository.saveAndFlush(orderStatus);

        int databaseSizeBeforeDelete = orderStatusRepository.findAll().size();

        // Delete the orderStatus
        restOrderStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
        assertThat(orderStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
