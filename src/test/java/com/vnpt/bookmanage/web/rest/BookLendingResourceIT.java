package com.vnpt.bookmanage.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.BookLending;
import com.vnpt.bookmanage.domain.BookLendingDetails;
import com.vnpt.bookmanage.domain.User;
import com.vnpt.bookmanage.domain.enumeration.LendingStatus;
import com.vnpt.bookmanage.repository.BookLendingRepository;
import com.vnpt.bookmanage.service.criteria.BookLendingCriteria;
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
 * Integration tests for the {@link BookLendingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookLendingResourceIT {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LendingStatus DEFAULT_STATUS = LendingStatus.PENDING;
    private static final LendingStatus UPDATED_STATUS = LendingStatus.DONE;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/book-lendings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookLendingRepository bookLendingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookLendingMockMvc;

    private BookLending bookLending;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookLending createEntity(EntityManager em) {
        BookLending bookLending = new BookLending()
            .creationDate(DEFAULT_CREATION_DATE)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION);
        return bookLending;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookLending createUpdatedEntity(EntityManager em) {
        BookLending bookLending = new BookLending()
            .creationDate(UPDATED_CREATION_DATE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);
        return bookLending;
    }

    @BeforeEach
    public void initTest() {
        bookLending = createEntity(em);
    }

    @Test
    @Transactional
    void createBookLending() throws Exception {
        int databaseSizeBeforeCreate = bookLendingRepository.findAll().size();
        // Create the BookLending
        restBookLendingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLending)))
            .andExpect(status().isCreated());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeCreate + 1);
        BookLending testBookLending = bookLendingList.get(bookLendingList.size() - 1);
        assertThat(testBookLending.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testBookLending.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBookLending.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBookLendingWithExistingId() throws Exception {
        // Create the BookLending with an existing ID
        bookLending.setId(1L);

        int databaseSizeBeforeCreate = bookLendingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookLendingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLending)))
            .andExpect(status().isBadRequest());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookLendingRepository.findAll().size();
        // set the field null
        bookLending.setCreationDate(null);

        // Create the BookLending, which fails.

        restBookLendingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLending)))
            .andExpect(status().isBadRequest());

        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookLendings() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList
        restBookLendingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookLending.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBookLending() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get the bookLending
        restBookLendingMockMvc
            .perform(get(ENTITY_API_URL_ID, bookLending.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookLending.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getBookLendingsByIdFiltering() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        Long id = bookLending.getId();

        defaultBookLendingShouldBeFound("id.equals=" + id);
        defaultBookLendingShouldNotBeFound("id.notEquals=" + id);

        defaultBookLendingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBookLendingShouldNotBeFound("id.greaterThan=" + id);

        defaultBookLendingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBookLendingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate equals to DEFAULT_CREATION_DATE
        defaultBookLendingShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the bookLendingList where creationDate equals to UPDATED_CREATION_DATE
        defaultBookLendingShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultBookLendingShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the bookLendingList where creationDate not equals to UPDATED_CREATION_DATE
        defaultBookLendingShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultBookLendingShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the bookLendingList where creationDate equals to UPDATED_CREATION_DATE
        defaultBookLendingShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate is not null
        defaultBookLendingShouldBeFound("creationDate.specified=true");

        // Get all the bookLendingList where creationDate is null
        defaultBookLendingShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate is greater than or equal to DEFAULT_CREATION_DATE
        defaultBookLendingShouldBeFound("creationDate.greaterThanOrEqual=" + DEFAULT_CREATION_DATE);

        // Get all the bookLendingList where creationDate is greater than or equal to UPDATED_CREATION_DATE
        defaultBookLendingShouldNotBeFound("creationDate.greaterThanOrEqual=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate is less than or equal to DEFAULT_CREATION_DATE
        defaultBookLendingShouldBeFound("creationDate.lessThanOrEqual=" + DEFAULT_CREATION_DATE);

        // Get all the bookLendingList where creationDate is less than or equal to SMALLER_CREATION_DATE
        defaultBookLendingShouldNotBeFound("creationDate.lessThanOrEqual=" + SMALLER_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate is less than DEFAULT_CREATION_DATE
        defaultBookLendingShouldNotBeFound("creationDate.lessThan=" + DEFAULT_CREATION_DATE);

        // Get all the bookLendingList where creationDate is less than UPDATED_CREATION_DATE
        defaultBookLendingShouldBeFound("creationDate.lessThan=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingsByCreationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where creationDate is greater than DEFAULT_CREATION_DATE
        defaultBookLendingShouldNotBeFound("creationDate.greaterThan=" + DEFAULT_CREATION_DATE);

        // Get all the bookLendingList where creationDate is greater than SMALLER_CREATION_DATE
        defaultBookLendingShouldBeFound("creationDate.greaterThan=" + SMALLER_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookLendingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where status equals to DEFAULT_STATUS
        defaultBookLendingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bookLendingList where status equals to UPDATED_STATUS
        defaultBookLendingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookLendingsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where status not equals to DEFAULT_STATUS
        defaultBookLendingShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the bookLendingList where status not equals to UPDATED_STATUS
        defaultBookLendingShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookLendingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBookLendingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bookLendingList where status equals to UPDATED_STATUS
        defaultBookLendingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookLendingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where status is not null
        defaultBookLendingShouldBeFound("status.specified=true");

        // Get all the bookLendingList where status is null
        defaultBookLendingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBookLendingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where description equals to DEFAULT_DESCRIPTION
        defaultBookLendingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the bookLendingList where description equals to UPDATED_DESCRIPTION
        defaultBookLendingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBookLendingsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where description not equals to DEFAULT_DESCRIPTION
        defaultBookLendingShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the bookLendingList where description not equals to UPDATED_DESCRIPTION
        defaultBookLendingShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBookLendingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBookLendingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the bookLendingList where description equals to UPDATED_DESCRIPTION
        defaultBookLendingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBookLendingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where description is not null
        defaultBookLendingShouldBeFound("description.specified=true");

        // Get all the bookLendingList where description is null
        defaultBookLendingShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBookLendingsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where description contains DEFAULT_DESCRIPTION
        defaultBookLendingShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the bookLendingList where description contains UPDATED_DESCRIPTION
        defaultBookLendingShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBookLendingsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        // Get all the bookLendingList where description does not contain DEFAULT_DESCRIPTION
        defaultBookLendingShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the bookLendingList where description does not contain UPDATED_DESCRIPTION
        defaultBookLendingShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBookLendingsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        bookLending.setUser(user);
        bookLendingRepository.saveAndFlush(bookLending);
        Long userId = user.getId();

        // Get all the bookLendingList where user equals to userId
        defaultBookLendingShouldBeFound("userId.equals=" + userId);

        // Get all the bookLendingList where user equals to (userId + 1)
        defaultBookLendingShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllBookLendingsByBookLendingDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);
        BookLendingDetails bookLendingDetails = BookLendingDetailsResourceIT.createEntity(em);
        em.persist(bookLendingDetails);
        em.flush();
        bookLending.addBookLendingDetails(bookLendingDetails);
        bookLendingRepository.saveAndFlush(bookLending);
        Long bookLendingDetailsId = bookLendingDetails.getId();

        // Get all the bookLendingList where bookLendingDetails equals to bookLendingDetailsId
        defaultBookLendingShouldBeFound("bookLendingDetailsId.equals=" + bookLendingDetailsId);

        // Get all the bookLendingList where bookLendingDetails equals to (bookLendingDetailsId + 1)
        defaultBookLendingShouldNotBeFound("bookLendingDetailsId.equals=" + (bookLendingDetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookLendingShouldBeFound(String filter) throws Exception {
        restBookLendingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookLending.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restBookLendingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookLendingShouldNotBeFound(String filter) throws Exception {
        restBookLendingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookLendingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBookLending() throws Exception {
        // Get the bookLending
        restBookLendingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBookLending() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();

        // Update the bookLending
        BookLending updatedBookLending = bookLendingRepository.findById(bookLending.getId()).get();
        // Disconnect from session so that the updates on updatedBookLending are not directly saved in db
        em.detach(updatedBookLending);
        updatedBookLending.creationDate(UPDATED_CREATION_DATE).status(UPDATED_STATUS).description(UPDATED_DESCRIPTION);

        restBookLendingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookLending.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookLending))
            )
            .andExpect(status().isOk());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
        BookLending testBookLending = bookLendingList.get(bookLendingList.size() - 1);
        assertThat(testBookLending.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBookLending.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookLending.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBookLending() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();
        bookLending.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookLendingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookLending.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookLending))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookLending() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();
        bookLending.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookLending))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookLending() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();
        bookLending.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookLending)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookLendingWithPatch() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();

        // Update the bookLending using partial update
        BookLending partialUpdatedBookLending = new BookLending();
        partialUpdatedBookLending.setId(bookLending.getId());

        partialUpdatedBookLending.status(UPDATED_STATUS);

        restBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookLending.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookLending))
            )
            .andExpect(status().isOk());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
        BookLending testBookLending = bookLendingList.get(bookLendingList.size() - 1);
        assertThat(testBookLending.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testBookLending.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookLending.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBookLendingWithPatch() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();

        // Update the bookLending using partial update
        BookLending partialUpdatedBookLending = new BookLending();
        partialUpdatedBookLending.setId(bookLending.getId());

        partialUpdatedBookLending.creationDate(UPDATED_CREATION_DATE).status(UPDATED_STATUS).description(UPDATED_DESCRIPTION);

        restBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookLending.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookLending))
            )
            .andExpect(status().isOk());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
        BookLending testBookLending = bookLendingList.get(bookLendingList.size() - 1);
        assertThat(testBookLending.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBookLending.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookLending.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBookLending() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();
        bookLending.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookLending.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookLending))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookLending() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();
        bookLending.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookLending))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookLending() throws Exception {
        int databaseSizeBeforeUpdate = bookLendingRepository.findAll().size();
        bookLending.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookLendingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookLending))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookLending in the database
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookLending() throws Exception {
        // Initialize the database
        bookLendingRepository.saveAndFlush(bookLending);

        int databaseSizeBeforeDelete = bookLendingRepository.findAll().size();

        // Delete the bookLending
        restBookLendingMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookLending.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookLending> bookLendingList = bookLendingRepository.findAll();
        assertThat(bookLendingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
