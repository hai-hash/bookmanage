package com.vnpt.bookmanage.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.Book;
import com.vnpt.bookmanage.domain.Catagory;
import com.vnpt.bookmanage.domain.Catagory;
import com.vnpt.bookmanage.domain.User;
import com.vnpt.bookmanage.repository.CatagoryRepository;
import com.vnpt.bookmanage.service.criteria.CatagoryCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CatagoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatagoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/catagories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatagoryRepository catagoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatagoryMockMvc;

    private Catagory catagory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catagory createEntity(EntityManager em) {
        Catagory catagory = new Catagory().name(DEFAULT_NAME).modifiedDate(DEFAULT_MODIFIED_DATE).isActive(DEFAULT_IS_ACTIVE);
        return catagory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catagory createUpdatedEntity(EntityManager em) {
        Catagory catagory = new Catagory().name(UPDATED_NAME).modifiedDate(UPDATED_MODIFIED_DATE).isActive(UPDATED_IS_ACTIVE);
        return catagory;
    }

    @BeforeEach
    public void initTest() {
        catagory = createEntity(em);
    }

    @Test
    @Transactional
    void createCatagory() throws Exception {
        int databaseSizeBeforeCreate = catagoryRepository.findAll().size();
        // Create the Catagory
        restCatagoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catagory)))
            .andExpect(status().isCreated());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeCreate + 1);
        Catagory testCatagory = catagoryList.get(catagoryList.size() - 1);
        assertThat(testCatagory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatagory.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testCatagory.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createCatagoryWithExistingId() throws Exception {
        // Create the Catagory with an existing ID
        catagory.setId(1L);

        int databaseSizeBeforeCreate = catagoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatagoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catagory)))
            .andExpect(status().isBadRequest());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catagoryRepository.findAll().size();
        // set the field null
        catagory.setName(null);

        // Create the Catagory, which fails.

        restCatagoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catagory)))
            .andExpect(status().isBadRequest());

        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = catagoryRepository.findAll().size();
        // set the field null
        catagory.setModifiedDate(null);

        // Create the Catagory, which fails.

        restCatagoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catagory)))
            .andExpect(status().isBadRequest());

        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = catagoryRepository.findAll().size();
        // set the field null
        catagory.setIsActive(null);

        // Create the Catagory, which fails.

        restCatagoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catagory)))
            .andExpect(status().isBadRequest());

        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatagories() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList
        restCatagoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catagory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getCatagory() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get the catagory
        restCatagoryMockMvc
            .perform(get(ENTITY_API_URL_ID, catagory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catagory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getCatagoriesByIdFiltering() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        Long id = catagory.getId();

        defaultCatagoryShouldBeFound("id.equals=" + id);
        defaultCatagoryShouldNotBeFound("id.notEquals=" + id);

        defaultCatagoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCatagoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCatagoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCatagoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCatagoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where name equals to DEFAULT_NAME
        defaultCatagoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the catagoryList where name equals to UPDATED_NAME
        defaultCatagoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatagoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where name not equals to DEFAULT_NAME
        defaultCatagoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the catagoryList where name not equals to UPDATED_NAME
        defaultCatagoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatagoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCatagoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the catagoryList where name equals to UPDATED_NAME
        defaultCatagoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatagoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where name is not null
        defaultCatagoryShouldBeFound("name.specified=true");

        // Get all the catagoryList where name is null
        defaultCatagoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCatagoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where name contains DEFAULT_NAME
        defaultCatagoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the catagoryList where name contains UPDATED_NAME
        defaultCatagoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatagoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where name does not contain DEFAULT_NAME
        defaultCatagoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the catagoryList where name does not contain UPDATED_NAME
        defaultCatagoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultCatagoryShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the catagoryList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultCatagoryShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate not equals to DEFAULT_MODIFIED_DATE
        defaultCatagoryShouldNotBeFound("modifiedDate.notEquals=" + DEFAULT_MODIFIED_DATE);

        // Get all the catagoryList where modifiedDate not equals to UPDATED_MODIFIED_DATE
        defaultCatagoryShouldBeFound("modifiedDate.notEquals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultCatagoryShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the catagoryList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultCatagoryShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate is not null
        defaultCatagoryShouldBeFound("modifiedDate.specified=true");

        // Get all the catagoryList where modifiedDate is null
        defaultCatagoryShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate is greater than or equal to DEFAULT_MODIFIED_DATE
        defaultCatagoryShouldBeFound("modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the catagoryList where modifiedDate is greater than or equal to UPDATED_MODIFIED_DATE
        defaultCatagoryShouldNotBeFound("modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate is less than or equal to DEFAULT_MODIFIED_DATE
        defaultCatagoryShouldBeFound("modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the catagoryList where modifiedDate is less than or equal to SMALLER_MODIFIED_DATE
        defaultCatagoryShouldNotBeFound("modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate is less than DEFAULT_MODIFIED_DATE
        defaultCatagoryShouldNotBeFound("modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the catagoryList where modifiedDate is less than UPDATED_MODIFIED_DATE
        defaultCatagoryShouldBeFound("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where modifiedDate is greater than DEFAULT_MODIFIED_DATE
        defaultCatagoryShouldNotBeFound("modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the catagoryList where modifiedDate is greater than SMALLER_MODIFIED_DATE
        defaultCatagoryShouldBeFound("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCatagoryShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the catagoryList where isActive equals to UPDATED_IS_ACTIVE
        defaultCatagoryShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultCatagoryShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the catagoryList where isActive not equals to UPDATED_IS_ACTIVE
        defaultCatagoryShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCatagoryShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the catagoryList where isActive equals to UPDATED_IS_ACTIVE
        defaultCatagoryShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCatagoriesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        // Get all the catagoryList where isActive is not null
        defaultCatagoryShouldBeFound("isActive.specified=true");

        // Get all the catagoryList where isActive is null
        defaultCatagoryShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllCatagoriesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        catagory.setUser(user);
        catagoryRepository.saveAndFlush(catagory);
        Long userId = user.getId();

        // Get all the catagoryList where user equals to userId
        defaultCatagoryShouldBeFound("userId.equals=" + userId);

        // Get all the catagoryList where user equals to (userId + 1)
        defaultCatagoryShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllCatagoriesByCatalogIsEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);
        Catagory catalog = CatagoryResourceIT.createEntity(em);
        em.persist(catalog);
        em.flush();
        catagory.setCatalog(catalog);
        catagoryRepository.saveAndFlush(catagory);
        Long catalogId = catalog.getId();

        // Get all the catagoryList where catalog equals to catalogId
        defaultCatagoryShouldBeFound("catalogId.equals=" + catalogId);

        // Get all the catagoryList where catalog equals to (catalogId + 1)
        defaultCatagoryShouldNotBeFound("catalogId.equals=" + (catalogId + 1));
    }

    @Test
    @Transactional
    void getAllCatagoriesByBookIsEqualToSomething() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);
        Book book = BookResourceIT.createEntity(em);
        em.persist(book);
        em.flush();
        catagory.addBook(book);
        catagoryRepository.saveAndFlush(catagory);
        Long bookId = book.getId();

        // Get all the catagoryList where book equals to bookId
        defaultCatagoryShouldBeFound("bookId.equals=" + bookId);

        // Get all the catagoryList where book equals to (bookId + 1)
        defaultCatagoryShouldNotBeFound("bookId.equals=" + (bookId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatagoryShouldBeFound(String filter) throws Exception {
        restCatagoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catagory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCatagoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatagoryShouldNotBeFound(String filter) throws Exception {
        restCatagoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatagoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCatagory() throws Exception {
        // Get the catagory
        restCatagoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatagory() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();

        // Update the catagory
        Catagory updatedCatagory = catagoryRepository.findById(catagory.getId()).get();
        // Disconnect from session so that the updates on updatedCatagory are not directly saved in db
        em.detach(updatedCatagory);
        updatedCatagory.name(UPDATED_NAME).modifiedDate(UPDATED_MODIFIED_DATE).isActive(UPDATED_IS_ACTIVE);

        restCatagoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatagory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCatagory))
            )
            .andExpect(status().isOk());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
        Catagory testCatagory = catagoryList.get(catagoryList.size() - 1);
        assertThat(testCatagory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatagory.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testCatagory.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingCatagory() throws Exception {
        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();
        catagory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatagoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catagory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catagory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatagory() throws Exception {
        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();
        catagory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatagoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catagory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatagory() throws Exception {
        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();
        catagory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatagoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catagory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatagoryWithPatch() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();

        // Update the catagory using partial update
        Catagory partialUpdatedCatagory = new Catagory();
        partialUpdatedCatagory.setId(catagory.getId());

        partialUpdatedCatagory.name(UPDATED_NAME);

        restCatagoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatagory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatagory))
            )
            .andExpect(status().isOk());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
        Catagory testCatagory = catagoryList.get(catagoryList.size() - 1);
        assertThat(testCatagory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatagory.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testCatagory.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateCatagoryWithPatch() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();

        // Update the catagory using partial update
        Catagory partialUpdatedCatagory = new Catagory();
        partialUpdatedCatagory.setId(catagory.getId());

        partialUpdatedCatagory.name(UPDATED_NAME).modifiedDate(UPDATED_MODIFIED_DATE).isActive(UPDATED_IS_ACTIVE);

        restCatagoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatagory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatagory))
            )
            .andExpect(status().isOk());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
        Catagory testCatagory = catagoryList.get(catagoryList.size() - 1);
        assertThat(testCatagory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatagory.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testCatagory.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingCatagory() throws Exception {
        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();
        catagory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatagoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catagory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catagory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatagory() throws Exception {
        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();
        catagory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatagoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catagory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatagory() throws Exception {
        int databaseSizeBeforeUpdate = catagoryRepository.findAll().size();
        catagory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatagoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(catagory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catagory in the database
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatagory() throws Exception {
        // Initialize the database
        catagoryRepository.saveAndFlush(catagory);

        int databaseSizeBeforeDelete = catagoryRepository.findAll().size();

        // Delete the catagory
        restCatagoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, catagory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Catagory> catagoryList = catagoryRepository.findAll();
        assertThat(catagoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
