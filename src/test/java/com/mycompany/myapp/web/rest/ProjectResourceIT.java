package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ExUser;
import com.mycompany.myapp.domain.Location;
import com.mycompany.myapp.domain.Project;
import com.mycompany.myapp.repository.ProjectRepository;
import com.mycompany.myapp.service.ProjectService;
import com.mycompany.myapp.service.criteria.ProjectCriteria;
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
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjectResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_COMPLETION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMPLETION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMPLETION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REG_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REG_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_PROGRESS = 1;
    private static final Integer UPDATED_PROGRESS = 2;
    private static final Integer SMALLER_PROGRESS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectRepository projectRepository;

    @Mock
    private ProjectRepository projectRepositoryMock;

    @Mock
    private ProjectService projectServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
            .description(DEFAULT_DESCRIPTION)
            .completionDate(DEFAULT_COMPLETION_DATE)
            .regNumber(DEFAULT_REG_NUMBER)
            .notes(DEFAULT_NOTES)
            .address(DEFAULT_ADDRESS)
            .priority(DEFAULT_PRIORITY)
            .progress(DEFAULT_PROGRESS);
        return project;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .completionDate(UPDATED_COMPLETION_DATE)
            .regNumber(UPDATED_REG_NUMBER)
            .notes(UPDATED_NOTES)
            .address(UPDATED_ADDRESS)
            .priority(UPDATED_PRIORITY)
            .progress(UPDATED_PROGRESS);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();
        // Create the Project
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProject.getCompletionDate()).isEqualTo(DEFAULT_COMPLETION_DATE);
        assertThat(testProject.getRegNumber()).isEqualTo(DEFAULT_REG_NUMBER);
        assertThat(testProject.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testProject.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testProject.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testProject.getProgress()).isEqualTo(DEFAULT_PROGRESS);
    }

    @Test
    @Transactional
    void createProjectWithExistingId() throws Exception {
        // Create the Project with an existing ID
        project.setId(1L);

        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setName(null);

        // Create the Project, which fails.

        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].regNumber").value(hasItem(DEFAULT_REG_NUMBER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectsWithEagerRelationshipsIsEnabled() throws Exception {
        when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(projectRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.completionDate").value(DEFAULT_COMPLETION_DATE.toString()))
            .andExpect(jsonPath("$.regNumber").value(DEFAULT_REG_NUMBER))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS));
    }

    @Test
    @Transactional
    void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        Long id = project.getId();

        defaultProjectShouldBeFound("id.equals=" + id);
        defaultProjectShouldNotBeFound("id.notEquals=" + id);

        defaultProjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.greaterThan=" + id);

        defaultProjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjectsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where code equals to DEFAULT_CODE
        defaultProjectShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the projectList where code equals to UPDATED_CODE
        defaultProjectShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProjectsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProjectShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the projectList where code equals to UPDATED_CODE
        defaultProjectShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProjectsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where code is not null
        defaultProjectShouldBeFound("code.specified=true");

        // Get all the projectList where code is null
        defaultProjectShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCodeContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where code contains DEFAULT_CODE
        defaultProjectShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the projectList where code contains UPDATED_CODE
        defaultProjectShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProjectsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where code does not contain DEFAULT_CODE
        defaultProjectShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the projectList where code does not contain UPDATED_CODE
        defaultProjectShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProjectsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name equals to DEFAULT_NAME
        defaultProjectShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the projectList where name equals to UPDATED_NAME
        defaultProjectShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProjectShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the projectList where name equals to UPDATED_NAME
        defaultProjectShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name is not null
        defaultProjectShouldBeFound("name.specified=true");

        // Get all the projectList where name is null
        defaultProjectShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByNameContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name contains DEFAULT_NAME
        defaultProjectShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the projectList where name contains UPDATED_NAME
        defaultProjectShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name does not contain DEFAULT_NAME
        defaultProjectShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the projectList where name does not contain UPDATED_NAME
        defaultProjectShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where isActive equals to DEFAULT_IS_ACTIVE
        defaultProjectShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the projectList where isActive equals to UPDATED_IS_ACTIVE
        defaultProjectShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProjectsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultProjectShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the projectList where isActive equals to UPDATED_IS_ACTIVE
        defaultProjectShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProjectsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where isActive is not null
        defaultProjectShouldBeFound("isActive.specified=true");

        // Get all the projectList where isActive is null
        defaultProjectShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description equals to DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description is not null
        defaultProjectShouldBeFound("description.specified=true");

        // Get all the projectList where description is null
        defaultProjectShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description contains DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description contains UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description does not contain DEFAULT_DESCRIPTION
        defaultProjectShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description does not contain UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProjectsByCompletionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where completionDate equals to DEFAULT_COMPLETION_DATE
        defaultProjectShouldBeFound("completionDate.equals=" + DEFAULT_COMPLETION_DATE);

        // Get all the projectList where completionDate equals to UPDATED_COMPLETION_DATE
        defaultProjectShouldNotBeFound("completionDate.equals=" + UPDATED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByCompletionDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where completionDate in DEFAULT_COMPLETION_DATE or UPDATED_COMPLETION_DATE
        defaultProjectShouldBeFound("completionDate.in=" + DEFAULT_COMPLETION_DATE + "," + UPDATED_COMPLETION_DATE);

        // Get all the projectList where completionDate equals to UPDATED_COMPLETION_DATE
        defaultProjectShouldNotBeFound("completionDate.in=" + UPDATED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByCompletionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where completionDate is not null
        defaultProjectShouldBeFound("completionDate.specified=true");

        // Get all the projectList where completionDate is null
        defaultProjectShouldNotBeFound("completionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCompletionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where completionDate is greater than or equal to DEFAULT_COMPLETION_DATE
        defaultProjectShouldBeFound("completionDate.greaterThanOrEqual=" + DEFAULT_COMPLETION_DATE);

        // Get all the projectList where completionDate is greater than or equal to UPDATED_COMPLETION_DATE
        defaultProjectShouldNotBeFound("completionDate.greaterThanOrEqual=" + UPDATED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByCompletionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where completionDate is less than or equal to DEFAULT_COMPLETION_DATE
        defaultProjectShouldBeFound("completionDate.lessThanOrEqual=" + DEFAULT_COMPLETION_DATE);

        // Get all the projectList where completionDate is less than or equal to SMALLER_COMPLETION_DATE
        defaultProjectShouldNotBeFound("completionDate.lessThanOrEqual=" + SMALLER_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByCompletionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where completionDate is less than DEFAULT_COMPLETION_DATE
        defaultProjectShouldNotBeFound("completionDate.lessThan=" + DEFAULT_COMPLETION_DATE);

        // Get all the projectList where completionDate is less than UPDATED_COMPLETION_DATE
        defaultProjectShouldBeFound("completionDate.lessThan=" + UPDATED_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByCompletionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where completionDate is greater than DEFAULT_COMPLETION_DATE
        defaultProjectShouldNotBeFound("completionDate.greaterThan=" + DEFAULT_COMPLETION_DATE);

        // Get all the projectList where completionDate is greater than SMALLER_COMPLETION_DATE
        defaultProjectShouldBeFound("completionDate.greaterThan=" + SMALLER_COMPLETION_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByRegNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where regNumber equals to DEFAULT_REG_NUMBER
        defaultProjectShouldBeFound("regNumber.equals=" + DEFAULT_REG_NUMBER);

        // Get all the projectList where regNumber equals to UPDATED_REG_NUMBER
        defaultProjectShouldNotBeFound("regNumber.equals=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByRegNumberIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where regNumber in DEFAULT_REG_NUMBER or UPDATED_REG_NUMBER
        defaultProjectShouldBeFound("regNumber.in=" + DEFAULT_REG_NUMBER + "," + UPDATED_REG_NUMBER);

        // Get all the projectList where regNumber equals to UPDATED_REG_NUMBER
        defaultProjectShouldNotBeFound("regNumber.in=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByRegNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where regNumber is not null
        defaultProjectShouldBeFound("regNumber.specified=true");

        // Get all the projectList where regNumber is null
        defaultProjectShouldNotBeFound("regNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByRegNumberContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where regNumber contains DEFAULT_REG_NUMBER
        defaultProjectShouldBeFound("regNumber.contains=" + DEFAULT_REG_NUMBER);

        // Get all the projectList where regNumber contains UPDATED_REG_NUMBER
        defaultProjectShouldNotBeFound("regNumber.contains=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByRegNumberNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where regNumber does not contain DEFAULT_REG_NUMBER
        defaultProjectShouldNotBeFound("regNumber.doesNotContain=" + DEFAULT_REG_NUMBER);

        // Get all the projectList where regNumber does not contain UPDATED_REG_NUMBER
        defaultProjectShouldBeFound("regNumber.doesNotContain=" + UPDATED_REG_NUMBER);
    }

    @Test
    @Transactional
    void getAllProjectsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where notes equals to DEFAULT_NOTES
        defaultProjectShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the projectList where notes equals to UPDATED_NOTES
        defaultProjectShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllProjectsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultProjectShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the projectList where notes equals to UPDATED_NOTES
        defaultProjectShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllProjectsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where notes is not null
        defaultProjectShouldBeFound("notes.specified=true");

        // Get all the projectList where notes is null
        defaultProjectShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByNotesContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where notes contains DEFAULT_NOTES
        defaultProjectShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the projectList where notes contains UPDATED_NOTES
        defaultProjectShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllProjectsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where notes does not contain DEFAULT_NOTES
        defaultProjectShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the projectList where notes does not contain UPDATED_NOTES
        defaultProjectShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address equals to DEFAULT_ADDRESS
        defaultProjectShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the projectList where address equals to UPDATED_ADDRESS
        defaultProjectShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultProjectShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the projectList where address equals to UPDATED_ADDRESS
        defaultProjectShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address is not null
        defaultProjectShouldBeFound("address.specified=true");

        // Get all the projectList where address is null
        defaultProjectShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByAddressContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address contains DEFAULT_ADDRESS
        defaultProjectShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the projectList where address contains UPDATED_ADDRESS
        defaultProjectShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where address does not contain DEFAULT_ADDRESS
        defaultProjectShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the projectList where address does not contain UPDATED_ADDRESS
        defaultProjectShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority equals to DEFAULT_PRIORITY
        defaultProjectShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the projectList where priority equals to UPDATED_PRIORITY
        defaultProjectShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultProjectShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the projectList where priority equals to UPDATED_PRIORITY
        defaultProjectShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority is not null
        defaultProjectShouldBeFound("priority.specified=true");

        // Get all the projectList where priority is null
        defaultProjectShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority contains DEFAULT_PRIORITY
        defaultProjectShouldBeFound("priority.contains=" + DEFAULT_PRIORITY);

        // Get all the projectList where priority contains UPDATED_PRIORITY
        defaultProjectShouldNotBeFound("priority.contains=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority does not contain DEFAULT_PRIORITY
        defaultProjectShouldNotBeFound("priority.doesNotContain=" + DEFAULT_PRIORITY);

        // Get all the projectList where priority does not contain UPDATED_PRIORITY
        defaultProjectShouldBeFound("priority.doesNotContain=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProjectsByProgressIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where progress equals to DEFAULT_PROGRESS
        defaultProjectShouldBeFound("progress.equals=" + DEFAULT_PROGRESS);

        // Get all the projectList where progress equals to UPDATED_PROGRESS
        defaultProjectShouldNotBeFound("progress.equals=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByProgressIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where progress in DEFAULT_PROGRESS or UPDATED_PROGRESS
        defaultProjectShouldBeFound("progress.in=" + DEFAULT_PROGRESS + "," + UPDATED_PROGRESS);

        // Get all the projectList where progress equals to UPDATED_PROGRESS
        defaultProjectShouldNotBeFound("progress.in=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByProgressIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where progress is not null
        defaultProjectShouldBeFound("progress.specified=true");

        // Get all the projectList where progress is null
        defaultProjectShouldNotBeFound("progress.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByProgressIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where progress is greater than or equal to DEFAULT_PROGRESS
        defaultProjectShouldBeFound("progress.greaterThanOrEqual=" + DEFAULT_PROGRESS);

        // Get all the projectList where progress is greater than or equal to UPDATED_PROGRESS
        defaultProjectShouldNotBeFound("progress.greaterThanOrEqual=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByProgressIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where progress is less than or equal to DEFAULT_PROGRESS
        defaultProjectShouldBeFound("progress.lessThanOrEqual=" + DEFAULT_PROGRESS);

        // Get all the projectList where progress is less than or equal to SMALLER_PROGRESS
        defaultProjectShouldNotBeFound("progress.lessThanOrEqual=" + SMALLER_PROGRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByProgressIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where progress is less than DEFAULT_PROGRESS
        defaultProjectShouldNotBeFound("progress.lessThan=" + DEFAULT_PROGRESS);

        // Get all the projectList where progress is less than UPDATED_PROGRESS
        defaultProjectShouldBeFound("progress.lessThan=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByProgressIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where progress is greater than DEFAULT_PROGRESS
        defaultProjectShouldNotBeFound("progress.greaterThan=" + DEFAULT_PROGRESS);

        // Get all the projectList where progress is greater than SMALLER_PROGRESS
        defaultProjectShouldBeFound("progress.greaterThan=" + SMALLER_PROGRESS);
    }

    @Test
    @Transactional
    void getAllProjectsByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            location = LocationResourceIT.createEntity(em);
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        project.setLocation(location);
        projectRepository.saveAndFlush(project);
        Long locationId = location.getId();

        // Get all the projectList where location equals to locationId
        defaultProjectShouldBeFound("locationId.equals=" + locationId);

        // Get all the projectList where location equals to (locationId + 1)
        defaultProjectShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByExUserIsEqualToSomething() throws Exception {
        ExUser exUser;
        if (TestUtil.findAll(em, ExUser.class).isEmpty()) {
            projectRepository.saveAndFlush(project);
            exUser = ExUserResourceIT.createEntity(em);
        } else {
            exUser = TestUtil.findAll(em, ExUser.class).get(0);
        }
        em.persist(exUser);
        em.flush();
        project.setExUser(exUser);
        projectRepository.saveAndFlush(project);
        Long exUserId = exUser.getId();

        // Get all the projectList where exUser equals to exUserId
        defaultProjectShouldBeFound("exUserId.equals=" + exUserId);

        // Get all the projectList where exUser equals to (exUserId + 1)
        defaultProjectShouldNotBeFound("exUserId.equals=" + (exUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].completionDate").value(hasItem(DEFAULT_COMPLETION_DATE.toString())))
            .andExpect(jsonPath("$.[*].regNumber").value(hasItem(DEFAULT_REG_NUMBER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)));

        // Check, that the count call also returns 1
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .completionDate(UPDATED_COMPLETION_DATE)
            .regNumber(UPDATED_REG_NUMBER)
            .notes(UPDATED_NOTES)
            .address(UPDATED_ADDRESS)
            .priority(UPDATED_PRIORITY)
            .progress(UPDATED_PROGRESS);

        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getCompletionDate()).isEqualTo(UPDATED_COMPLETION_DATE);
        assertThat(testProject.getRegNumber()).isEqualTo(UPDATED_REG_NUMBER);
        assertThat(testProject.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testProject.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProject.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testProject.getProgress()).isEqualTo(UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void putNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, project.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .completionDate(UPDATED_COMPLETION_DATE)
            .notes(UPDATED_NOTES)
            .address(UPDATED_ADDRESS)
            .priority(UPDATED_PRIORITY)
            .progress(UPDATED_PROGRESS);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getCompletionDate()).isEqualTo(UPDATED_COMPLETION_DATE);
        assertThat(testProject.getRegNumber()).isEqualTo(DEFAULT_REG_NUMBER);
        assertThat(testProject.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testProject.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProject.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testProject.getProgress()).isEqualTo(UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void fullUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .completionDate(UPDATED_COMPLETION_DATE)
            .regNumber(UPDATED_REG_NUMBER)
            .notes(UPDATED_NOTES)
            .address(UPDATED_ADDRESS)
            .priority(UPDATED_PRIORITY)
            .progress(UPDATED_PROGRESS);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getCompletionDate()).isEqualTo(UPDATED_COMPLETION_DATE);
        assertThat(testProject.getRegNumber()).isEqualTo(UPDATED_REG_NUMBER);
        assertThat(testProject.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testProject.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProject.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testProject.getProgress()).isEqualTo(UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void patchNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, project.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(project))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();
        project.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, project.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
