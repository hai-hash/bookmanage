package com.vnpt.bookmanage.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.Book;
import com.vnpt.bookmanage.domain.Publisher;
import com.vnpt.bookmanage.domain.User;
import com.vnpt.bookmanage.repository.PublisherRepository;
import com.vnpt.bookmanage.service.criteria.PublisherCriteria;
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
 * Integration tests for the {@link PublisherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PublisherResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/publishers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublisherMockMvc;

    private Publisher publisher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createEntity(EntityManager em) {
        Publisher publisher = new Publisher().name(DEFAULT_NAME).modifiedDate(DEFAULT_MODIFIED_DATE).isActive(DEFAULT_IS_ACTIVE);
        return publisher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publisher createUpdatedEntity(EntityManager em) {
        Publisher publisher = new Publisher().name(UPDATED_NAME).modifiedDate(UPDATED_MODIFIED_DATE).isActive(UPDATED_IS_ACTIVE);
        return publisher;
    }

    @BeforeEach
    public void initTest() {
        publisher = createEntity(em);
    }

    @Test
    @Transactional
    void createPublisher() throws Exception {
        int databaseSizeBeforeCreate = publisherRepository.findAll().size();
        // Create the Publisher
        restPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
            .andExpect(status().isCreated());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeCreate + 1);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublisher.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testPublisher.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createPublisherWithExistingId() throws Exception {
        // Create the Publisher with an existing ID
        publisher.setId(1L);

        int databaseSizeBeforeCreate = publisherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = publisherRepository.findAll().size();
        // set the field null
        publisher.setName(null);

        // Create the Publisher, which fails.

        restPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
            .andExpect(status().isBadRequest());

        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = publisherRepository.findAll().size();
        // set the field null
        publisher.setModifiedDate(null);

        // Create the Publisher, which fails.

        restPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
            .andExpect(status().isBadRequest());

        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = publisherRepository.findAll().size();
        // set the field null
        publisher.setIsActive(null);

        // Create the Publisher, which fails.

        restPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
            .andExpect(status().isBadRequest());

        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPublishers() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get the publisher
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL_ID, publisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publisher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getPublishersByIdFiltering() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        Long id = publisher.getId();

        defaultPublisherShouldBeFound("id.equals=" + id);
        defaultPublisherShouldNotBeFound("id.notEquals=" + id);

        defaultPublisherShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPublisherShouldNotBeFound("id.greaterThan=" + id);

        defaultPublisherShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPublisherShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPublishersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where name equals to DEFAULT_NAME
        defaultPublisherShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the publisherList where name equals to UPDATED_NAME
        defaultPublisherShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where name not equals to DEFAULT_NAME
        defaultPublisherShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the publisherList where name not equals to UPDATED_NAME
        defaultPublisherShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPublisherShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the publisherList where name equals to UPDATED_NAME
        defaultPublisherShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where name is not null
        defaultPublisherShouldBeFound("name.specified=true");

        // Get all the publisherList where name is null
        defaultPublisherShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishersByNameContainsSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where name contains DEFAULT_NAME
        defaultPublisherShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the publisherList where name contains UPDATED_NAME
        defaultPublisherShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where name does not contain DEFAULT_NAME
        defaultPublisherShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the publisherList where name does not contain UPDATED_NAME
        defaultPublisherShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultPublisherShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the publisherList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultPublisherShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate not equals to DEFAULT_MODIFIED_DATE
        defaultPublisherShouldNotBeFound("modifiedDate.notEquals=" + DEFAULT_MODIFIED_DATE);

        // Get all the publisherList where modifiedDate not equals to UPDATED_MODIFIED_DATE
        defaultPublisherShouldBeFound("modifiedDate.notEquals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultPublisherShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the publisherList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultPublisherShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate is not null
        defaultPublisherShouldBeFound("modifiedDate.specified=true");

        // Get all the publisherList where modifiedDate is null
        defaultPublisherShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate is greater than or equal to DEFAULT_MODIFIED_DATE
        defaultPublisherShouldBeFound("modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the publisherList where modifiedDate is greater than or equal to UPDATED_MODIFIED_DATE
        defaultPublisherShouldNotBeFound("modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate is less than or equal to DEFAULT_MODIFIED_DATE
        defaultPublisherShouldBeFound("modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the publisherList where modifiedDate is less than or equal to SMALLER_MODIFIED_DATE
        defaultPublisherShouldNotBeFound("modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate is less than DEFAULT_MODIFIED_DATE
        defaultPublisherShouldNotBeFound("modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the publisherList where modifiedDate is less than UPDATED_MODIFIED_DATE
        defaultPublisherShouldBeFound("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishersByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where modifiedDate is greater than DEFAULT_MODIFIED_DATE
        defaultPublisherShouldNotBeFound("modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the publisherList where modifiedDate is greater than SMALLER_MODIFIED_DATE
        defaultPublisherShouldBeFound("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where isActive equals to DEFAULT_IS_ACTIVE
        defaultPublisherShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the publisherList where isActive equals to UPDATED_IS_ACTIVE
        defaultPublisherShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPublishersByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultPublisherShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the publisherList where isActive not equals to UPDATED_IS_ACTIVE
        defaultPublisherShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPublishersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultPublisherShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the publisherList where isActive equals to UPDATED_IS_ACTIVE
        defaultPublisherShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPublishersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        // Get all the publisherList where isActive is not null
        defaultPublisherShouldBeFound("isActive.specified=true");

        // Get all the publisherList where isActive is null
        defaultPublisherShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        publisher.setUser(user);
        publisherRepository.saveAndFlush(publisher);
        Long userId = user.getId();

        // Get all the publisherList where user equals to userId
        defaultPublisherShouldBeFound("userId.equals=" + userId);

        // Get all the publisherList where user equals to (userId + 1)
        defaultPublisherShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllPublishersByBookIsEqualToSomething() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);
        Book book = BookResourceIT.createEntity(em);
        em.persist(book);
        em.flush();
        publisher.addBook(book);
        publisherRepository.saveAndFlush(publisher);
        Long bookId = book.getId();

        // Get all the publisherList where book equals to bookId
        defaultPublisherShouldBeFound("bookId.equals=" + bookId);

        // Get all the publisherList where book equals to (bookId + 1)
        defaultPublisherShouldNotBeFound("bookId.equals=" + (bookId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPublisherShouldBeFound(String filter) throws Exception {
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publisher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPublisherShouldNotBeFound(String filter) throws Exception {
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPublisherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPublisher() throws Exception {
        // Get the publisher
        restPublisherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher
        Publisher updatedPublisher = publisherRepository.findById(publisher.getId()).get();
        // Disconnect from session so that the updates on updatedPublisher are not directly saved in db
        em.detach(updatedPublisher);
        updatedPublisher.name(UPDATED_NAME).modifiedDate(UPDATED_MODIFIED_DATE).isActive(UPDATED_IS_ACTIVE);

        restPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPublisher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPublisher))
            )
            .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublisher.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testPublisher.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publisher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(publisher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePublisherWithPatch() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher using partial update
        Publisher partialUpdatedPublisher = new Publisher();
        partialUpdatedPublisher.setId(publisher.getId());

        partialUpdatedPublisher.isActive(UPDATED_IS_ACTIVE);

        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPublisher))
            )
            .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublisher.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testPublisher.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdatePublisherWithPatch() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();

        // Update the publisher using partial update
        Publisher partialUpdatedPublisher = new Publisher();
        partialUpdatedPublisher.setId(publisher.getId());

        partialUpdatedPublisher.name(UPDATED_NAME).modifiedDate(UPDATED_MODIFIED_DATE).isActive(UPDATED_IS_ACTIVE);

        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPublisher))
            )
            .andExpect(status().isOk());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
        Publisher testPublisher = publisherList.get(publisherList.size() - 1);
        assertThat(testPublisher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublisher.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testPublisher.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, publisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(publisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPublisher() throws Exception {
        int databaseSizeBeforeUpdate = publisherRepository.findAll().size();
        publisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(publisher))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Publisher in the database
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePublisher() throws Exception {
        // Initialize the database
        publisherRepository.saveAndFlush(publisher);

        int databaseSizeBeforeDelete = publisherRepository.findAll().size();

        // Delete the publisher
        restPublisherMockMvc
            .perform(delete(ENTITY_API_URL_ID, publisher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Publisher> publisherList = publisherRepository.findAll();
        assertThat(publisherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
