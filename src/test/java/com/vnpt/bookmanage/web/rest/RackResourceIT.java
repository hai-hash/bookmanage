package com.vnpt.bookmanage.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.BookItem;
import com.vnpt.bookmanage.domain.Rack;
import com.vnpt.bookmanage.domain.User;
import com.vnpt.bookmanage.repository.RackRepository;
import com.vnpt.bookmanage.service.criteria.RackCriteria;
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
 * Integration tests for the {@link RackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RackResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final Integer SMALLER_NUMBER = 1 - 1;

    private static final String DEFAULT_LOCATION_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_IDENTIFIER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/racks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRackMockMvc;

    private Rack rack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rack createEntity(EntityManager em) {
        Rack rack = new Rack()
            .number(DEFAULT_NUMBER)
            .locationIdentifier(DEFAULT_LOCATION_IDENTIFIER)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .isActive(DEFAULT_IS_ACTIVE);
        return rack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rack createUpdatedEntity(EntityManager em) {
        Rack rack = new Rack()
            .number(UPDATED_NUMBER)
            .locationIdentifier(UPDATED_LOCATION_IDENTIFIER)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .isActive(UPDATED_IS_ACTIVE);
        return rack;
    }

    @BeforeEach
    public void initTest() {
        rack = createEntity(em);
    }

    @Test
    @Transactional
    void createRack() throws Exception {
        int databaseSizeBeforeCreate = rackRepository.findAll().size();
        // Create the Rack
        restRackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isCreated());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeCreate + 1);
        Rack testRack = rackList.get(rackList.size() - 1);
        assertThat(testRack.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testRack.getLocationIdentifier()).isEqualTo(DEFAULT_LOCATION_IDENTIFIER);
        assertThat(testRack.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testRack.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createRackWithExistingId() throws Exception {
        // Create the Rack with an existing ID
        rack.setId(1L);

        int databaseSizeBeforeCreate = rackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rackRepository.findAll().size();
        // set the field null
        rack.setModifiedDate(null);

        // Create the Rack, which fails.

        restRackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isBadRequest());

        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = rackRepository.findAll().size();
        // set the field null
        rack.setIsActive(null);

        // Create the Rack, which fails.

        restRackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isBadRequest());

        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRacks() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList
        restRackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rack.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].locationIdentifier").value(hasItem(DEFAULT_LOCATION_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getRack() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get the rack
        restRackMockMvc
            .perform(get(ENTITY_API_URL_ID, rack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rack.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.locationIdentifier").value(DEFAULT_LOCATION_IDENTIFIER))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getRacksByIdFiltering() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        Long id = rack.getId();

        defaultRackShouldBeFound("id.equals=" + id);
        defaultRackShouldNotBeFound("id.notEquals=" + id);

        defaultRackShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRackShouldNotBeFound("id.greaterThan=" + id);

        defaultRackShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRackShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number equals to DEFAULT_NUMBER
        defaultRackShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the rackList where number equals to UPDATED_NUMBER
        defaultRackShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number not equals to DEFAULT_NUMBER
        defaultRackShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the rackList where number not equals to UPDATED_NUMBER
        defaultRackShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultRackShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the rackList where number equals to UPDATED_NUMBER
        defaultRackShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number is not null
        defaultRackShouldBeFound("number.specified=true");

        // Get all the rackList where number is null
        defaultRackShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number is greater than or equal to DEFAULT_NUMBER
        defaultRackShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the rackList where number is greater than or equal to UPDATED_NUMBER
        defaultRackShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number is less than or equal to DEFAULT_NUMBER
        defaultRackShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the rackList where number is less than or equal to SMALLER_NUMBER
        defaultRackShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number is less than DEFAULT_NUMBER
        defaultRackShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the rackList where number is less than UPDATED_NUMBER
        defaultRackShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllRacksByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where number is greater than DEFAULT_NUMBER
        defaultRackShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the rackList where number is greater than SMALLER_NUMBER
        defaultRackShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllRacksByLocationIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where locationIdentifier equals to DEFAULT_LOCATION_IDENTIFIER
        defaultRackShouldBeFound("locationIdentifier.equals=" + DEFAULT_LOCATION_IDENTIFIER);

        // Get all the rackList where locationIdentifier equals to UPDATED_LOCATION_IDENTIFIER
        defaultRackShouldNotBeFound("locationIdentifier.equals=" + UPDATED_LOCATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRacksByLocationIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where locationIdentifier not equals to DEFAULT_LOCATION_IDENTIFIER
        defaultRackShouldNotBeFound("locationIdentifier.notEquals=" + DEFAULT_LOCATION_IDENTIFIER);

        // Get all the rackList where locationIdentifier not equals to UPDATED_LOCATION_IDENTIFIER
        defaultRackShouldBeFound("locationIdentifier.notEquals=" + UPDATED_LOCATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRacksByLocationIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where locationIdentifier in DEFAULT_LOCATION_IDENTIFIER or UPDATED_LOCATION_IDENTIFIER
        defaultRackShouldBeFound("locationIdentifier.in=" + DEFAULT_LOCATION_IDENTIFIER + "," + UPDATED_LOCATION_IDENTIFIER);

        // Get all the rackList where locationIdentifier equals to UPDATED_LOCATION_IDENTIFIER
        defaultRackShouldNotBeFound("locationIdentifier.in=" + UPDATED_LOCATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRacksByLocationIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where locationIdentifier is not null
        defaultRackShouldBeFound("locationIdentifier.specified=true");

        // Get all the rackList where locationIdentifier is null
        defaultRackShouldNotBeFound("locationIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllRacksByLocationIdentifierContainsSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where locationIdentifier contains DEFAULT_LOCATION_IDENTIFIER
        defaultRackShouldBeFound("locationIdentifier.contains=" + DEFAULT_LOCATION_IDENTIFIER);

        // Get all the rackList where locationIdentifier contains UPDATED_LOCATION_IDENTIFIER
        defaultRackShouldNotBeFound("locationIdentifier.contains=" + UPDATED_LOCATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRacksByLocationIdentifierNotContainsSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where locationIdentifier does not contain DEFAULT_LOCATION_IDENTIFIER
        defaultRackShouldNotBeFound("locationIdentifier.doesNotContain=" + DEFAULT_LOCATION_IDENTIFIER);

        // Get all the rackList where locationIdentifier does not contain UPDATED_LOCATION_IDENTIFIER
        defaultRackShouldBeFound("locationIdentifier.doesNotContain=" + UPDATED_LOCATION_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultRackShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the rackList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultRackShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate not equals to DEFAULT_MODIFIED_DATE
        defaultRackShouldNotBeFound("modifiedDate.notEquals=" + DEFAULT_MODIFIED_DATE);

        // Get all the rackList where modifiedDate not equals to UPDATED_MODIFIED_DATE
        defaultRackShouldBeFound("modifiedDate.notEquals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultRackShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the rackList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultRackShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate is not null
        defaultRackShouldBeFound("modifiedDate.specified=true");

        // Get all the rackList where modifiedDate is null
        defaultRackShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate is greater than or equal to DEFAULT_MODIFIED_DATE
        defaultRackShouldBeFound("modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the rackList where modifiedDate is greater than or equal to UPDATED_MODIFIED_DATE
        defaultRackShouldNotBeFound("modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate is less than or equal to DEFAULT_MODIFIED_DATE
        defaultRackShouldBeFound("modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the rackList where modifiedDate is less than or equal to SMALLER_MODIFIED_DATE
        defaultRackShouldNotBeFound("modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate is less than DEFAULT_MODIFIED_DATE
        defaultRackShouldNotBeFound("modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the rackList where modifiedDate is less than UPDATED_MODIFIED_DATE
        defaultRackShouldBeFound("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRacksByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where modifiedDate is greater than DEFAULT_MODIFIED_DATE
        defaultRackShouldNotBeFound("modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the rackList where modifiedDate is greater than SMALLER_MODIFIED_DATE
        defaultRackShouldBeFound("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllRacksByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where isActive equals to DEFAULT_IS_ACTIVE
        defaultRackShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the rackList where isActive equals to UPDATED_IS_ACTIVE
        defaultRackShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRacksByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultRackShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the rackList where isActive not equals to UPDATED_IS_ACTIVE
        defaultRackShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRacksByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultRackShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the rackList where isActive equals to UPDATED_IS_ACTIVE
        defaultRackShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllRacksByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        // Get all the rackList where isActive is not null
        defaultRackShouldBeFound("isActive.specified=true");

        // Get all the rackList where isActive is null
        defaultRackShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllRacksByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        rack.setUser(user);
        rackRepository.saveAndFlush(rack);
        Long userId = user.getId();

        // Get all the rackList where user equals to userId
        defaultRackShouldBeFound("userId.equals=" + userId);

        // Get all the rackList where user equals to (userId + 1)
        defaultRackShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllRacksByBookItemIsEqualToSomething() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);
        BookItem bookItem = BookItemResourceIT.createEntity(em);
        em.persist(bookItem);
        em.flush();
        rack.addBookItem(bookItem);
        rackRepository.saveAndFlush(rack);
        Long bookItemId = bookItem.getId();

        // Get all the rackList where bookItem equals to bookItemId
        defaultRackShouldBeFound("bookItemId.equals=" + bookItemId);

        // Get all the rackList where bookItem equals to (bookItemId + 1)
        defaultRackShouldNotBeFound("bookItemId.equals=" + (bookItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRackShouldBeFound(String filter) throws Exception {
        restRackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rack.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].locationIdentifier").value(hasItem(DEFAULT_LOCATION_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restRackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRackShouldNotBeFound(String filter) throws Exception {
        restRackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRack() throws Exception {
        // Get the rack
        restRackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRack() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeUpdate = rackRepository.findAll().size();

        // Update the rack
        Rack updatedRack = rackRepository.findById(rack.getId()).get();
        // Disconnect from session so that the updates on updatedRack are not directly saved in db
        em.detach(updatedRack);
        updatedRack
            .number(UPDATED_NUMBER)
            .locationIdentifier(UPDATED_LOCATION_IDENTIFIER)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restRackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRack))
            )
            .andExpect(status().isOk());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
        Rack testRack = rackList.get(rackList.size() - 1);
        assertThat(testRack.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testRack.getLocationIdentifier()).isEqualTo(UPDATED_LOCATION_IDENTIFIER);
        assertThat(testRack.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testRack.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rack.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRackWithPatch() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeUpdate = rackRepository.findAll().size();

        // Update the rack using partial update
        Rack partialUpdatedRack = new Rack();
        partialUpdatedRack.setId(rack.getId());

        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRack))
            )
            .andExpect(status().isOk());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
        Rack testRack = rackList.get(rackList.size() - 1);
        assertThat(testRack.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testRack.getLocationIdentifier()).isEqualTo(DEFAULT_LOCATION_IDENTIFIER);
        assertThat(testRack.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testRack.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateRackWithPatch() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeUpdate = rackRepository.findAll().size();

        // Update the rack using partial update
        Rack partialUpdatedRack = new Rack();
        partialUpdatedRack.setId(rack.getId());

        partialUpdatedRack
            .number(UPDATED_NUMBER)
            .locationIdentifier(UPDATED_LOCATION_IDENTIFIER)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRack))
            )
            .andExpect(status().isOk());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
        Rack testRack = rackList.get(rackList.size() - 1);
        assertThat(testRack.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testRack.getLocationIdentifier()).isEqualTo(UPDATED_LOCATION_IDENTIFIER);
        assertThat(testRack.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testRack.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rack))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRack() throws Exception {
        int databaseSizeBeforeUpdate = rackRepository.findAll().size();
        rack.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rack)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rack in the database
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRack() throws Exception {
        // Initialize the database
        rackRepository.saveAndFlush(rack);

        int databaseSizeBeforeDelete = rackRepository.findAll().size();

        // Delete the rack
        restRackMockMvc
            .perform(delete(ENTITY_API_URL_ID, rack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rack> rackList = rackRepository.findAll();
        assertThat(rackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
