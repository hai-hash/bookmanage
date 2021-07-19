package com.vnpt.bookmanage.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.BookReservation;
import com.vnpt.bookmanage.domain.Reader;
import com.vnpt.bookmanage.domain.User;
import com.vnpt.bookmanage.domain.enumeration.AccountStatus;
import com.vnpt.bookmanage.repository.ReaderRepository;
import com.vnpt.bookmanage.service.criteria.ReaderCriteria;
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
 * Integration tests for the {@link ReaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReaderResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final AccountStatus DEFAULT_STATUS = AccountStatus.ACTIVE;
    private static final AccountStatus UPDATED_STATUS = AccountStatus.CLOSED;

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/readers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReaderMockMvc;

    private Reader reader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reader createEntity(EntityManager em) {
        Reader reader = new Reader()
            .phone(DEFAULT_PHONE)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .status(DEFAULT_STATUS)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return reader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reader createUpdatedEntity(EntityManager em) {
        Reader reader = new Reader()
            .phone(UPDATED_PHONE)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .status(UPDATED_STATUS)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return reader;
    }

    @BeforeEach
    public void initTest() {
        reader = createEntity(em);
    }

    @Test
    @Transactional
    void createReader() throws Exception {
        int databaseSizeBeforeCreate = readerRepository.findAll().size();
        // Create the Reader
        restReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reader)))
            .andExpect(status().isCreated());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeCreate + 1);
        Reader testReader = readerList.get(readerList.size() - 1);
        assertThat(testReader.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testReader.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testReader.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testReader.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testReader.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testReader.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testReader.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReader.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createReaderWithExistingId() throws Exception {
        // Create the Reader with an existing ID
        reader.setId(1L);

        int databaseSizeBeforeCreate = readerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reader)))
            .andExpect(status().isBadRequest());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = readerRepository.findAll().size();
        // set the field null
        reader.setCountry(null);

        // Create the Reader, which fails.

        restReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reader)))
            .andExpect(status().isBadRequest());

        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = readerRepository.findAll().size();
        // set the field null
        reader.setModifiedDate(null);

        // Create the Reader, which fails.

        restReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reader)))
            .andExpect(status().isBadRequest());

        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReaders() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList
        restReaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reader.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getReader() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get the reader
        restReaderMockMvc
            .perform(get(ENTITY_API_URL_ID, reader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reader.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getReadersByIdFiltering() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        Long id = reader.getId();

        defaultReaderShouldBeFound("id.equals=" + id);
        defaultReaderShouldNotBeFound("id.notEquals=" + id);

        defaultReaderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReaderShouldNotBeFound("id.greaterThan=" + id);

        defaultReaderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReaderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReadersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where phone equals to DEFAULT_PHONE
        defaultReaderShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the readerList where phone equals to UPDATED_PHONE
        defaultReaderShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllReadersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where phone not equals to DEFAULT_PHONE
        defaultReaderShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the readerList where phone not equals to UPDATED_PHONE
        defaultReaderShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllReadersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultReaderShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the readerList where phone equals to UPDATED_PHONE
        defaultReaderShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllReadersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where phone is not null
        defaultReaderShouldBeFound("phone.specified=true");

        // Get all the readerList where phone is null
        defaultReaderShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where phone contains DEFAULT_PHONE
        defaultReaderShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the readerList where phone contains UPDATED_PHONE
        defaultReaderShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllReadersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where phone does not contain DEFAULT_PHONE
        defaultReaderShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the readerList where phone does not contain UPDATED_PHONE
        defaultReaderShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllReadersByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultReaderShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the readerList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultReaderShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllReadersByStreetAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where streetAddress not equals to DEFAULT_STREET_ADDRESS
        defaultReaderShouldNotBeFound("streetAddress.notEquals=" + DEFAULT_STREET_ADDRESS);

        // Get all the readerList where streetAddress not equals to UPDATED_STREET_ADDRESS
        defaultReaderShouldBeFound("streetAddress.notEquals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllReadersByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultReaderShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the readerList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultReaderShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllReadersByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where streetAddress is not null
        defaultReaderShouldBeFound("streetAddress.specified=true");

        // Get all the readerList where streetAddress is null
        defaultReaderShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultReaderShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the readerList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultReaderShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllReadersByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultReaderShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the readerList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultReaderShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllReadersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where city equals to DEFAULT_CITY
        defaultReaderShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the readerList where city equals to UPDATED_CITY
        defaultReaderShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllReadersByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where city not equals to DEFAULT_CITY
        defaultReaderShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the readerList where city not equals to UPDATED_CITY
        defaultReaderShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllReadersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultReaderShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the readerList where city equals to UPDATED_CITY
        defaultReaderShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllReadersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where city is not null
        defaultReaderShouldBeFound("city.specified=true");

        // Get all the readerList where city is null
        defaultReaderShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByCityContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where city contains DEFAULT_CITY
        defaultReaderShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the readerList where city contains UPDATED_CITY
        defaultReaderShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllReadersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where city does not contain DEFAULT_CITY
        defaultReaderShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the readerList where city does not contain UPDATED_CITY
        defaultReaderShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllReadersByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where state equals to DEFAULT_STATE
        defaultReaderShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the readerList where state equals to UPDATED_STATE
        defaultReaderShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllReadersByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where state not equals to DEFAULT_STATE
        defaultReaderShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the readerList where state not equals to UPDATED_STATE
        defaultReaderShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllReadersByStateIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where state in DEFAULT_STATE or UPDATED_STATE
        defaultReaderShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the readerList where state equals to UPDATED_STATE
        defaultReaderShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllReadersByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where state is not null
        defaultReaderShouldBeFound("state.specified=true");

        // Get all the readerList where state is null
        defaultReaderShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByStateContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where state contains DEFAULT_STATE
        defaultReaderShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the readerList where state contains UPDATED_STATE
        defaultReaderShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllReadersByStateNotContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where state does not contain DEFAULT_STATE
        defaultReaderShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the readerList where state does not contain UPDATED_STATE
        defaultReaderShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllReadersByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where zipCode equals to DEFAULT_ZIP_CODE
        defaultReaderShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the readerList where zipCode equals to UPDATED_ZIP_CODE
        defaultReaderShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllReadersByZipCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where zipCode not equals to DEFAULT_ZIP_CODE
        defaultReaderShouldNotBeFound("zipCode.notEquals=" + DEFAULT_ZIP_CODE);

        // Get all the readerList where zipCode not equals to UPDATED_ZIP_CODE
        defaultReaderShouldBeFound("zipCode.notEquals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllReadersByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultReaderShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the readerList where zipCode equals to UPDATED_ZIP_CODE
        defaultReaderShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllReadersByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where zipCode is not null
        defaultReaderShouldBeFound("zipCode.specified=true");

        // Get all the readerList where zipCode is null
        defaultReaderShouldNotBeFound("zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where zipCode contains DEFAULT_ZIP_CODE
        defaultReaderShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the readerList where zipCode contains UPDATED_ZIP_CODE
        defaultReaderShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllReadersByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultReaderShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the readerList where zipCode does not contain UPDATED_ZIP_CODE
        defaultReaderShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllReadersByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where country equals to DEFAULT_COUNTRY
        defaultReaderShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the readerList where country equals to UPDATED_COUNTRY
        defaultReaderShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllReadersByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where country not equals to DEFAULT_COUNTRY
        defaultReaderShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the readerList where country not equals to UPDATED_COUNTRY
        defaultReaderShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllReadersByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultReaderShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the readerList where country equals to UPDATED_COUNTRY
        defaultReaderShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllReadersByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where country is not null
        defaultReaderShouldBeFound("country.specified=true");

        // Get all the readerList where country is null
        defaultReaderShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByCountryContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where country contains DEFAULT_COUNTRY
        defaultReaderShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the readerList where country contains UPDATED_COUNTRY
        defaultReaderShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllReadersByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where country does not contain DEFAULT_COUNTRY
        defaultReaderShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the readerList where country does not contain UPDATED_COUNTRY
        defaultReaderShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllReadersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where status equals to DEFAULT_STATUS
        defaultReaderShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the readerList where status equals to UPDATED_STATUS
        defaultReaderShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReadersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where status not equals to DEFAULT_STATUS
        defaultReaderShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the readerList where status not equals to UPDATED_STATUS
        defaultReaderShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReadersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultReaderShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the readerList where status equals to UPDATED_STATUS
        defaultReaderShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReadersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where status is not null
        defaultReaderShouldBeFound("status.specified=true");

        // Get all the readerList where status is null
        defaultReaderShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultReaderShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the readerList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultReaderShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate not equals to DEFAULT_MODIFIED_DATE
        defaultReaderShouldNotBeFound("modifiedDate.notEquals=" + DEFAULT_MODIFIED_DATE);

        // Get all the readerList where modifiedDate not equals to UPDATED_MODIFIED_DATE
        defaultReaderShouldBeFound("modifiedDate.notEquals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultReaderShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the readerList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultReaderShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate is not null
        defaultReaderShouldBeFound("modifiedDate.specified=true");

        // Get all the readerList where modifiedDate is null
        defaultReaderShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate is greater than or equal to DEFAULT_MODIFIED_DATE
        defaultReaderShouldBeFound("modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the readerList where modifiedDate is greater than or equal to UPDATED_MODIFIED_DATE
        defaultReaderShouldNotBeFound("modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate is less than or equal to DEFAULT_MODIFIED_DATE
        defaultReaderShouldBeFound("modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the readerList where modifiedDate is less than or equal to SMALLER_MODIFIED_DATE
        defaultReaderShouldNotBeFound("modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate is less than DEFAULT_MODIFIED_DATE
        defaultReaderShouldNotBeFound("modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the readerList where modifiedDate is less than UPDATED_MODIFIED_DATE
        defaultReaderShouldBeFound("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReadersByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        // Get all the readerList where modifiedDate is greater than DEFAULT_MODIFIED_DATE
        defaultReaderShouldNotBeFound("modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the readerList where modifiedDate is greater than SMALLER_MODIFIED_DATE
        defaultReaderShouldBeFound("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllReadersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        reader.setUser(user);
        readerRepository.saveAndFlush(reader);
        Long userId = user.getId();

        // Get all the readerList where user equals to userId
        defaultReaderShouldBeFound("userId.equals=" + userId);

        // Get all the readerList where user equals to (userId + 1)
        defaultReaderShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllReadersByBookReservationIsEqualToSomething() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);
        BookReservation bookReservation = BookReservationResourceIT.createEntity(em);
        em.persist(bookReservation);
        em.flush();
        reader.addBookReservation(bookReservation);
        readerRepository.saveAndFlush(reader);
        Long bookReservationId = bookReservation.getId();

        // Get all the readerList where bookReservation equals to bookReservationId
        defaultReaderShouldBeFound("bookReservationId.equals=" + bookReservationId);

        // Get all the readerList where bookReservation equals to (bookReservationId + 1)
        defaultReaderShouldNotBeFound("bookReservationId.equals=" + (bookReservationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReaderShouldBeFound(String filter) throws Exception {
        restReaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reader.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restReaderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReaderShouldNotBeFound(String filter) throws Exception {
        restReaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReaderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReader() throws Exception {
        // Get the reader
        restReaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReader() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        int databaseSizeBeforeUpdate = readerRepository.findAll().size();

        // Update the reader
        Reader updatedReader = readerRepository.findById(reader.getId()).get();
        // Disconnect from session so that the updates on updatedReader are not directly saved in db
        em.detach(updatedReader);
        updatedReader
            .phone(UPDATED_PHONE)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .status(UPDATED_STATUS)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restReaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReader))
            )
            .andExpect(status().isOk());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
        Reader testReader = readerList.get(readerList.size() - 1);
        assertThat(testReader.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testReader.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testReader.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testReader.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testReader.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testReader.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testReader.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReader.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingReader() throws Exception {
        int databaseSizeBeforeUpdate = readerRepository.findAll().size();
        reader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReader() throws Exception {
        int databaseSizeBeforeUpdate = readerRepository.findAll().size();
        reader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReader() throws Exception {
        int databaseSizeBeforeUpdate = readerRepository.findAll().size();
        reader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReaderWithPatch() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        int databaseSizeBeforeUpdate = readerRepository.findAll().size();

        // Update the reader using partial update
        Reader partialUpdatedReader = new Reader();
        partialUpdatedReader.setId(reader.getId());

        partialUpdatedReader
            .streetAddress(UPDATED_STREET_ADDRESS)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .status(UPDATED_STATUS)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReader))
            )
            .andExpect(status().isOk());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
        Reader testReader = readerList.get(readerList.size() - 1);
        assertThat(testReader.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testReader.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testReader.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testReader.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testReader.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testReader.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testReader.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReader.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateReaderWithPatch() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        int databaseSizeBeforeUpdate = readerRepository.findAll().size();

        // Update the reader using partial update
        Reader partialUpdatedReader = new Reader();
        partialUpdatedReader.setId(reader.getId());

        partialUpdatedReader
            .phone(UPDATED_PHONE)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .status(UPDATED_STATUS)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReader))
            )
            .andExpect(status().isOk());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
        Reader testReader = readerList.get(readerList.size() - 1);
        assertThat(testReader.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testReader.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testReader.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testReader.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testReader.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testReader.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testReader.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReader.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingReader() throws Exception {
        int databaseSizeBeforeUpdate = readerRepository.findAll().size();
        reader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReader() throws Exception {
        int databaseSizeBeforeUpdate = readerRepository.findAll().size();
        reader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReader() throws Exception {
        int databaseSizeBeforeUpdate = readerRepository.findAll().size();
        reader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReaderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reader in the database
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReader() throws Exception {
        // Initialize the database
        readerRepository.saveAndFlush(reader);

        int databaseSizeBeforeDelete = readerRepository.findAll().size();

        // Delete the reader
        restReaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, reader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reader> readerList = readerRepository.findAll();
        assertThat(readerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
