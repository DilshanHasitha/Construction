package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserRole;
import com.mycompany.myapp.repository.ExUserRepository;
import com.mycompany.myapp.service.ExUserService;
import com.mycompany.myapp.service.criteria.ExUserCriteria;
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
 * Integration tests for the {@link ExUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExUserResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "g@`b.K{/L<";
    private static final String UPDATED_EMAIL = "-_Q0a@gpUC.$";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;
    private static final Integer SMALLER_PHONE = 1 - 1;

    private static final String DEFAULT_BR_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BR_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ex-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExUserRepository exUserRepository;

    @Mock
    private ExUserRepository exUserRepositoryMock;

    @Mock
    private ExUserService exUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExUserMockMvc;

    private ExUser exUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExUser createEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .login(DEFAULT_LOGIN)
            .userName(DEFAULT_USER_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .phone(DEFAULT_PHONE)
            .brNumber(DEFAULT_BR_NUMBER);
        return exUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExUser createUpdatedEntity(EntityManager em) {
        ExUser exUser = new ExUser()
            .login(UPDATED_LOGIN)
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .brNumber(UPDATED_BR_NUMBER);
        return exUser;
    }

    @BeforeEach
    public void initTest() {
        exUser = createEntity(em);
    }

    @Test
    @Transactional
    void createExUser() throws Exception {
        int databaseSizeBeforeCreate = exUserRepository.findAll().size();
        // Create the ExUser
        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isCreated());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testExUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testExUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testExUser.getBrNumber()).isEqualTo(DEFAULT_BR_NUMBER);
    }

    @Test
    @Transactional
    void createExUserWithExistingId() throws Exception {
        // Create the ExUser with an existing ID
        exUser.setId(1L);

        int databaseSizeBeforeCreate = exUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setLogin(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setUserName(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = exUserRepository.findAll().size();
        // set the field null
        exUser.setEmail(null);

        // Create the ExUser, which fails.

        restExUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isBadRequest());

        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExUsers() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].brNumber").value(hasItem(DEFAULT_BR_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(exUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(exUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(exUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(exUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get the exUser
        restExUserMockMvc
            .perform(get(ENTITY_API_URL_ID, exUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exUser.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.brNumber").value(DEFAULT_BR_NUMBER));
    }

    @Test
    @Transactional
    void getExUsersByIdFiltering() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        Long id = exUser.getId();

        defaultExUserShouldBeFound("id.equals=" + id);
        defaultExUserShouldNotBeFound("id.notEquals=" + id);

        defaultExUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExUserShouldNotBeFound("id.greaterThan=" + id);

        defaultExUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login equals to DEFAULT_LOGIN
        defaultExUserShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the exUserList where login equals to UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultExUserShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the exUserList where login equals to UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login is not null
        defaultExUserShouldBeFound("login.specified=true");

        // Get all the exUserList where login is null
        defaultExUserShouldNotBeFound("login.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByLoginContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login contains DEFAULT_LOGIN
        defaultExUserShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the exUserList where login contains UPDATED_LOGIN
        defaultExUserShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where login does not contain DEFAULT_LOGIN
        defaultExUserShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the exUserList where login does not contain UPDATED_LOGIN
        defaultExUserShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllExUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userName equals to DEFAULT_USER_NAME
        defaultExUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the exUserList where userName equals to UPDATED_USER_NAME
        defaultExUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultExUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the exUserList where userName equals to UPDATED_USER_NAME
        defaultExUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userName is not null
        defaultExUserShouldBeFound("userName.specified=true");

        // Get all the exUserList where userName is null
        defaultExUserShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userName contains DEFAULT_USER_NAME
        defaultExUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the exUserList where userName contains UPDATED_USER_NAME
        defaultExUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where userName does not contain DEFAULT_USER_NAME
        defaultExUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the exUserList where userName does not contain UPDATED_USER_NAME
        defaultExUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultExUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName equals to UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultExUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the exUserList where firstName equals to UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName is not null
        defaultExUserShouldBeFound("firstName.specified=true");

        // Get all the exUserList where firstName is null
        defaultExUserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName contains DEFAULT_FIRST_NAME
        defaultExUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName contains UPDATED_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultExUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the exUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultExUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName equals to DEFAULT_LAST_NAME
        defaultExUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName equals to UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultExUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the exUserList where lastName equals to UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName is not null
        defaultExUserShouldBeFound("lastName.specified=true");

        // Get all the exUserList where lastName is null
        defaultExUserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName contains DEFAULT_LAST_NAME
        defaultExUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName contains UPDATED_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultExUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the exUserList where lastName does not contain UPDATED_LAST_NAME
        defaultExUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email equals to DEFAULT_EMAIL
        defaultExUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the exUserList where email equals to UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultExUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the exUserList where email equals to UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email is not null
        defaultExUserShouldBeFound("email.specified=true");

        // Get all the exUserList where email is null
        defaultExUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email contains DEFAULT_EMAIL
        defaultExUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the exUserList where email contains UPDATED_EMAIL
        defaultExUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where email does not contain DEFAULT_EMAIL
        defaultExUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the exUserList where email does not contain UPDATED_EMAIL
        defaultExUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllExUsersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive equals to DEFAULT_IS_ACTIVE
        defaultExUserShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the exUserList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExUsersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultExUserShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the exUserList where isActive equals to UPDATED_IS_ACTIVE
        defaultExUserShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExUsersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where isActive is not null
        defaultExUserShouldBeFound("isActive.specified=true");

        // Get all the exUserList where isActive is null
        defaultExUserShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone equals to DEFAULT_PHONE
        defaultExUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the exUserList where phone equals to UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultExUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the exUserList where phone equals to UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone is not null
        defaultExUserShouldBeFound("phone.specified=true");

        // Get all the exUserList where phone is null
        defaultExUserShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone is greater than or equal to DEFAULT_PHONE
        defaultExUserShouldBeFound("phone.greaterThanOrEqual=" + DEFAULT_PHONE);

        // Get all the exUserList where phone is greater than or equal to UPDATED_PHONE
        defaultExUserShouldNotBeFound("phone.greaterThanOrEqual=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone is less than or equal to DEFAULT_PHONE
        defaultExUserShouldBeFound("phone.lessThanOrEqual=" + DEFAULT_PHONE);

        // Get all the exUserList where phone is less than or equal to SMALLER_PHONE
        defaultExUserShouldNotBeFound("phone.lessThanOrEqual=" + SMALLER_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsLessThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone is less than DEFAULT_PHONE
        defaultExUserShouldNotBeFound("phone.lessThan=" + DEFAULT_PHONE);

        // Get all the exUserList where phone is less than UPDATED_PHONE
        defaultExUserShouldBeFound("phone.lessThan=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByPhoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where phone is greater than DEFAULT_PHONE
        defaultExUserShouldNotBeFound("phone.greaterThan=" + DEFAULT_PHONE);

        // Get all the exUserList where phone is greater than SMALLER_PHONE
        defaultExUserShouldBeFound("phone.greaterThan=" + SMALLER_PHONE);
    }

    @Test
    @Transactional
    void getAllExUsersByBrNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where brNumber equals to DEFAULT_BR_NUMBER
        defaultExUserShouldBeFound("brNumber.equals=" + DEFAULT_BR_NUMBER);

        // Get all the exUserList where brNumber equals to UPDATED_BR_NUMBER
        defaultExUserShouldNotBeFound("brNumber.equals=" + UPDATED_BR_NUMBER);
    }

    @Test
    @Transactional
    void getAllExUsersByBrNumberIsInShouldWork() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where brNumber in DEFAULT_BR_NUMBER or UPDATED_BR_NUMBER
        defaultExUserShouldBeFound("brNumber.in=" + DEFAULT_BR_NUMBER + "," + UPDATED_BR_NUMBER);

        // Get all the exUserList where brNumber equals to UPDATED_BR_NUMBER
        defaultExUserShouldNotBeFound("brNumber.in=" + UPDATED_BR_NUMBER);
    }

    @Test
    @Transactional
    void getAllExUsersByBrNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where brNumber is not null
        defaultExUserShouldBeFound("brNumber.specified=true");

        // Get all the exUserList where brNumber is null
        defaultExUserShouldNotBeFound("brNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllExUsersByBrNumberContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where brNumber contains DEFAULT_BR_NUMBER
        defaultExUserShouldBeFound("brNumber.contains=" + DEFAULT_BR_NUMBER);

        // Get all the exUserList where brNumber contains UPDATED_BR_NUMBER
        defaultExUserShouldNotBeFound("brNumber.contains=" + UPDATED_BR_NUMBER);
    }

    @Test
    @Transactional
    void getAllExUsersByBrNumberNotContainsSomething() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        // Get all the exUserList where brNumber does not contain DEFAULT_BR_NUMBER
        defaultExUserShouldNotBeFound("brNumber.doesNotContain=" + DEFAULT_BR_NUMBER);

        // Get all the exUserList where brNumber does not contain UPDATED_BR_NUMBER
        defaultExUserShouldBeFound("brNumber.doesNotContain=" + UPDATED_BR_NUMBER);
    }

    @Test
    @Transactional
    void getAllExUsersByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            exUserRepository.saveAndFlush(exUser);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        exUser.setUser(user);
        exUserRepository.saveAndFlush(exUser);
        Long userId = user.getId();

        // Get all the exUserList where user equals to userId
        defaultExUserShouldBeFound("userId.equals=" + userId);

        // Get all the exUserList where user equals to (userId + 1)
        defaultExUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllExUsersByUserRoleIsEqualToSomething() throws Exception {
        UserRole userRole;
        if (TestUtil.findAll(em, UserRole.class).isEmpty()) {
            exUserRepository.saveAndFlush(exUser);
            userRole = UserRoleResourceIT.createEntity(em);
        } else {
            userRole = TestUtil.findAll(em, UserRole.class).get(0);
        }
        em.persist(userRole);
        em.flush();
        exUser.setUserRole(userRole);
        exUserRepository.saveAndFlush(exUser);
        Long userRoleId = userRole.getId();

        // Get all the exUserList where userRole equals to userRoleId
        defaultExUserShouldBeFound("userRoleId.equals=" + userRoleId);

        // Get all the exUserList where userRole equals to (userRoleId + 1)
        defaultExUserShouldNotBeFound("userRoleId.equals=" + (userRoleId + 1));
    }

    @Test
    @Transactional
    void getAllExUsersByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            exUserRepository.saveAndFlush(exUser);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        exUser.setCompany(company);
        exUserRepository.saveAndFlush(exUser);
        Long companyId = company.getId();

        // Get all the exUserList where company equals to companyId
        defaultExUserShouldBeFound("companyId.equals=" + companyId);

        // Get all the exUserList where company equals to (companyId + 1)
        defaultExUserShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExUserShouldBeFound(String filter) throws Exception {
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].brNumber").value(hasItem(DEFAULT_BR_NUMBER)));

        // Check, that the count call also returns 1
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExUserShouldNotBeFound(String filter) throws Exception {
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExUser() throws Exception {
        // Get the exUser
        restExUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser
        ExUser updatedExUser = exUserRepository.findById(exUser.getId()).get();
        // Disconnect from session so that the updates on updatedExUser are not directly saved in db
        em.detach(updatedExUser);
        updatedExUser
            .login(UPDATED_LOGIN)
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .brNumber(UPDATED_BR_NUMBER);

        restExUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExUser))
            )
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testExUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testExUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testExUser.getBrNumber()).isEqualTo(UPDATED_BR_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExUserWithPatch() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser using partial update
        ExUser partialUpdatedExUser = new ExUser();
        partialUpdatedExUser.setId(exUser.getId());

        partialUpdatedExUser.userName(UPDATED_USER_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE);

        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExUser))
            )
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testExUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testExUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testExUser.getBrNumber()).isEqualTo(DEFAULT_BR_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateExUserWithPatch() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();

        // Update the exUser using partial update
        ExUser partialUpdatedExUser = new ExUser();
        partialUpdatedExUser.setId(exUser.getId());

        partialUpdatedExUser
            .login(UPDATED_LOGIN)
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .brNumber(UPDATED_BR_NUMBER);

        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExUser))
            )
            .andExpect(status().isOk());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
        ExUser testExUser = exUserList.get(exUserList.size() - 1);
        assertThat(testExUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testExUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testExUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testExUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testExUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testExUser.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testExUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testExUser.getBrNumber()).isEqualTo(UPDATED_BR_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExUser() throws Exception {
        int databaseSizeBeforeUpdate = exUserRepository.findAll().size();
        exUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExUser in the database
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExUser() throws Exception {
        // Initialize the database
        exUserRepository.saveAndFlush(exUser);

        int databaseSizeBeforeDelete = exUserRepository.findAll().size();

        // Delete the exUser
        restExUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, exUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExUser> exUserList = exUserRepository.findAll();
        assertThat(exUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
