package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Certificate;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.domain.Item;
import com.mycompany.myapp.domain.MasterItem;
import com.mycompany.myapp.domain.Rating;
import com.mycompany.myapp.domain.UnitOfMeasure;
import com.mycompany.myapp.repository.ItemRepository;
import com.mycompany.myapp.service.ItemService;
import com.mycompany.myapp.service.criteria.ItemCriteria;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    private static final BigDecimal DEFAULT_ITEM_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ITEM_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ITEM_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ITEM_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ITEM_COST = new BigDecimal(1 - 1);

    private static final String DEFAULT_BANNER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_BANNER_TEXT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SPECIAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SPECIAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SPECIAL_PRICE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Double DEFAULT_MIN_QTY = 1D;
    private static final Double UPDATED_MIN_QTY = 2D;
    private static final Double SMALLER_MIN_QTY = 1D - 1D;

    private static final Double DEFAULT_MAX_QTY = 1D;
    private static final Double UPDATED_MAX_QTY = 2D;
    private static final Double SMALLER_MAX_QTY = 1D - 1D;

    private static final Double DEFAULT_STEPS = 1D;
    private static final Double UPDATED_STEPS = 2D;
    private static final Double SMALLER_STEPS = 1D - 1D;

    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEAD_TIME = 1;
    private static final Integer UPDATED_LEAD_TIME = 2;
    private static final Integer SMALLER_LEAD_TIME = 1 - 1;

    private static final Double DEFAULT_REORDER_QTY = 1D;
    private static final Double UPDATED_REORDER_QTY = 2D;
    private static final Double SMALLER_REORDER_QTY = 1D - 1D;

    private static final String DEFAULT_ITEM_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_BARCODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Mock
    private ItemService itemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .itemPrice(DEFAULT_ITEM_PRICE)
            .itemCost(DEFAULT_ITEM_COST)
            .bannerText(DEFAULT_BANNER_TEXT)
            .specialPrice(DEFAULT_SPECIAL_PRICE)
            .isActive(DEFAULT_IS_ACTIVE)
            .minQTY(DEFAULT_MIN_QTY)
            .maxQTY(DEFAULT_MAX_QTY)
            .steps(DEFAULT_STEPS)
            .longDescription(DEFAULT_LONG_DESCRIPTION)
            .leadTime(DEFAULT_LEAD_TIME)
            .reorderQty(DEFAULT_REORDER_QTY)
            .itemBarcode(DEFAULT_ITEM_BARCODE);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemCost(UPDATED_ITEM_COST)
            .bannerText(UPDATED_BANNER_TEXT)
            .specialPrice(UPDATED_SPECIAL_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .minQTY(UPDATED_MIN_QTY)
            .maxQTY(UPDATED_MAX_QTY)
            .steps(UPDATED_STEPS)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .leadTime(UPDATED_LEAD_TIME)
            .reorderQty(UPDATED_REORDER_QTY)
            .itemBarcode(UPDATED_ITEM_BARCODE);
        return item;
    }

    @BeforeEach
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();
        // Create the Item
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemPrice()).isEqualByComparingTo(DEFAULT_ITEM_PRICE);
        assertThat(testItem.getItemCost()).isEqualByComparingTo(DEFAULT_ITEM_COST);
        assertThat(testItem.getBannerText()).isEqualTo(DEFAULT_BANNER_TEXT);
        assertThat(testItem.getSpecialPrice()).isEqualByComparingTo(DEFAULT_SPECIAL_PRICE);
        assertThat(testItem.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testItem.getMinQTY()).isEqualTo(DEFAULT_MIN_QTY);
        assertThat(testItem.getMaxQTY()).isEqualTo(DEFAULT_MAX_QTY);
        assertThat(testItem.getSteps()).isEqualTo(DEFAULT_STEPS);
        assertThat(testItem.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testItem.getLeadTime()).isEqualTo(DEFAULT_LEAD_TIME);
        assertThat(testItem.getReorderQty()).isEqualTo(DEFAULT_REORDER_QTY);
        assertThat(testItem.getItemBarcode()).isEqualTo(DEFAULT_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);

        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(sameNumber(DEFAULT_ITEM_PRICE))))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(sameNumber(DEFAULT_ITEM_COST))))
            .andExpect(jsonPath("$.[*].bannerText").value(hasItem(DEFAULT_BANNER_TEXT)))
            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(sameNumber(DEFAULT_SPECIAL_PRICE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].minQTY").value(hasItem(DEFAULT_MIN_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].maxQTY").value(hasItem(DEFAULT_MAX_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].steps").value(hasItem(DEFAULT_STEPS.doubleValue())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].reorderQty").value(hasItem(DEFAULT_REORDER_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].itemBarcode").value(hasItem(DEFAULT_ITEM_BARCODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(itemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.itemPrice").value(sameNumber(DEFAULT_ITEM_PRICE)))
            .andExpect(jsonPath("$.itemCost").value(sameNumber(DEFAULT_ITEM_COST)))
            .andExpect(jsonPath("$.bannerText").value(DEFAULT_BANNER_TEXT))
            .andExpect(jsonPath("$.specialPrice").value(sameNumber(DEFAULT_SPECIAL_PRICE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.minQTY").value(DEFAULT_MIN_QTY.doubleValue()))
            .andExpect(jsonPath("$.maxQTY").value(DEFAULT_MAX_QTY.doubleValue()))
            .andExpect(jsonPath("$.steps").value(DEFAULT_STEPS.doubleValue()))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION))
            .andExpect(jsonPath("$.leadTime").value(DEFAULT_LEAD_TIME))
            .andExpect(jsonPath("$.reorderQty").value(DEFAULT_REORDER_QTY.doubleValue()))
            .andExpect(jsonPath("$.itemBarcode").value(DEFAULT_ITEM_BARCODE));
    }

    @Test
    @Transactional
    void getItemsByIdFiltering() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        Long id = item.getId();

        defaultItemShouldBeFound("id.equals=" + id);
        defaultItemShouldNotBeFound("id.notEquals=" + id);

        defaultItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemShouldNotBeFound("id.greaterThan=" + id);

        defaultItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemsByItemPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemPrice equals to DEFAULT_ITEM_PRICE
        defaultItemShouldBeFound("itemPrice.equals=" + DEFAULT_ITEM_PRICE);

        // Get all the itemList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultItemShouldNotBeFound("itemPrice.equals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByItemPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemPrice in DEFAULT_ITEM_PRICE or UPDATED_ITEM_PRICE
        defaultItemShouldBeFound("itemPrice.in=" + DEFAULT_ITEM_PRICE + "," + UPDATED_ITEM_PRICE);

        // Get all the itemList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultItemShouldNotBeFound("itemPrice.in=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByItemPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemPrice is not null
        defaultItemShouldBeFound("itemPrice.specified=true");

        // Get all the itemList where itemPrice is null
        defaultItemShouldNotBeFound("itemPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByItemPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemPrice is greater than or equal to DEFAULT_ITEM_PRICE
        defaultItemShouldBeFound("itemPrice.greaterThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the itemList where itemPrice is greater than or equal to UPDATED_ITEM_PRICE
        defaultItemShouldNotBeFound("itemPrice.greaterThanOrEqual=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByItemPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemPrice is less than or equal to DEFAULT_ITEM_PRICE
        defaultItemShouldBeFound("itemPrice.lessThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the itemList where itemPrice is less than or equal to SMALLER_ITEM_PRICE
        defaultItemShouldNotBeFound("itemPrice.lessThanOrEqual=" + SMALLER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByItemPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemPrice is less than DEFAULT_ITEM_PRICE
        defaultItemShouldNotBeFound("itemPrice.lessThan=" + DEFAULT_ITEM_PRICE);

        // Get all the itemList where itemPrice is less than UPDATED_ITEM_PRICE
        defaultItemShouldBeFound("itemPrice.lessThan=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByItemPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemPrice is greater than DEFAULT_ITEM_PRICE
        defaultItemShouldNotBeFound("itemPrice.greaterThan=" + DEFAULT_ITEM_PRICE);

        // Get all the itemList where itemPrice is greater than SMALLER_ITEM_PRICE
        defaultItemShouldBeFound("itemPrice.greaterThan=" + SMALLER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByItemCostIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemCost equals to DEFAULT_ITEM_COST
        defaultItemShouldBeFound("itemCost.equals=" + DEFAULT_ITEM_COST);

        // Get all the itemList where itemCost equals to UPDATED_ITEM_COST
        defaultItemShouldNotBeFound("itemCost.equals=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    void getAllItemsByItemCostIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemCost in DEFAULT_ITEM_COST or UPDATED_ITEM_COST
        defaultItemShouldBeFound("itemCost.in=" + DEFAULT_ITEM_COST + "," + UPDATED_ITEM_COST);

        // Get all the itemList where itemCost equals to UPDATED_ITEM_COST
        defaultItemShouldNotBeFound("itemCost.in=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    void getAllItemsByItemCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemCost is not null
        defaultItemShouldBeFound("itemCost.specified=true");

        // Get all the itemList where itemCost is null
        defaultItemShouldNotBeFound("itemCost.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByItemCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemCost is greater than or equal to DEFAULT_ITEM_COST
        defaultItemShouldBeFound("itemCost.greaterThanOrEqual=" + DEFAULT_ITEM_COST);

        // Get all the itemList where itemCost is greater than or equal to UPDATED_ITEM_COST
        defaultItemShouldNotBeFound("itemCost.greaterThanOrEqual=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    void getAllItemsByItemCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemCost is less than or equal to DEFAULT_ITEM_COST
        defaultItemShouldBeFound("itemCost.lessThanOrEqual=" + DEFAULT_ITEM_COST);

        // Get all the itemList where itemCost is less than or equal to SMALLER_ITEM_COST
        defaultItemShouldNotBeFound("itemCost.lessThanOrEqual=" + SMALLER_ITEM_COST);
    }

    @Test
    @Transactional
    void getAllItemsByItemCostIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemCost is less than DEFAULT_ITEM_COST
        defaultItemShouldNotBeFound("itemCost.lessThan=" + DEFAULT_ITEM_COST);

        // Get all the itemList where itemCost is less than UPDATED_ITEM_COST
        defaultItemShouldBeFound("itemCost.lessThan=" + UPDATED_ITEM_COST);
    }

    @Test
    @Transactional
    void getAllItemsByItemCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemCost is greater than DEFAULT_ITEM_COST
        defaultItemShouldNotBeFound("itemCost.greaterThan=" + DEFAULT_ITEM_COST);

        // Get all the itemList where itemCost is greater than SMALLER_ITEM_COST
        defaultItemShouldBeFound("itemCost.greaterThan=" + SMALLER_ITEM_COST);
    }

    @Test
    @Transactional
    void getAllItemsByBannerTextIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where bannerText equals to DEFAULT_BANNER_TEXT
        defaultItemShouldBeFound("bannerText.equals=" + DEFAULT_BANNER_TEXT);

        // Get all the itemList where bannerText equals to UPDATED_BANNER_TEXT
        defaultItemShouldNotBeFound("bannerText.equals=" + UPDATED_BANNER_TEXT);
    }

    @Test
    @Transactional
    void getAllItemsByBannerTextIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where bannerText in DEFAULT_BANNER_TEXT or UPDATED_BANNER_TEXT
        defaultItemShouldBeFound("bannerText.in=" + DEFAULT_BANNER_TEXT + "," + UPDATED_BANNER_TEXT);

        // Get all the itemList where bannerText equals to UPDATED_BANNER_TEXT
        defaultItemShouldNotBeFound("bannerText.in=" + UPDATED_BANNER_TEXT);
    }

    @Test
    @Transactional
    void getAllItemsByBannerTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where bannerText is not null
        defaultItemShouldBeFound("bannerText.specified=true");

        // Get all the itemList where bannerText is null
        defaultItemShouldNotBeFound("bannerText.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByBannerTextContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where bannerText contains DEFAULT_BANNER_TEXT
        defaultItemShouldBeFound("bannerText.contains=" + DEFAULT_BANNER_TEXT);

        // Get all the itemList where bannerText contains UPDATED_BANNER_TEXT
        defaultItemShouldNotBeFound("bannerText.contains=" + UPDATED_BANNER_TEXT);
    }

    @Test
    @Transactional
    void getAllItemsByBannerTextNotContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where bannerText does not contain DEFAULT_BANNER_TEXT
        defaultItemShouldNotBeFound("bannerText.doesNotContain=" + DEFAULT_BANNER_TEXT);

        // Get all the itemList where bannerText does not contain UPDATED_BANNER_TEXT
        defaultItemShouldBeFound("bannerText.doesNotContain=" + UPDATED_BANNER_TEXT);
    }

    @Test
    @Transactional
    void getAllItemsBySpecialPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specialPrice equals to DEFAULT_SPECIAL_PRICE
        defaultItemShouldBeFound("specialPrice.equals=" + DEFAULT_SPECIAL_PRICE);

        // Get all the itemList where specialPrice equals to UPDATED_SPECIAL_PRICE
        defaultItemShouldNotBeFound("specialPrice.equals=" + UPDATED_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsBySpecialPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specialPrice in DEFAULT_SPECIAL_PRICE or UPDATED_SPECIAL_PRICE
        defaultItemShouldBeFound("specialPrice.in=" + DEFAULT_SPECIAL_PRICE + "," + UPDATED_SPECIAL_PRICE);

        // Get all the itemList where specialPrice equals to UPDATED_SPECIAL_PRICE
        defaultItemShouldNotBeFound("specialPrice.in=" + UPDATED_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsBySpecialPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specialPrice is not null
        defaultItemShouldBeFound("specialPrice.specified=true");

        // Get all the itemList where specialPrice is null
        defaultItemShouldNotBeFound("specialPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsBySpecialPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specialPrice is greater than or equal to DEFAULT_SPECIAL_PRICE
        defaultItemShouldBeFound("specialPrice.greaterThanOrEqual=" + DEFAULT_SPECIAL_PRICE);

        // Get all the itemList where specialPrice is greater than or equal to UPDATED_SPECIAL_PRICE
        defaultItemShouldNotBeFound("specialPrice.greaterThanOrEqual=" + UPDATED_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsBySpecialPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specialPrice is less than or equal to DEFAULT_SPECIAL_PRICE
        defaultItemShouldBeFound("specialPrice.lessThanOrEqual=" + DEFAULT_SPECIAL_PRICE);

        // Get all the itemList where specialPrice is less than or equal to SMALLER_SPECIAL_PRICE
        defaultItemShouldNotBeFound("specialPrice.lessThanOrEqual=" + SMALLER_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsBySpecialPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specialPrice is less than DEFAULT_SPECIAL_PRICE
        defaultItemShouldNotBeFound("specialPrice.lessThan=" + DEFAULT_SPECIAL_PRICE);

        // Get all the itemList where specialPrice is less than UPDATED_SPECIAL_PRICE
        defaultItemShouldBeFound("specialPrice.lessThan=" + UPDATED_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsBySpecialPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where specialPrice is greater than DEFAULT_SPECIAL_PRICE
        defaultItemShouldNotBeFound("specialPrice.greaterThan=" + DEFAULT_SPECIAL_PRICE);

        // Get all the itemList where specialPrice is greater than SMALLER_SPECIAL_PRICE
        defaultItemShouldBeFound("specialPrice.greaterThan=" + SMALLER_SPECIAL_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where isActive equals to DEFAULT_IS_ACTIVE
        defaultItemShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the itemList where isActive equals to UPDATED_IS_ACTIVE
        defaultItemShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllItemsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultItemShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the itemList where isActive equals to UPDATED_IS_ACTIVE
        defaultItemShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllItemsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where isActive is not null
        defaultItemShouldBeFound("isActive.specified=true");

        // Get all the itemList where isActive is null
        defaultItemShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByMinQTYIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minQTY equals to DEFAULT_MIN_QTY
        defaultItemShouldBeFound("minQTY.equals=" + DEFAULT_MIN_QTY);

        // Get all the itemList where minQTY equals to UPDATED_MIN_QTY
        defaultItemShouldNotBeFound("minQTY.equals=" + UPDATED_MIN_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMinQTYIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minQTY in DEFAULT_MIN_QTY or UPDATED_MIN_QTY
        defaultItemShouldBeFound("minQTY.in=" + DEFAULT_MIN_QTY + "," + UPDATED_MIN_QTY);

        // Get all the itemList where minQTY equals to UPDATED_MIN_QTY
        defaultItemShouldNotBeFound("minQTY.in=" + UPDATED_MIN_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMinQTYIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minQTY is not null
        defaultItemShouldBeFound("minQTY.specified=true");

        // Get all the itemList where minQTY is null
        defaultItemShouldNotBeFound("minQTY.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByMinQTYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minQTY is greater than or equal to DEFAULT_MIN_QTY
        defaultItemShouldBeFound("minQTY.greaterThanOrEqual=" + DEFAULT_MIN_QTY);

        // Get all the itemList where minQTY is greater than or equal to UPDATED_MIN_QTY
        defaultItemShouldNotBeFound("minQTY.greaterThanOrEqual=" + UPDATED_MIN_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMinQTYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minQTY is less than or equal to DEFAULT_MIN_QTY
        defaultItemShouldBeFound("minQTY.lessThanOrEqual=" + DEFAULT_MIN_QTY);

        // Get all the itemList where minQTY is less than or equal to SMALLER_MIN_QTY
        defaultItemShouldNotBeFound("minQTY.lessThanOrEqual=" + SMALLER_MIN_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMinQTYIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minQTY is less than DEFAULT_MIN_QTY
        defaultItemShouldNotBeFound("minQTY.lessThan=" + DEFAULT_MIN_QTY);

        // Get all the itemList where minQTY is less than UPDATED_MIN_QTY
        defaultItemShouldBeFound("minQTY.lessThan=" + UPDATED_MIN_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMinQTYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where minQTY is greater than DEFAULT_MIN_QTY
        defaultItemShouldNotBeFound("minQTY.greaterThan=" + DEFAULT_MIN_QTY);

        // Get all the itemList where minQTY is greater than SMALLER_MIN_QTY
        defaultItemShouldBeFound("minQTY.greaterThan=" + SMALLER_MIN_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMaxQTYIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where maxQTY equals to DEFAULT_MAX_QTY
        defaultItemShouldBeFound("maxQTY.equals=" + DEFAULT_MAX_QTY);

        // Get all the itemList where maxQTY equals to UPDATED_MAX_QTY
        defaultItemShouldNotBeFound("maxQTY.equals=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMaxQTYIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where maxQTY in DEFAULT_MAX_QTY or UPDATED_MAX_QTY
        defaultItemShouldBeFound("maxQTY.in=" + DEFAULT_MAX_QTY + "," + UPDATED_MAX_QTY);

        // Get all the itemList where maxQTY equals to UPDATED_MAX_QTY
        defaultItemShouldNotBeFound("maxQTY.in=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMaxQTYIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where maxQTY is not null
        defaultItemShouldBeFound("maxQTY.specified=true");

        // Get all the itemList where maxQTY is null
        defaultItemShouldNotBeFound("maxQTY.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByMaxQTYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where maxQTY is greater than or equal to DEFAULT_MAX_QTY
        defaultItemShouldBeFound("maxQTY.greaterThanOrEqual=" + DEFAULT_MAX_QTY);

        // Get all the itemList where maxQTY is greater than or equal to UPDATED_MAX_QTY
        defaultItemShouldNotBeFound("maxQTY.greaterThanOrEqual=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMaxQTYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where maxQTY is less than or equal to DEFAULT_MAX_QTY
        defaultItemShouldBeFound("maxQTY.lessThanOrEqual=" + DEFAULT_MAX_QTY);

        // Get all the itemList where maxQTY is less than or equal to SMALLER_MAX_QTY
        defaultItemShouldNotBeFound("maxQTY.lessThanOrEqual=" + SMALLER_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMaxQTYIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where maxQTY is less than DEFAULT_MAX_QTY
        defaultItemShouldNotBeFound("maxQTY.lessThan=" + DEFAULT_MAX_QTY);

        // Get all the itemList where maxQTY is less than UPDATED_MAX_QTY
        defaultItemShouldBeFound("maxQTY.lessThan=" + UPDATED_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByMaxQTYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where maxQTY is greater than DEFAULT_MAX_QTY
        defaultItemShouldNotBeFound("maxQTY.greaterThan=" + DEFAULT_MAX_QTY);

        // Get all the itemList where maxQTY is greater than SMALLER_MAX_QTY
        defaultItemShouldBeFound("maxQTY.greaterThan=" + SMALLER_MAX_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByStepsIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where steps equals to DEFAULT_STEPS
        defaultItemShouldBeFound("steps.equals=" + DEFAULT_STEPS);

        // Get all the itemList where steps equals to UPDATED_STEPS
        defaultItemShouldNotBeFound("steps.equals=" + UPDATED_STEPS);
    }

    @Test
    @Transactional
    void getAllItemsByStepsIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where steps in DEFAULT_STEPS or UPDATED_STEPS
        defaultItemShouldBeFound("steps.in=" + DEFAULT_STEPS + "," + UPDATED_STEPS);

        // Get all the itemList where steps equals to UPDATED_STEPS
        defaultItemShouldNotBeFound("steps.in=" + UPDATED_STEPS);
    }

    @Test
    @Transactional
    void getAllItemsByStepsIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where steps is not null
        defaultItemShouldBeFound("steps.specified=true");

        // Get all the itemList where steps is null
        defaultItemShouldNotBeFound("steps.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByStepsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where steps is greater than or equal to DEFAULT_STEPS
        defaultItemShouldBeFound("steps.greaterThanOrEqual=" + DEFAULT_STEPS);

        // Get all the itemList where steps is greater than or equal to UPDATED_STEPS
        defaultItemShouldNotBeFound("steps.greaterThanOrEqual=" + UPDATED_STEPS);
    }

    @Test
    @Transactional
    void getAllItemsByStepsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where steps is less than or equal to DEFAULT_STEPS
        defaultItemShouldBeFound("steps.lessThanOrEqual=" + DEFAULT_STEPS);

        // Get all the itemList where steps is less than or equal to SMALLER_STEPS
        defaultItemShouldNotBeFound("steps.lessThanOrEqual=" + SMALLER_STEPS);
    }

    @Test
    @Transactional
    void getAllItemsByStepsIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where steps is less than DEFAULT_STEPS
        defaultItemShouldNotBeFound("steps.lessThan=" + DEFAULT_STEPS);

        // Get all the itemList where steps is less than UPDATED_STEPS
        defaultItemShouldBeFound("steps.lessThan=" + UPDATED_STEPS);
    }

    @Test
    @Transactional
    void getAllItemsByStepsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where steps is greater than DEFAULT_STEPS
        defaultItemShouldNotBeFound("steps.greaterThan=" + DEFAULT_STEPS);

        // Get all the itemList where steps is greater than SMALLER_STEPS
        defaultItemShouldBeFound("steps.greaterThan=" + SMALLER_STEPS);
    }

    @Test
    @Transactional
    void getAllItemsByLongDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where longDescription equals to DEFAULT_LONG_DESCRIPTION
        defaultItemShouldBeFound("longDescription.equals=" + DEFAULT_LONG_DESCRIPTION);

        // Get all the itemList where longDescription equals to UPDATED_LONG_DESCRIPTION
        defaultItemShouldNotBeFound("longDescription.equals=" + UPDATED_LONG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByLongDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where longDescription in DEFAULT_LONG_DESCRIPTION or UPDATED_LONG_DESCRIPTION
        defaultItemShouldBeFound("longDescription.in=" + DEFAULT_LONG_DESCRIPTION + "," + UPDATED_LONG_DESCRIPTION);

        // Get all the itemList where longDescription equals to UPDATED_LONG_DESCRIPTION
        defaultItemShouldNotBeFound("longDescription.in=" + UPDATED_LONG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByLongDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where longDescription is not null
        defaultItemShouldBeFound("longDescription.specified=true");

        // Get all the itemList where longDescription is null
        defaultItemShouldNotBeFound("longDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByLongDescriptionContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where longDescription contains DEFAULT_LONG_DESCRIPTION
        defaultItemShouldBeFound("longDescription.contains=" + DEFAULT_LONG_DESCRIPTION);

        // Get all the itemList where longDescription contains UPDATED_LONG_DESCRIPTION
        defaultItemShouldNotBeFound("longDescription.contains=" + UPDATED_LONG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByLongDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where longDescription does not contain DEFAULT_LONG_DESCRIPTION
        defaultItemShouldNotBeFound("longDescription.doesNotContain=" + DEFAULT_LONG_DESCRIPTION);

        // Get all the itemList where longDescription does not contain UPDATED_LONG_DESCRIPTION
        defaultItemShouldBeFound("longDescription.doesNotContain=" + UPDATED_LONG_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByLeadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where leadTime equals to DEFAULT_LEAD_TIME
        defaultItemShouldBeFound("leadTime.equals=" + DEFAULT_LEAD_TIME);

        // Get all the itemList where leadTime equals to UPDATED_LEAD_TIME
        defaultItemShouldNotBeFound("leadTime.equals=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllItemsByLeadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where leadTime in DEFAULT_LEAD_TIME or UPDATED_LEAD_TIME
        defaultItemShouldBeFound("leadTime.in=" + DEFAULT_LEAD_TIME + "," + UPDATED_LEAD_TIME);

        // Get all the itemList where leadTime equals to UPDATED_LEAD_TIME
        defaultItemShouldNotBeFound("leadTime.in=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllItemsByLeadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where leadTime is not null
        defaultItemShouldBeFound("leadTime.specified=true");

        // Get all the itemList where leadTime is null
        defaultItemShouldNotBeFound("leadTime.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByLeadTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where leadTime is greater than or equal to DEFAULT_LEAD_TIME
        defaultItemShouldBeFound("leadTime.greaterThanOrEqual=" + DEFAULT_LEAD_TIME);

        // Get all the itemList where leadTime is greater than or equal to UPDATED_LEAD_TIME
        defaultItemShouldNotBeFound("leadTime.greaterThanOrEqual=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllItemsByLeadTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where leadTime is less than or equal to DEFAULT_LEAD_TIME
        defaultItemShouldBeFound("leadTime.lessThanOrEqual=" + DEFAULT_LEAD_TIME);

        // Get all the itemList where leadTime is less than or equal to SMALLER_LEAD_TIME
        defaultItemShouldNotBeFound("leadTime.lessThanOrEqual=" + SMALLER_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllItemsByLeadTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where leadTime is less than DEFAULT_LEAD_TIME
        defaultItemShouldNotBeFound("leadTime.lessThan=" + DEFAULT_LEAD_TIME);

        // Get all the itemList where leadTime is less than UPDATED_LEAD_TIME
        defaultItemShouldBeFound("leadTime.lessThan=" + UPDATED_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllItemsByLeadTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where leadTime is greater than DEFAULT_LEAD_TIME
        defaultItemShouldNotBeFound("leadTime.greaterThan=" + DEFAULT_LEAD_TIME);

        // Get all the itemList where leadTime is greater than SMALLER_LEAD_TIME
        defaultItemShouldBeFound("leadTime.greaterThan=" + SMALLER_LEAD_TIME);
    }

    @Test
    @Transactional
    void getAllItemsByReorderQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where reorderQty equals to DEFAULT_REORDER_QTY
        defaultItemShouldBeFound("reorderQty.equals=" + DEFAULT_REORDER_QTY);

        // Get all the itemList where reorderQty equals to UPDATED_REORDER_QTY
        defaultItemShouldNotBeFound("reorderQty.equals=" + UPDATED_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByReorderQtyIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where reorderQty in DEFAULT_REORDER_QTY or UPDATED_REORDER_QTY
        defaultItemShouldBeFound("reorderQty.in=" + DEFAULT_REORDER_QTY + "," + UPDATED_REORDER_QTY);

        // Get all the itemList where reorderQty equals to UPDATED_REORDER_QTY
        defaultItemShouldNotBeFound("reorderQty.in=" + UPDATED_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByReorderQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where reorderQty is not null
        defaultItemShouldBeFound("reorderQty.specified=true");

        // Get all the itemList where reorderQty is null
        defaultItemShouldNotBeFound("reorderQty.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByReorderQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where reorderQty is greater than or equal to DEFAULT_REORDER_QTY
        defaultItemShouldBeFound("reorderQty.greaterThanOrEqual=" + DEFAULT_REORDER_QTY);

        // Get all the itemList where reorderQty is greater than or equal to UPDATED_REORDER_QTY
        defaultItemShouldNotBeFound("reorderQty.greaterThanOrEqual=" + UPDATED_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByReorderQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where reorderQty is less than or equal to DEFAULT_REORDER_QTY
        defaultItemShouldBeFound("reorderQty.lessThanOrEqual=" + DEFAULT_REORDER_QTY);

        // Get all the itemList where reorderQty is less than or equal to SMALLER_REORDER_QTY
        defaultItemShouldNotBeFound("reorderQty.lessThanOrEqual=" + SMALLER_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByReorderQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where reorderQty is less than DEFAULT_REORDER_QTY
        defaultItemShouldNotBeFound("reorderQty.lessThan=" + DEFAULT_REORDER_QTY);

        // Get all the itemList where reorderQty is less than UPDATED_REORDER_QTY
        defaultItemShouldBeFound("reorderQty.lessThan=" + UPDATED_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByReorderQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where reorderQty is greater than DEFAULT_REORDER_QTY
        defaultItemShouldNotBeFound("reorderQty.greaterThan=" + DEFAULT_REORDER_QTY);

        // Get all the itemList where reorderQty is greater than SMALLER_REORDER_QTY
        defaultItemShouldBeFound("reorderQty.greaterThan=" + SMALLER_REORDER_QTY);
    }

    @Test
    @Transactional
    void getAllItemsByItemBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemBarcode equals to DEFAULT_ITEM_BARCODE
        defaultItemShouldBeFound("itemBarcode.equals=" + DEFAULT_ITEM_BARCODE);

        // Get all the itemList where itemBarcode equals to UPDATED_ITEM_BARCODE
        defaultItemShouldNotBeFound("itemBarcode.equals=" + UPDATED_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void getAllItemsByItemBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemBarcode in DEFAULT_ITEM_BARCODE or UPDATED_ITEM_BARCODE
        defaultItemShouldBeFound("itemBarcode.in=" + DEFAULT_ITEM_BARCODE + "," + UPDATED_ITEM_BARCODE);

        // Get all the itemList where itemBarcode equals to UPDATED_ITEM_BARCODE
        defaultItemShouldNotBeFound("itemBarcode.in=" + UPDATED_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void getAllItemsByItemBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemBarcode is not null
        defaultItemShouldBeFound("itemBarcode.specified=true");

        // Get all the itemList where itemBarcode is null
        defaultItemShouldNotBeFound("itemBarcode.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByItemBarcodeContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemBarcode contains DEFAULT_ITEM_BARCODE
        defaultItemShouldBeFound("itemBarcode.contains=" + DEFAULT_ITEM_BARCODE);

        // Get all the itemList where itemBarcode contains UPDATED_ITEM_BARCODE
        defaultItemShouldNotBeFound("itemBarcode.contains=" + UPDATED_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void getAllItemsByItemBarcodeNotContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where itemBarcode does not contain DEFAULT_ITEM_BARCODE
        defaultItemShouldNotBeFound("itemBarcode.doesNotContain=" + DEFAULT_ITEM_BARCODE);

        // Get all the itemList where itemBarcode does not contain UPDATED_ITEM_BARCODE
        defaultItemShouldBeFound("itemBarcode.doesNotContain=" + UPDATED_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void getAllItemsByMasterItemIsEqualToSomething() throws Exception {
        MasterItem masterItem;
        if (TestUtil.findAll(em, MasterItem.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            masterItem = MasterItemResourceIT.createEntity(em);
        } else {
            masterItem = TestUtil.findAll(em, MasterItem.class).get(0);
        }
        em.persist(masterItem);
        em.flush();
        item.setMasterItem(masterItem);
        itemRepository.saveAndFlush(item);
        Long masterItemId = masterItem.getId();

        // Get all the itemList where masterItem equals to masterItemId
        defaultItemShouldBeFound("masterItemId.equals=" + masterItemId);

        // Get all the itemList where masterItem equals to (masterItemId + 1)
        defaultItemShouldNotBeFound("masterItemId.equals=" + (masterItemId + 1));
    }

    @Test
    @Transactional
    void getAllItemsByUnitIsEqualToSomething() throws Exception {
        UnitOfMeasure unit;
        if (TestUtil.findAll(em, UnitOfMeasure.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            unit = UnitOfMeasureResourceIT.createEntity(em);
        } else {
            unit = TestUtil.findAll(em, UnitOfMeasure.class).get(0);
        }
        em.persist(unit);
        em.flush();
        item.setUnit(unit);
        itemRepository.saveAndFlush(item);
        Long unitId = unit.getId();

        // Get all the itemList where unit equals to unitId
        defaultItemShouldBeFound("unitId.equals=" + unitId);

        // Get all the itemList where unit equals to (unitId + 1)
        defaultItemShouldNotBeFound("unitId.equals=" + (unitId + 1));
    }

    @Test
    @Transactional
    void getAllItemsByExUserIsEqualToSomething() throws Exception {
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            exUser = ExUserResourceIT.createEntity(em);
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        em.persist(exUser);
        em.flush();
        item.setExUser(exUser);
        itemRepository.saveAndFlush(item);
        Long exUserId = exUser.getId();

        // Get all the itemList where exUser equals to exUserId
        defaultItemShouldBeFound("exUserId.equals=" + exUserId);

        // Get all the itemList where exUser equals to (exUserId + 1)
        defaultItemShouldNotBeFound("exUserId.equals=" + (exUserId + 1));
    }

    @Test
    @Transactional
    void getAllItemsByRatingIsEqualToSomething() throws Exception {
        Rating rating;
        if (TestUtil.findAll(em, Rating.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            rating = RatingResourceIT.createEntity(em);
        } else {
            rating = TestUtil.findAll(em, Rating.class).get(0);
        }
        em.persist(rating);
        em.flush();
        item.addRating(rating);
        itemRepository.saveAndFlush(item);
        Long ratingId = rating.getId();

        // Get all the itemList where rating equals to ratingId
        defaultItemShouldBeFound("ratingId.equals=" + ratingId);

        // Get all the itemList where rating equals to (ratingId + 1)
        defaultItemShouldNotBeFound("ratingId.equals=" + (ratingId + 1));
    }

    @Test
    @Transactional
    void getAllItemsByCertificateIsEqualToSomething() throws Exception {
        Certificate certificate;
        if (TestUtil.findAll(em, Certificate.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            certificate = CertificateResourceIT.createEntity(em);
        } else {
            certificate = TestUtil.findAll(em, Certificate.class).get(0);
        }
        em.persist(certificate);
        em.flush();
        item.addCertificate(certificate);
        itemRepository.saveAndFlush(item);
        Long certificateId = certificate.getId();

        // Get all the itemList where certificate equals to certificateId
        defaultItemShouldBeFound("certificateId.equals=" + certificateId);

        // Get all the itemList where certificate equals to (certificateId + 1)
        defaultItemShouldNotBeFound("certificateId.equals=" + (certificateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemShouldBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(sameNumber(DEFAULT_ITEM_PRICE))))
            .andExpect(jsonPath("$.[*].itemCost").value(hasItem(sameNumber(DEFAULT_ITEM_COST))))
            .andExpect(jsonPath("$.[*].bannerText").value(hasItem(DEFAULT_BANNER_TEXT)))
            .andExpect(jsonPath("$.[*].specialPrice").value(hasItem(sameNumber(DEFAULT_SPECIAL_PRICE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].minQTY").value(hasItem(DEFAULT_MIN_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].maxQTY").value(hasItem(DEFAULT_MAX_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].steps").value(hasItem(DEFAULT_STEPS.doubleValue())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].leadTime").value(hasItem(DEFAULT_LEAD_TIME)))
            .andExpect(jsonPath("$.[*].reorderQty").value(hasItem(DEFAULT_REORDER_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].itemBarcode").value(hasItem(DEFAULT_ITEM_BARCODE)));

        // Check, that the count call also returns 1
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemShouldNotBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemCost(UPDATED_ITEM_COST)
            .bannerText(UPDATED_BANNER_TEXT)
            .specialPrice(UPDATED_SPECIAL_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .minQTY(UPDATED_MIN_QTY)
            .maxQTY(UPDATED_MAX_QTY)
            .steps(UPDATED_STEPS)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .leadTime(UPDATED_LEAD_TIME)
            .reorderQty(UPDATED_REORDER_QTY)
            .itemBarcode(UPDATED_ITEM_BARCODE);

        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemPrice()).isEqualByComparingTo(UPDATED_ITEM_PRICE);
        assertThat(testItem.getItemCost()).isEqualByComparingTo(UPDATED_ITEM_COST);
        assertThat(testItem.getBannerText()).isEqualTo(UPDATED_BANNER_TEXT);
        assertThat(testItem.getSpecialPrice()).isEqualByComparingTo(UPDATED_SPECIAL_PRICE);
        assertThat(testItem.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testItem.getMinQTY()).isEqualTo(UPDATED_MIN_QTY);
        assertThat(testItem.getMaxQTY()).isEqualTo(UPDATED_MAX_QTY);
        assertThat(testItem.getSteps()).isEqualTo(UPDATED_STEPS);
        assertThat(testItem.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testItem.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testItem.getReorderQty()).isEqualTo(UPDATED_REORDER_QTY);
        assertThat(testItem.getItemBarcode()).isEqualTo(UPDATED_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, item.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(item))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(item))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .specialPrice(UPDATED_SPECIAL_PRICE)
            .steps(UPDATED_STEPS)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .leadTime(UPDATED_LEAD_TIME)
            .reorderQty(UPDATED_REORDER_QTY)
            .itemBarcode(UPDATED_ITEM_BARCODE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemPrice()).isEqualByComparingTo(DEFAULT_ITEM_PRICE);
        assertThat(testItem.getItemCost()).isEqualByComparingTo(DEFAULT_ITEM_COST);
        assertThat(testItem.getBannerText()).isEqualTo(DEFAULT_BANNER_TEXT);
        assertThat(testItem.getSpecialPrice()).isEqualByComparingTo(UPDATED_SPECIAL_PRICE);
        assertThat(testItem.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testItem.getMinQTY()).isEqualTo(DEFAULT_MIN_QTY);
        assertThat(testItem.getMaxQTY()).isEqualTo(DEFAULT_MAX_QTY);
        assertThat(testItem.getSteps()).isEqualTo(UPDATED_STEPS);
        assertThat(testItem.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testItem.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testItem.getReorderQty()).isEqualTo(UPDATED_REORDER_QTY);
        assertThat(testItem.getItemBarcode()).isEqualTo(UPDATED_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemCost(UPDATED_ITEM_COST)
            .bannerText(UPDATED_BANNER_TEXT)
            .specialPrice(UPDATED_SPECIAL_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .minQTY(UPDATED_MIN_QTY)
            .maxQTY(UPDATED_MAX_QTY)
            .steps(UPDATED_STEPS)
            .longDescription(UPDATED_LONG_DESCRIPTION)
            .leadTime(UPDATED_LEAD_TIME)
            .reorderQty(UPDATED_REORDER_QTY)
            .itemBarcode(UPDATED_ITEM_BARCODE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getItemPrice()).isEqualByComparingTo(UPDATED_ITEM_PRICE);
        assertThat(testItem.getItemCost()).isEqualByComparingTo(UPDATED_ITEM_COST);
        assertThat(testItem.getBannerText()).isEqualTo(UPDATED_BANNER_TEXT);
        assertThat(testItem.getSpecialPrice()).isEqualByComparingTo(UPDATED_SPECIAL_PRICE);
        assertThat(testItem.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testItem.getMinQTY()).isEqualTo(UPDATED_MIN_QTY);
        assertThat(testItem.getMaxQTY()).isEqualTo(UPDATED_MAX_QTY);
        assertThat(testItem.getSteps()).isEqualTo(UPDATED_STEPS);
        assertThat(testItem.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testItem.getLeadTime()).isEqualTo(UPDATED_LEAD_TIME);
        assertThat(testItem.getReorderQty()).isEqualTo(UPDATED_REORDER_QTY);
        assertThat(testItem.getItemBarcode()).isEqualTo(UPDATED_ITEM_BARCODE);
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, item.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(item))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(item))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
