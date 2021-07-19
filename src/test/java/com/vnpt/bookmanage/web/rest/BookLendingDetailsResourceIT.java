package com.vnpt.bookmanage.web.rest;

import static com.vnpt.bookmanage.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.BookLending;
import com.vnpt.bookmanage.domain.BookLendingDetails;
import com.vnpt.bookmanage.domain.BookReservation;
import com.vnpt.bookmanage.repository.BookLendingDetailsRepository;
import com.vnpt.bookmanage.service.criteria.BookLendingDetailsCriteria;
import java.math.BigDecimal;
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
 * Integration tests for the {@link BookLendingDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookLendingDetailsResourceIT {

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RETURN_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/book-lending-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookLendingDetailsRepository bookLendingDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookLendingDetailsMockMvc;

    private BookLendingDetails bookLendingDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookLendingDetails createEntity(EntityManager em) {
        BookLendingDetails bookLendingDetails = new BookLendingDetails()
            .dueDate(DEFAULT_DUE_DATE)
            .returnDate(DEFAULT_RETURN_DATE)
            .price(DEFAULT_PRICE);
        return bookLendingDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookLendingDetails createUpdatedEntity(EntityManager em) {
        BookLendingDetails bookLendingDetails = new BookLendingDetails()
            .dueDate(UPDATED_DUE_DATE)
            .returnDate(UPDATED_RETURN_DATE)
            .price(UPDATED_PRICE);
        return bookLendingDetails;
    }

    @BeforeEach
    public void initTest() {
        bookLendingDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createBookLendingDetails() throws Exception {
        int databaseSizeBeforeCreate = bookLendingDetailsRepository.findAll().size();
        // Create the BookLendingDetails
        restBookLendingDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isCreated());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BookLendingDetails testBookLendingDetails = bookLendingDetailsList.get(bookLendingDetailsList.size() - 1);
        assertThat(testBookLendingDetails.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testBookLendingDetails.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testBookLendingDetails.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createBookLendingDetailsWithExistingId() throws Exception {
        // Create the BookLendingDetails with an existing ID
        bookLendingDetails.setId(1L);

        int databaseSizeBeforeCreate = bookLendingDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookLendingDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookLendingDetailsRepository.findAll().size();
        // set the field null
        bookLendingDetails.setDueDate(null);

        // Create the BookLendingDetails, which fails.

        restBookLendingDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isBadRequest());

        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookLendingDetails() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList
        restBookLendingDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookLendingDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getBookLendingDetails() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get the bookLendingDetails
        restBookLendingDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, bookLendingDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookLendingDetails.getId().intValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getBookLendingDetailsByIdFiltering() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        Long id = bookLendingDetails.getId();

        defaultBookLendingDetailsShouldBeFound("id.equals=" + id);
        defaultBookLendingDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultBookLendingDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBookLendingDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultBookLendingDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBookLendingDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate equals to DEFAULT_DUE_DATE
        defaultBookLendingDetailsShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the bookLendingDetailsList where dueDate equals to UPDATED_DUE_DATE
        defaultBookLendingDetailsShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate not equals to DEFAULT_DUE_DATE
        defaultBookLendingDetailsShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the bookLendingDetailsList where dueDate not equals to UPDATED_DUE_DATE
        defaultBookLendingDetailsShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultBookLendingDetailsShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the bookLendingDetailsList where dueDate equals to UPDATED_DUE_DATE
        defaultBookLendingDetailsShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate is not null
        defaultBookLendingDetailsShouldBeFound("dueDate.specified=true");

        // Get all the bookLendingDetailsList where dueDate is null
        defaultBookLendingDetailsShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultBookLendingDetailsShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the bookLendingDetailsList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultBookLendingDetailsShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultBookLendingDetailsShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the bookLendingDetailsList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultBookLendingDetailsShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate is less than DEFAULT_DUE_DATE
        defaultBookLendingDetailsShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the bookLendingDetailsList where dueDate is less than UPDATED_DUE_DATE
        defaultBookLendingDetailsShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where dueDate is greater than DEFAULT_DUE_DATE
        defaultBookLendingDetailsShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the bookLendingDetailsList where dueDate is greater than SMALLER_DUE_DATE
        defaultBookLendingDetailsShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate equals to DEFAULT_RETURN_DATE
        defaultBookLendingDetailsShouldBeFound("returnDate.equals=" + DEFAULT_RETURN_DATE);

        // Get all the bookLendingDetailsList where returnDate equals to UPDATED_RETURN_DATE
        defaultBookLendingDetailsShouldNotBeFound("returnDate.equals=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate not equals to DEFAULT_RETURN_DATE
        defaultBookLendingDetailsShouldNotBeFound("returnDate.notEquals=" + DEFAULT_RETURN_DATE);

        // Get all the bookLendingDetailsList where returnDate not equals to UPDATED_RETURN_DATE
        defaultBookLendingDetailsShouldBeFound("returnDate.notEquals=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate in DEFAULT_RETURN_DATE or UPDATED_RETURN_DATE
        defaultBookLendingDetailsShouldBeFound("returnDate.in=" + DEFAULT_RETURN_DATE + "," + UPDATED_RETURN_DATE);

        // Get all the bookLendingDetailsList where returnDate equals to UPDATED_RETURN_DATE
        defaultBookLendingDetailsShouldNotBeFound("returnDate.in=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate is not null
        defaultBookLendingDetailsShouldBeFound("returnDate.specified=true");

        // Get all the bookLendingDetailsList where returnDate is null
        defaultBookLendingDetailsShouldNotBeFound("returnDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate is greater than or equal to DEFAULT_RETURN_DATE
        defaultBookLendingDetailsShouldBeFound("returnDate.greaterThanOrEqual=" + DEFAULT_RETURN_DATE);

        // Get all the bookLendingDetailsList where returnDate is greater than or equal to UPDATED_RETURN_DATE
        defaultBookLendingDetailsShouldNotBeFound("returnDate.greaterThanOrEqual=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate is less than or equal to DEFAULT_RETURN_DATE
        defaultBookLendingDetailsShouldBeFound("returnDate.lessThanOrEqual=" + DEFAULT_RETURN_DATE);

        // Get all the bookLendingDetailsList where returnDate is less than or equal to SMALLER_RETURN_DATE
        defaultBookLendingDetailsShouldNotBeFound("returnDate.lessThanOrEqual=" + SMALLER_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate is less than DEFAULT_RETURN_DATE
        defaultBookLendingDetailsShouldNotBeFound("returnDate.lessThan=" + DEFAULT_RETURN_DATE);

        // Get all the bookLendingDetailsList where returnDate is less than UPDATED_RETURN_DATE
        defaultBookLendingDetailsShouldBeFound("returnDate.lessThan=" + UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByReturnDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where returnDate is greater than DEFAULT_RETURN_DATE
        defaultBookLendingDetailsShouldNotBeFound("returnDate.greaterThan=" + DEFAULT_RETURN_DATE);

        // Get all the bookLendingDetailsList where returnDate is greater than SMALLER_RETURN_DATE
        defaultBookLendingDetailsShouldBeFound("returnDate.greaterThan=" + SMALLER_RETURN_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price equals to DEFAULT_PRICE
        defaultBookLendingDetailsShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the bookLendingDetailsList where price equals to UPDATED_PRICE
        defaultBookLendingDetailsShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price not equals to DEFAULT_PRICE
        defaultBookLendingDetailsShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the bookLendingDetailsList where price not equals to UPDATED_PRICE
        defaultBookLendingDetailsShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultBookLendingDetailsShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the bookLendingDetailsList where price equals to UPDATED_PRICE
        defaultBookLendingDetailsShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price is not null
        defaultBookLendingDetailsShouldBeFound("price.specified=true");

        // Get all the bookLendingDetailsList where price is null
        defaultBookLendingDetailsShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price is greater than or equal to DEFAULT_PRICE
        defaultBookLendingDetailsShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the bookLendingDetailsList where price is greater than or equal to UPDATED_PRICE
        defaultBookLendingDetailsShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price is less than or equal to DEFAULT_PRICE
        defaultBookLendingDetailsShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the bookLendingDetailsList where price is less than or equal to SMALLER_PRICE
        defaultBookLendingDetailsShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price is less than DEFAULT_PRICE
        defaultBookLendingDetailsShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the bookLendingDetailsList where price is less than UPDATED_PRICE
        defaultBookLendingDetailsShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        // Get all the bookLendingDetailsList where price is greater than DEFAULT_PRICE
        defaultBookLendingDetailsShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the bookLendingDetailsList where price is greater than SMALLER_PRICE
        defaultBookLendingDetailsShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByBookReservationIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);
        BookReservation bookReservation = BookReservationResourceIT.createEntity(em);
        em.persist(bookReservation);
        em.flush();
        bookLendingDetails.setBookReservation(bookReservation);
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);
        Long bookReservationId = bookReservation.getId();

        // Get all the bookLendingDetailsList where bookReservation equals to bookReservationId
        defaultBookLendingDetailsShouldBeFound("bookReservationId.equals=" + bookReservationId);

        // Get all the bookLendingDetailsList where bookReservation equals to (bookReservationId + 1)
        defaultBookLendingDetailsShouldNotBeFound("bookReservationId.equals=" + (bookReservationId + 1));
    }

    @Test
    @Transactional
    void getAllBookLendingDetailsByBookLendingIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);
        BookLending bookLending = BookLendingResourceIT.createEntity(em);
        em.persist(bookLending);
        em.flush();
        bookLendingDetails.setBookLending(bookLending);
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);
        Long bookLendingId = bookLending.getId();

        // Get all the bookLendingDetailsList where bookLending equals to bookLendingId
        defaultBookLendingDetailsShouldBeFound("bookLendingId.equals=" + bookLendingId);

        // Get all the bookLendingDetailsList where bookLending equals to (bookLendingId + 1)
        defaultBookLendingDetailsShouldNotBeFound("bookLendingId.equals=" + (bookLendingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookLendingDetailsShouldBeFound(String filter) throws Exception {
        restBookLendingDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookLendingDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));

        // Check, that the count call also returns 1
        restBookLendingDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookLendingDetailsShouldNotBeFound(String filter) throws Exception {
        restBookLendingDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookLendingDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBookLendingDetails() throws Exception {
        // Get the bookLendingDetails
        restBookLendingDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBookLendingDetails() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();

        // Update the bookLendingDetails
        BookLendingDetails updatedBookLendingDetails = bookLendingDetailsRepository.findById(bookLendingDetails.getId()).get();
        // Disconnect from session so that the updates on updatedBookLendingDetails are not directly saved in db
        em.detach(updatedBookLendingDetails);
        updatedBookLendingDetails.dueDate(UPDATED_DUE_DATE).returnDate(UPDATED_RETURN_DATE).price(UPDATED_PRICE);

        restBookLendingDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookLendingDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookLendingDetails))
            )
            .andExpect(status().isOk());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookLendingDetails testBookLendingDetails = bookLendingDetailsList.get(bookLendingDetailsList.size() - 1);
        assertThat(testBookLendingDetails.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testBookLendingDetails.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBookLendingDetails.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingBookLendingDetails() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();
        bookLendingDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookLendingDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookLendingDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookLendingDetails() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();
        bookLendingDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookLendingDetails() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();
        bookLendingDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookLendingDetailsWithPatch() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();

        // Update the bookLendingDetails using partial update
        BookLendingDetails partialUpdatedBookLendingDetails = new BookLendingDetails();
        partialUpdatedBookLendingDetails.setId(bookLendingDetails.getId());

        partialUpdatedBookLendingDetails.returnDate(UPDATED_RETURN_DATE).price(UPDATED_PRICE);

        restBookLendingDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookLendingDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookLendingDetails))
            )
            .andExpect(status().isOk());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookLendingDetails testBookLendingDetails = bookLendingDetailsList.get(bookLendingDetailsList.size() - 1);
        assertThat(testBookLendingDetails.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testBookLendingDetails.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBookLendingDetails.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateBookLendingDetailsWithPatch() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();

        // Update the bookLendingDetails using partial update
        BookLendingDetails partialUpdatedBookLendingDetails = new BookLendingDetails();
        partialUpdatedBookLendingDetails.setId(bookLendingDetails.getId());

        partialUpdatedBookLendingDetails.dueDate(UPDATED_DUE_DATE).returnDate(UPDATED_RETURN_DATE).price(UPDATED_PRICE);

        restBookLendingDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookLendingDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookLendingDetails))
            )
            .andExpect(status().isOk());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
        BookLendingDetails testBookLendingDetails = bookLendingDetailsList.get(bookLendingDetailsList.size() - 1);
        assertThat(testBookLendingDetails.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testBookLendingDetails.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBookLendingDetails.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingBookLendingDetails() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();
        bookLendingDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookLendingDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookLendingDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookLendingDetails() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();
        bookLendingDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookLendingDetails() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingDetailsRepository.findAll().size();
        bookLendingDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookLendingDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookLendingDetails in the database
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookLendingDetails() throws Exception {
        // Initialize the database
        bookLendingDetailsRepository.saveAndFlush(bookLendingDetails);

        int databaseSizeBeforeDelete = bookLendingDetailsRepository.findAll().size();

        // Delete the bookLendingDetails
        restBookLendingDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookLendingDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookLendingDetails> bookLendingDetailsList = bookLendingDetailsRepository.findAll();
        assertThat(bookLendingDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
