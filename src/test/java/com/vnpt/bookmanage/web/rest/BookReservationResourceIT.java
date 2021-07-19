package com.vnpt.bookmanage.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.BookItem;
import com.vnpt.bookmanage.domain.BookLendingDetails;
import com.vnpt.bookmanage.domain.BookReservation;
import com.vnpt.bookmanage.domain.Reader;
import com.vnpt.bookmanage.domain.enumeration.ReservationStatus;
import com.vnpt.bookmanage.repository.BookReservationRepository;
import com.vnpt.bookmanage.service.criteria.BookReservationCriteria;
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
 * Integration tests for the {@link BookReservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookReservationResourceIT {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final ReservationStatus DEFAULT_STATUS = ReservationStatus.WAITING;
    private static final ReservationStatus UPDATED_STATUS = ReservationStatus.CANCELED;

    private static final String ENTITY_API_URL = "/api/book-reservations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookReservationRepository bookReservationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookReservationMockMvc;

    private BookReservation bookReservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookReservation createEntity(EntityManager em) {
        BookReservation bookReservation = new BookReservation().creationDate(DEFAULT_CREATION_DATE).status(DEFAULT_STATUS);
        return bookReservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookReservation createUpdatedEntity(EntityManager em) {
        BookReservation bookReservation = new BookReservation().creationDate(UPDATED_CREATION_DATE).status(UPDATED_STATUS);
        return bookReservation;
    }

    @BeforeEach
    public void initTest() {
        bookReservation = createEntity(em);
    }

    @Test
    @Transactional
    void createBookReservation() throws Exception {
        int databaseSizeBeforeCreate = bookReservationRepository.findAll().size();
        // Create the BookReservation
        restBookReservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isCreated());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeCreate + 1);
        BookReservation testBookReservation = bookReservationList.get(bookReservationList.size() - 1);
        assertThat(testBookReservation.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testBookReservation.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createBookReservationWithExistingId() throws Exception {
        // Create the BookReservation with an existing ID
        bookReservation.setId(1L);

        int databaseSizeBeforeCreate = bookReservationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookReservationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookReservations() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList
        restBookReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookReservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getBookReservation() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get the bookReservation
        restBookReservationMockMvc
            .perform(get(ENTITY_API_URL_ID, bookReservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookReservation.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getBookReservationsByIdFiltering() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        Long id = bookReservation.getId();

        defaultBookReservationShouldBeFound("id.equals=" + id);
        defaultBookReservationShouldNotBeFound("id.notEquals=" + id);

        defaultBookReservationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBookReservationShouldNotBeFound("id.greaterThan=" + id);

        defaultBookReservationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBookReservationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate equals to DEFAULT_CREATION_DATE
        defaultBookReservationShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the bookReservationList where creationDate equals to UPDATED_CREATION_DATE
        defaultBookReservationShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultBookReservationShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the bookReservationList where creationDate not equals to UPDATED_CREATION_DATE
        defaultBookReservationShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultBookReservationShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the bookReservationList where creationDate equals to UPDATED_CREATION_DATE
        defaultBookReservationShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate is not null
        defaultBookReservationShouldBeFound("creationDate.specified=true");

        // Get all the bookReservationList where creationDate is null
        defaultBookReservationShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate is greater than or equal to DEFAULT_CREATION_DATE
        defaultBookReservationShouldBeFound("creationDate.greaterThanOrEqual=" + DEFAULT_CREATION_DATE);

        // Get all the bookReservationList where creationDate is greater than or equal to UPDATED_CREATION_DATE
        defaultBookReservationShouldNotBeFound("creationDate.greaterThanOrEqual=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate is less than or equal to DEFAULT_CREATION_DATE
        defaultBookReservationShouldBeFound("creationDate.lessThanOrEqual=" + DEFAULT_CREATION_DATE);

        // Get all the bookReservationList where creationDate is less than or equal to SMALLER_CREATION_DATE
        defaultBookReservationShouldNotBeFound("creationDate.lessThanOrEqual=" + SMALLER_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate is less than DEFAULT_CREATION_DATE
        defaultBookReservationShouldNotBeFound("creationDate.lessThan=" + DEFAULT_CREATION_DATE);

        // Get all the bookReservationList where creationDate is less than UPDATED_CREATION_DATE
        defaultBookReservationShouldBeFound("creationDate.lessThan=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookReservationsByCreationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where creationDate is greater than DEFAULT_CREATION_DATE
        defaultBookReservationShouldNotBeFound("creationDate.greaterThan=" + DEFAULT_CREATION_DATE);

        // Get all the bookReservationList where creationDate is greater than SMALLER_CREATION_DATE
        defaultBookReservationShouldBeFound("creationDate.greaterThan=" + SMALLER_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookReservationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where status equals to DEFAULT_STATUS
        defaultBookReservationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bookReservationList where status equals to UPDATED_STATUS
        defaultBookReservationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookReservationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where status not equals to DEFAULT_STATUS
        defaultBookReservationShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the bookReservationList where status not equals to UPDATED_STATUS
        defaultBookReservationShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookReservationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBookReservationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bookReservationList where status equals to UPDATED_STATUS
        defaultBookReservationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookReservationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList where status is not null
        defaultBookReservationShouldBeFound("status.specified=true");

        // Get all the bookReservationList where status is null
        defaultBookReservationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBookReservationsByBookItemIsEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);
        BookItem bookItem = BookItemResourceIT.createEntity(em);
        em.persist(bookItem);
        em.flush();
        bookReservation.setBookItem(bookItem);
        bookReservationRepository.saveAndFlush(bookReservation);
        Long bookItemId = bookItem.getId();

        // Get all the bookReservationList where bookItem equals to bookItemId
        defaultBookReservationShouldBeFound("bookItemId.equals=" + bookItemId);

        // Get all the bookReservationList where bookItem equals to (bookItemId + 1)
        defaultBookReservationShouldNotBeFound("bookItemId.equals=" + (bookItemId + 1));
    }

    @Test
    @Transactional
    void getAllBookReservationsByReaderIsEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);
        Reader reader = ReaderResourceIT.createEntity(em);
        em.persist(reader);
        em.flush();
        bookReservation.setReader(reader);
        bookReservationRepository.saveAndFlush(bookReservation);
        Long readerId = reader.getId();

        // Get all the bookReservationList where reader equals to readerId
        defaultBookReservationShouldBeFound("readerId.equals=" + readerId);

        // Get all the bookReservationList where reader equals to (readerId + 1)
        defaultBookReservationShouldNotBeFound("readerId.equals=" + (readerId + 1));
    }

    @Test
    @Transactional
    void getAllBookReservationsByBookLendingDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);
        BookLendingDetails bookLendingDetails = BookLendingDetailsResourceIT.createEntity(em);
        em.persist(bookLendingDetails);
        em.flush();
        bookReservation.setBookLendingDetails(bookLendingDetails);
        bookLendingDetails.setBookReservation(bookReservation);
        bookReservationRepository.saveAndFlush(bookReservation);
        Long bookLendingDetailsId = bookLendingDetails.getId();

        // Get all the bookReservationList where bookLendingDetails equals to bookLendingDetailsId
        defaultBookReservationShouldBeFound("bookLendingDetailsId.equals=" + bookLendingDetailsId);

        // Get all the bookReservationList where bookLendingDetails equals to (bookLendingDetailsId + 1)
        defaultBookReservationShouldNotBeFound("bookLendingDetailsId.equals=" + (bookLendingDetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookReservationShouldBeFound(String filter) throws Exception {
        restBookReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookReservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restBookReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookReservationShouldNotBeFound(String filter) throws Exception {
        restBookReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookReservationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBookReservation() throws Exception {
        // Get the bookReservation
        restBookReservationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBookReservation() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();

        // Update the bookReservation
        BookReservation updatedBookReservation = bookReservationRepository.findById(bookReservation.getId()).get();
        // Disconnect from session so that the updates on updatedBookReservation are not directly saved in db
        em.detach(updatedBookReservation);
        updatedBookReservation.creationDate(UPDATED_CREATION_DATE).status(UPDATED_STATUS);

        restBookReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookReservation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookReservation))
            )
            .andExpect(status().isOk());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
        BookReservation testBookReservation = bookReservationList.get(bookReservationList.size() - 1);
        assertThat(testBookReservation.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBookReservation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingBookReservation() throws Exception {
        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();
        bookReservation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookReservation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookReservation() throws Exception {
        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();
        bookReservation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookReservationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookReservation() throws Exception {
        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();
        bookReservation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookReservationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookReservationWithPatch() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();

        // Update the bookReservation using partial update
        BookReservation partialUpdatedBookReservation = new BookReservation();
        partialUpdatedBookReservation.setId(bookReservation.getId());

        partialUpdatedBookReservation.creationDate(UPDATED_CREATION_DATE).status(UPDATED_STATUS);

        restBookReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookReservation))
            )
            .andExpect(status().isOk());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
        BookReservation testBookReservation = bookReservationList.get(bookReservationList.size() - 1);
        assertThat(testBookReservation.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBookReservation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateBookReservationWithPatch() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();

        // Update the bookReservation using partial update
        BookReservation partialUpdatedBookReservation = new BookReservation();
        partialUpdatedBookReservation.setId(bookReservation.getId());

        partialUpdatedBookReservation.creationDate(UPDATED_CREATION_DATE).status(UPDATED_STATUS);

        restBookReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookReservation))
            )
            .andExpect(status().isOk());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
        BookReservation testBookReservation = bookReservationList.get(bookReservationList.size() - 1);
        assertThat(testBookReservation.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testBookReservation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingBookReservation() throws Exception {
        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();
        bookReservation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookReservation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookReservation() throws Exception {
        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();
        bookReservation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookReservationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookReservation() throws Exception {
        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();
        bookReservation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookReservationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookReservation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookReservation() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        int databaseSizeBeforeDelete = bookReservationRepository.findAll().size();

        // Delete the bookReservation
        restBookReservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookReservation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
