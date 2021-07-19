package com.vnpt.bookmanage.web.rest;

import static com.vnpt.bookmanage.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.Book;
import com.vnpt.bookmanage.domain.BookItem;
import com.vnpt.bookmanage.domain.BookReservation;
import com.vnpt.bookmanage.domain.Rack;
import com.vnpt.bookmanage.domain.User;
import com.vnpt.bookmanage.domain.enumeration.BookFormat;
import com.vnpt.bookmanage.domain.enumeration.BookStatus;
import com.vnpt.bookmanage.repository.BookItemRepository;
import com.vnpt.bookmanage.service.criteria.BookItemCriteria;
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
 * Integration tests for the {@link BookItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookItemResourceIT {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_REFERENCE_ONLY = false;
    private static final Boolean UPDATED_IS_REFERENCE_ONLY = true;

    private static final LocalDate DEFAULT_BORROWED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORROWED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BORROWED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final BookFormat DEFAULT_FORMAT = BookFormat.HARDCOVER;
    private static final BookFormat UPDATED_FORMAT = BookFormat.PAPERBACK;

    private static final BookStatus DEFAULT_STATUS = BookStatus.AVAILABLE;
    private static final BookStatus UPDATED_STATUS = BookStatus.RESERVED;

    private static final LocalDate DEFAULT_DATE_OF_PURCHASE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_PURCHASE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_PURCHASE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PUBLICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/book-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookItemRepository bookItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookItemMockMvc;

    private BookItem bookItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookItem createEntity(EntityManager em) {
        BookItem bookItem = new BookItem()
            .barcode(DEFAULT_BARCODE)
            .isReferenceOnly(DEFAULT_IS_REFERENCE_ONLY)
            .borrowed(DEFAULT_BORROWED)
            .dueDate(DEFAULT_DUE_DATE)
            .price(DEFAULT_PRICE)
            .format(DEFAULT_FORMAT)
            .status(DEFAULT_STATUS)
            .dateOfPurchase(DEFAULT_DATE_OF_PURCHASE)
            .publicationDate(DEFAULT_PUBLICATION_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return bookItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookItem createUpdatedEntity(EntityManager em) {
        BookItem bookItem = new BookItem()
            .barcode(UPDATED_BARCODE)
            .isReferenceOnly(UPDATED_IS_REFERENCE_ONLY)
            .borrowed(UPDATED_BORROWED)
            .dueDate(UPDATED_DUE_DATE)
            .price(UPDATED_PRICE)
            .format(UPDATED_FORMAT)
            .status(UPDATED_STATUS)
            .dateOfPurchase(UPDATED_DATE_OF_PURCHASE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return bookItem;
    }

    @BeforeEach
    public void initTest() {
        bookItem = createEntity(em);
    }

    @Test
    @Transactional
    void createBookItem() throws Exception {
        int databaseSizeBeforeCreate = bookItemRepository.findAll().size();
        // Create the BookItem
        restBookItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookItem)))
            .andExpect(status().isCreated());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeCreate + 1);
        BookItem testBookItem = bookItemList.get(bookItemList.size() - 1);
        assertThat(testBookItem.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testBookItem.getIsReferenceOnly()).isEqualTo(DEFAULT_IS_REFERENCE_ONLY);
        assertThat(testBookItem.getBorrowed()).isEqualTo(DEFAULT_BORROWED);
        assertThat(testBookItem.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testBookItem.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testBookItem.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testBookItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBookItem.getDateOfPurchase()).isEqualTo(DEFAULT_DATE_OF_PURCHASE);
        assertThat(testBookItem.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testBookItem.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createBookItemWithExistingId() throws Exception {
        // Create the BookItem with an existing ID
        bookItem.setId(1L);

        int databaseSizeBeforeCreate = bookItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookItem)))
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFormatIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookItemRepository.findAll().size();
        // set the field null
        bookItem.setFormat(null);

        // Create the BookItem, which fails.

        restBookItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookItem)))
            .andExpect(status().isBadRequest());

        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPublicationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookItemRepository.findAll().size();
        // set the field null
        bookItem.setPublicationDate(null);

        // Create the BookItem, which fails.

        restBookItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookItem)))
            .andExpect(status().isBadRequest());

        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookItemRepository.findAll().size();
        // set the field null
        bookItem.setModifiedDate(null);

        // Create the BookItem, which fails.

        restBookItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookItem)))
            .andExpect(status().isBadRequest());

        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookItems() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].isReferenceOnly").value(hasItem(DEFAULT_IS_REFERENCE_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].borrowed").value(hasItem(DEFAULT_BORROWED.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfPurchase").value(hasItem(DEFAULT_DATE_OF_PURCHASE.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBookItem() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get the bookItem
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL_ID, bookItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookItem.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.isReferenceOnly").value(DEFAULT_IS_REFERENCE_ONLY.booleanValue()))
            .andExpect(jsonPath("$.borrowed").value(DEFAULT_BORROWED.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dateOfPurchase").value(DEFAULT_DATE_OF_PURCHASE.toString()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBookItemsByIdFiltering() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        Long id = bookItem.getId();

        defaultBookItemShouldBeFound("id.equals=" + id);
        defaultBookItemShouldNotBeFound("id.notEquals=" + id);

        defaultBookItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBookItemShouldNotBeFound("id.greaterThan=" + id);

        defaultBookItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBookItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBookItemsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where barcode equals to DEFAULT_BARCODE
        defaultBookItemShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the bookItemList where barcode equals to UPDATED_BARCODE
        defaultBookItemShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllBookItemsByBarcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where barcode not equals to DEFAULT_BARCODE
        defaultBookItemShouldNotBeFound("barcode.notEquals=" + DEFAULT_BARCODE);

        // Get all the bookItemList where barcode not equals to UPDATED_BARCODE
        defaultBookItemShouldBeFound("barcode.notEquals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllBookItemsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultBookItemShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the bookItemList where barcode equals to UPDATED_BARCODE
        defaultBookItemShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllBookItemsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where barcode is not null
        defaultBookItemShouldBeFound("barcode.specified=true");

        // Get all the bookItemList where barcode is null
        defaultBookItemShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByBarcodeContainsSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where barcode contains DEFAULT_BARCODE
        defaultBookItemShouldBeFound("barcode.contains=" + DEFAULT_BARCODE);

        // Get all the bookItemList where barcode contains UPDATED_BARCODE
        defaultBookItemShouldNotBeFound("barcode.contains=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllBookItemsByBarcodeNotContainsSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where barcode does not contain DEFAULT_BARCODE
        defaultBookItemShouldNotBeFound("barcode.doesNotContain=" + DEFAULT_BARCODE);

        // Get all the bookItemList where barcode does not contain UPDATED_BARCODE
        defaultBookItemShouldBeFound("barcode.doesNotContain=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllBookItemsByIsReferenceOnlyIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where isReferenceOnly equals to DEFAULT_IS_REFERENCE_ONLY
        defaultBookItemShouldBeFound("isReferenceOnly.equals=" + DEFAULT_IS_REFERENCE_ONLY);

        // Get all the bookItemList where isReferenceOnly equals to UPDATED_IS_REFERENCE_ONLY
        defaultBookItemShouldNotBeFound("isReferenceOnly.equals=" + UPDATED_IS_REFERENCE_ONLY);
    }

    @Test
    @Transactional
    void getAllBookItemsByIsReferenceOnlyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where isReferenceOnly not equals to DEFAULT_IS_REFERENCE_ONLY
        defaultBookItemShouldNotBeFound("isReferenceOnly.notEquals=" + DEFAULT_IS_REFERENCE_ONLY);

        // Get all the bookItemList where isReferenceOnly not equals to UPDATED_IS_REFERENCE_ONLY
        defaultBookItemShouldBeFound("isReferenceOnly.notEquals=" + UPDATED_IS_REFERENCE_ONLY);
    }

    @Test
    @Transactional
    void getAllBookItemsByIsReferenceOnlyIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where isReferenceOnly in DEFAULT_IS_REFERENCE_ONLY or UPDATED_IS_REFERENCE_ONLY
        defaultBookItemShouldBeFound("isReferenceOnly.in=" + DEFAULT_IS_REFERENCE_ONLY + "," + UPDATED_IS_REFERENCE_ONLY);

        // Get all the bookItemList where isReferenceOnly equals to UPDATED_IS_REFERENCE_ONLY
        defaultBookItemShouldNotBeFound("isReferenceOnly.in=" + UPDATED_IS_REFERENCE_ONLY);
    }

    @Test
    @Transactional
    void getAllBookItemsByIsReferenceOnlyIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where isReferenceOnly is not null
        defaultBookItemShouldBeFound("isReferenceOnly.specified=true");

        // Get all the bookItemList where isReferenceOnly is null
        defaultBookItemShouldNotBeFound("isReferenceOnly.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed equals to DEFAULT_BORROWED
        defaultBookItemShouldBeFound("borrowed.equals=" + DEFAULT_BORROWED);

        // Get all the bookItemList where borrowed equals to UPDATED_BORROWED
        defaultBookItemShouldNotBeFound("borrowed.equals=" + UPDATED_BORROWED);
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed not equals to DEFAULT_BORROWED
        defaultBookItemShouldNotBeFound("borrowed.notEquals=" + DEFAULT_BORROWED);

        // Get all the bookItemList where borrowed not equals to UPDATED_BORROWED
        defaultBookItemShouldBeFound("borrowed.notEquals=" + UPDATED_BORROWED);
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed in DEFAULT_BORROWED or UPDATED_BORROWED
        defaultBookItemShouldBeFound("borrowed.in=" + DEFAULT_BORROWED + "," + UPDATED_BORROWED);

        // Get all the bookItemList where borrowed equals to UPDATED_BORROWED
        defaultBookItemShouldNotBeFound("borrowed.in=" + UPDATED_BORROWED);
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed is not null
        defaultBookItemShouldBeFound("borrowed.specified=true");

        // Get all the bookItemList where borrowed is null
        defaultBookItemShouldNotBeFound("borrowed.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed is greater than or equal to DEFAULT_BORROWED
        defaultBookItemShouldBeFound("borrowed.greaterThanOrEqual=" + DEFAULT_BORROWED);

        // Get all the bookItemList where borrowed is greater than or equal to UPDATED_BORROWED
        defaultBookItemShouldNotBeFound("borrowed.greaterThanOrEqual=" + UPDATED_BORROWED);
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed is less than or equal to DEFAULT_BORROWED
        defaultBookItemShouldBeFound("borrowed.lessThanOrEqual=" + DEFAULT_BORROWED);

        // Get all the bookItemList where borrowed is less than or equal to SMALLER_BORROWED
        defaultBookItemShouldNotBeFound("borrowed.lessThanOrEqual=" + SMALLER_BORROWED);
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsLessThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed is less than DEFAULT_BORROWED
        defaultBookItemShouldNotBeFound("borrowed.lessThan=" + DEFAULT_BORROWED);

        // Get all the bookItemList where borrowed is less than UPDATED_BORROWED
        defaultBookItemShouldBeFound("borrowed.lessThan=" + UPDATED_BORROWED);
    }

    @Test
    @Transactional
    void getAllBookItemsByBorrowedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where borrowed is greater than DEFAULT_BORROWED
        defaultBookItemShouldNotBeFound("borrowed.greaterThan=" + DEFAULT_BORROWED);

        // Get all the bookItemList where borrowed is greater than SMALLER_BORROWED
        defaultBookItemShouldBeFound("borrowed.greaterThan=" + SMALLER_BORROWED);
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate equals to DEFAULT_DUE_DATE
        defaultBookItemShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the bookItemList where dueDate equals to UPDATED_DUE_DATE
        defaultBookItemShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate not equals to DEFAULT_DUE_DATE
        defaultBookItemShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the bookItemList where dueDate not equals to UPDATED_DUE_DATE
        defaultBookItemShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultBookItemShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the bookItemList where dueDate equals to UPDATED_DUE_DATE
        defaultBookItemShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate is not null
        defaultBookItemShouldBeFound("dueDate.specified=true");

        // Get all the bookItemList where dueDate is null
        defaultBookItemShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultBookItemShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the bookItemList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultBookItemShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultBookItemShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the bookItemList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultBookItemShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate is less than DEFAULT_DUE_DATE
        defaultBookItemShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the bookItemList where dueDate is less than UPDATED_DUE_DATE
        defaultBookItemShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dueDate is greater than DEFAULT_DUE_DATE
        defaultBookItemShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the bookItemList where dueDate is greater than SMALLER_DUE_DATE
        defaultBookItemShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price equals to DEFAULT_PRICE
        defaultBookItemShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the bookItemList where price equals to UPDATED_PRICE
        defaultBookItemShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price not equals to DEFAULT_PRICE
        defaultBookItemShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the bookItemList where price not equals to UPDATED_PRICE
        defaultBookItemShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultBookItemShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the bookItemList where price equals to UPDATED_PRICE
        defaultBookItemShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price is not null
        defaultBookItemShouldBeFound("price.specified=true");

        // Get all the bookItemList where price is null
        defaultBookItemShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price is greater than or equal to DEFAULT_PRICE
        defaultBookItemShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the bookItemList where price is greater than or equal to UPDATED_PRICE
        defaultBookItemShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price is less than or equal to DEFAULT_PRICE
        defaultBookItemShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the bookItemList where price is less than or equal to SMALLER_PRICE
        defaultBookItemShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price is less than DEFAULT_PRICE
        defaultBookItemShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the bookItemList where price is less than UPDATED_PRICE
        defaultBookItemShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where price is greater than DEFAULT_PRICE
        defaultBookItemShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the bookItemList where price is greater than SMALLER_PRICE
        defaultBookItemShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllBookItemsByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where format equals to DEFAULT_FORMAT
        defaultBookItemShouldBeFound("format.equals=" + DEFAULT_FORMAT);

        // Get all the bookItemList where format equals to UPDATED_FORMAT
        defaultBookItemShouldNotBeFound("format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllBookItemsByFormatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where format not equals to DEFAULT_FORMAT
        defaultBookItemShouldNotBeFound("format.notEquals=" + DEFAULT_FORMAT);

        // Get all the bookItemList where format not equals to UPDATED_FORMAT
        defaultBookItemShouldBeFound("format.notEquals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllBookItemsByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where format in DEFAULT_FORMAT or UPDATED_FORMAT
        defaultBookItemShouldBeFound("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT);

        // Get all the bookItemList where format equals to UPDATED_FORMAT
        defaultBookItemShouldNotBeFound("format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllBookItemsByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where format is not null
        defaultBookItemShouldBeFound("format.specified=true");

        // Get all the bookItemList where format is null
        defaultBookItemShouldNotBeFound("format.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where status equals to DEFAULT_STATUS
        defaultBookItemShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bookItemList where status equals to UPDATED_STATUS
        defaultBookItemShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookItemsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where status not equals to DEFAULT_STATUS
        defaultBookItemShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the bookItemList where status not equals to UPDATED_STATUS
        defaultBookItemShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookItemsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBookItemShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bookItemList where status equals to UPDATED_STATUS
        defaultBookItemShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookItemsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where status is not null
        defaultBookItemShouldBeFound("status.specified=true");

        // Get all the bookItemList where status is null
        defaultBookItemShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase equals to DEFAULT_DATE_OF_PURCHASE
        defaultBookItemShouldBeFound("dateOfPurchase.equals=" + DEFAULT_DATE_OF_PURCHASE);

        // Get all the bookItemList where dateOfPurchase equals to UPDATED_DATE_OF_PURCHASE
        defaultBookItemShouldNotBeFound("dateOfPurchase.equals=" + UPDATED_DATE_OF_PURCHASE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase not equals to DEFAULT_DATE_OF_PURCHASE
        defaultBookItemShouldNotBeFound("dateOfPurchase.notEquals=" + DEFAULT_DATE_OF_PURCHASE);

        // Get all the bookItemList where dateOfPurchase not equals to UPDATED_DATE_OF_PURCHASE
        defaultBookItemShouldBeFound("dateOfPurchase.notEquals=" + UPDATED_DATE_OF_PURCHASE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase in DEFAULT_DATE_OF_PURCHASE or UPDATED_DATE_OF_PURCHASE
        defaultBookItemShouldBeFound("dateOfPurchase.in=" + DEFAULT_DATE_OF_PURCHASE + "," + UPDATED_DATE_OF_PURCHASE);

        // Get all the bookItemList where dateOfPurchase equals to UPDATED_DATE_OF_PURCHASE
        defaultBookItemShouldNotBeFound("dateOfPurchase.in=" + UPDATED_DATE_OF_PURCHASE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase is not null
        defaultBookItemShouldBeFound("dateOfPurchase.specified=true");

        // Get all the bookItemList where dateOfPurchase is null
        defaultBookItemShouldNotBeFound("dateOfPurchase.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase is greater than or equal to DEFAULT_DATE_OF_PURCHASE
        defaultBookItemShouldBeFound("dateOfPurchase.greaterThanOrEqual=" + DEFAULT_DATE_OF_PURCHASE);

        // Get all the bookItemList where dateOfPurchase is greater than or equal to UPDATED_DATE_OF_PURCHASE
        defaultBookItemShouldNotBeFound("dateOfPurchase.greaterThanOrEqual=" + UPDATED_DATE_OF_PURCHASE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase is less than or equal to DEFAULT_DATE_OF_PURCHASE
        defaultBookItemShouldBeFound("dateOfPurchase.lessThanOrEqual=" + DEFAULT_DATE_OF_PURCHASE);

        // Get all the bookItemList where dateOfPurchase is less than or equal to SMALLER_DATE_OF_PURCHASE
        defaultBookItemShouldNotBeFound("dateOfPurchase.lessThanOrEqual=" + SMALLER_DATE_OF_PURCHASE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsLessThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase is less than DEFAULT_DATE_OF_PURCHASE
        defaultBookItemShouldNotBeFound("dateOfPurchase.lessThan=" + DEFAULT_DATE_OF_PURCHASE);

        // Get all the bookItemList where dateOfPurchase is less than UPDATED_DATE_OF_PURCHASE
        defaultBookItemShouldBeFound("dateOfPurchase.lessThan=" + UPDATED_DATE_OF_PURCHASE);
    }

    @Test
    @Transactional
    void getAllBookItemsByDateOfPurchaseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where dateOfPurchase is greater than DEFAULT_DATE_OF_PURCHASE
        defaultBookItemShouldNotBeFound("dateOfPurchase.greaterThan=" + DEFAULT_DATE_OF_PURCHASE);

        // Get all the bookItemList where dateOfPurchase is greater than SMALLER_DATE_OF_PURCHASE
        defaultBookItemShouldBeFound("dateOfPurchase.greaterThan=" + SMALLER_DATE_OF_PURCHASE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate equals to DEFAULT_PUBLICATION_DATE
        defaultBookItemShouldBeFound("publicationDate.equals=" + DEFAULT_PUBLICATION_DATE);

        // Get all the bookItemList where publicationDate equals to UPDATED_PUBLICATION_DATE
        defaultBookItemShouldNotBeFound("publicationDate.equals=" + UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate not equals to DEFAULT_PUBLICATION_DATE
        defaultBookItemShouldNotBeFound("publicationDate.notEquals=" + DEFAULT_PUBLICATION_DATE);

        // Get all the bookItemList where publicationDate not equals to UPDATED_PUBLICATION_DATE
        defaultBookItemShouldBeFound("publicationDate.notEquals=" + UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate in DEFAULT_PUBLICATION_DATE or UPDATED_PUBLICATION_DATE
        defaultBookItemShouldBeFound("publicationDate.in=" + DEFAULT_PUBLICATION_DATE + "," + UPDATED_PUBLICATION_DATE);

        // Get all the bookItemList where publicationDate equals to UPDATED_PUBLICATION_DATE
        defaultBookItemShouldNotBeFound("publicationDate.in=" + UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate is not null
        defaultBookItemShouldBeFound("publicationDate.specified=true");

        // Get all the bookItemList where publicationDate is null
        defaultBookItemShouldNotBeFound("publicationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate is greater than or equal to DEFAULT_PUBLICATION_DATE
        defaultBookItemShouldBeFound("publicationDate.greaterThanOrEqual=" + DEFAULT_PUBLICATION_DATE);

        // Get all the bookItemList where publicationDate is greater than or equal to UPDATED_PUBLICATION_DATE
        defaultBookItemShouldNotBeFound("publicationDate.greaterThanOrEqual=" + UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate is less than or equal to DEFAULT_PUBLICATION_DATE
        defaultBookItemShouldBeFound("publicationDate.lessThanOrEqual=" + DEFAULT_PUBLICATION_DATE);

        // Get all the bookItemList where publicationDate is less than or equal to SMALLER_PUBLICATION_DATE
        defaultBookItemShouldNotBeFound("publicationDate.lessThanOrEqual=" + SMALLER_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate is less than DEFAULT_PUBLICATION_DATE
        defaultBookItemShouldNotBeFound("publicationDate.lessThan=" + DEFAULT_PUBLICATION_DATE);

        // Get all the bookItemList where publicationDate is less than UPDATED_PUBLICATION_DATE
        defaultBookItemShouldBeFound("publicationDate.lessThan=" + UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByPublicationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where publicationDate is greater than DEFAULT_PUBLICATION_DATE
        defaultBookItemShouldNotBeFound("publicationDate.greaterThan=" + DEFAULT_PUBLICATION_DATE);

        // Get all the bookItemList where publicationDate is greater than SMALLER_PUBLICATION_DATE
        defaultBookItemShouldBeFound("publicationDate.greaterThan=" + SMALLER_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultBookItemShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookItemList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultBookItemShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate not equals to DEFAULT_MODIFIED_DATE
        defaultBookItemShouldNotBeFound("modifiedDate.notEquals=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookItemList where modifiedDate not equals to UPDATED_MODIFIED_DATE
        defaultBookItemShouldBeFound("modifiedDate.notEquals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultBookItemShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the bookItemList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultBookItemShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate is not null
        defaultBookItemShouldBeFound("modifiedDate.specified=true");

        // Get all the bookItemList where modifiedDate is null
        defaultBookItemShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate is greater than or equal to DEFAULT_MODIFIED_DATE
        defaultBookItemShouldBeFound("modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookItemList where modifiedDate is greater than or equal to UPDATED_MODIFIED_DATE
        defaultBookItemShouldNotBeFound("modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate is less than or equal to DEFAULT_MODIFIED_DATE
        defaultBookItemShouldBeFound("modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookItemList where modifiedDate is less than or equal to SMALLER_MODIFIED_DATE
        defaultBookItemShouldNotBeFound("modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate is less than DEFAULT_MODIFIED_DATE
        defaultBookItemShouldNotBeFound("modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookItemList where modifiedDate is less than UPDATED_MODIFIED_DATE
        defaultBookItemShouldBeFound("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        // Get all the bookItemList where modifiedDate is greater than DEFAULT_MODIFIED_DATE
        defaultBookItemShouldNotBeFound("modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookItemList where modifiedDate is greater than SMALLER_MODIFIED_DATE
        defaultBookItemShouldBeFound("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBookItemsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        bookItem.setUser(user);
        bookItemRepository.saveAndFlush(bookItem);
        Long userId = user.getId();

        // Get all the bookItemList where user equals to userId
        defaultBookItemShouldBeFound("userId.equals=" + userId);

        // Get all the bookItemList where user equals to (userId + 1)
        defaultBookItemShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllBookItemsByRackIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);
        Rack rack = RackResourceIT.createEntity(em);
        em.persist(rack);
        em.flush();
        bookItem.setRack(rack);
        bookItemRepository.saveAndFlush(bookItem);
        Long rackId = rack.getId();

        // Get all the bookItemList where rack equals to rackId
        defaultBookItemShouldBeFound("rackId.equals=" + rackId);

        // Get all the bookItemList where rack equals to (rackId + 1)
        defaultBookItemShouldNotBeFound("rackId.equals=" + (rackId + 1));
    }

    @Test
    @Transactional
    void getAllBookItemsByBookIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);
        Book book = BookResourceIT.createEntity(em);
        em.persist(book);
        em.flush();
        bookItem.setBook(book);
        bookItemRepository.saveAndFlush(bookItem);
        Long bookId = book.getId();

        // Get all the bookItemList where book equals to bookId
        defaultBookItemShouldBeFound("bookId.equals=" + bookId);

        // Get all the bookItemList where book equals to (bookId + 1)
        defaultBookItemShouldNotBeFound("bookId.equals=" + (bookId + 1));
    }

    @Test
    @Transactional
    void getAllBookItemsByBookReservationIsEqualToSomething() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);
        BookReservation bookReservation = BookReservationResourceIT.createEntity(em);
        em.persist(bookReservation);
        em.flush();
        bookItem.setBookReservation(bookReservation);
        bookReservation.setBookItem(bookItem);
        bookItemRepository.saveAndFlush(bookItem);
        Long bookReservationId = bookReservation.getId();

        // Get all the bookItemList where bookReservation equals to bookReservationId
        defaultBookItemShouldBeFound("bookReservationId.equals=" + bookReservationId);

        // Get all the bookItemList where bookReservation equals to (bookReservationId + 1)
        defaultBookItemShouldNotBeFound("bookReservationId.equals=" + (bookReservationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookItemShouldBeFound(String filter) throws Exception {
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].isReferenceOnly").value(hasItem(DEFAULT_IS_REFERENCE_ONLY.booleanValue())))
            .andExpect(jsonPath("$.[*].borrowed").value(hasItem(DEFAULT_BORROWED.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfPurchase").value(hasItem(DEFAULT_DATE_OF_PURCHASE.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookItemShouldNotBeFound(String filter) throws Exception {
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBookItem() throws Exception {
        // Get the bookItem
        restBookItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBookItem() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();

        // Update the bookItem
        BookItem updatedBookItem = bookItemRepository.findById(bookItem.getId()).get();
        // Disconnect from session so that the updates on updatedBookItem are not directly saved in db
        em.detach(updatedBookItem);
        updatedBookItem
            .barcode(UPDATED_BARCODE)
            .isReferenceOnly(UPDATED_IS_REFERENCE_ONLY)
            .borrowed(UPDATED_BORROWED)
            .dueDate(UPDATED_DUE_DATE)
            .price(UPDATED_PRICE)
            .format(UPDATED_FORMAT)
            .status(UPDATED_STATUS)
            .dateOfPurchase(UPDATED_DATE_OF_PURCHASE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBookItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookItem))
            )
            .andExpect(status().isOk());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
        BookItem testBookItem = bookItemList.get(bookItemList.size() - 1);
        assertThat(testBookItem.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testBookItem.getIsReferenceOnly()).isEqualTo(UPDATED_IS_REFERENCE_ONLY);
        assertThat(testBookItem.getBorrowed()).isEqualTo(UPDATED_BORROWED);
        assertThat(testBookItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testBookItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBookItem.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testBookItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookItem.getDateOfPurchase()).isEqualTo(UPDATED_DATE_OF_PURCHASE);
        assertThat(testBookItem.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testBookItem.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();
        bookItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();
        bookItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();
        bookItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookItemWithPatch() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();

        // Update the bookItem using partial update
        BookItem partialUpdatedBookItem = new BookItem();
        partialUpdatedBookItem.setId(bookItem.getId());

        partialUpdatedBookItem
            .dueDate(UPDATED_DUE_DATE)
            .format(UPDATED_FORMAT)
            .status(UPDATED_STATUS)
            .dateOfPurchase(UPDATED_DATE_OF_PURCHASE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBookItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookItem))
            )
            .andExpect(status().isOk());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
        BookItem testBookItem = bookItemList.get(bookItemList.size() - 1);
        assertThat(testBookItem.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testBookItem.getIsReferenceOnly()).isEqualTo(DEFAULT_IS_REFERENCE_ONLY);
        assertThat(testBookItem.getBorrowed()).isEqualTo(DEFAULT_BORROWED);
        assertThat(testBookItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testBookItem.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testBookItem.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testBookItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookItem.getDateOfPurchase()).isEqualTo(UPDATED_DATE_OF_PURCHASE);
        assertThat(testBookItem.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testBookItem.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateBookItemWithPatch() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();

        // Update the bookItem using partial update
        BookItem partialUpdatedBookItem = new BookItem();
        partialUpdatedBookItem.setId(bookItem.getId());

        partialUpdatedBookItem
            .barcode(UPDATED_BARCODE)
            .isReferenceOnly(UPDATED_IS_REFERENCE_ONLY)
            .borrowed(UPDATED_BORROWED)
            .dueDate(UPDATED_DUE_DATE)
            .price(UPDATED_PRICE)
            .format(UPDATED_FORMAT)
            .status(UPDATED_STATUS)
            .dateOfPurchase(UPDATED_DATE_OF_PURCHASE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBookItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookItem))
            )
            .andExpect(status().isOk());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
        BookItem testBookItem = bookItemList.get(bookItemList.size() - 1);
        assertThat(testBookItem.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testBookItem.getIsReferenceOnly()).isEqualTo(UPDATED_IS_REFERENCE_ONLY);
        assertThat(testBookItem.getBorrowed()).isEqualTo(UPDATED_BORROWED);
        assertThat(testBookItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testBookItem.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testBookItem.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testBookItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookItem.getDateOfPurchase()).isEqualTo(UPDATED_DATE_OF_PURCHASE);
        assertThat(testBookItem.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testBookItem.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();
        bookItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();
        bookItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookItemRepository.findAll().size();
        bookItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookItem in the database
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookItem() throws Exception {
        // Initialize the database
        bookItemRepository.saveAndFlush(bookItem);

        int databaseSizeBeforeDelete = bookItemRepository.findAll().size();

        // Delete the bookItem
        restBookItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookItem> bookItemList = bookItemRepository.findAll();
        assertThat(bookItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
