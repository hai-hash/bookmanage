package com.vnpt.bookmanage.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vnpt.bookmanage.IntegrationTest;
import com.vnpt.bookmanage.domain.Author;
import com.vnpt.bookmanage.domain.Book;
import com.vnpt.bookmanage.domain.BookItem;
import com.vnpt.bookmanage.domain.Catagory;
import com.vnpt.bookmanage.domain.Publisher;
import com.vnpt.bookmanage.domain.User;
import com.vnpt.bookmanage.repository.BookRepository;
import com.vnpt.bookmanage.service.BookService;
import com.vnpt.bookmanage.service.criteria.BookCriteria;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookResourceIT {

    private static final String DEFAULT_ISBN = "AAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET = "AAAAAAAAAA";
    private static final String UPDATED_TARGET = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_PAGES = 1;
    private static final Integer UPDATED_NUMBER_OF_PAGES = 2;
    private static final Integer SMALLER_NUMBER_OF_PAGES = 1 - 1;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MODIFIED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookRepository bookRepository;

    @Mock
    private BookRepository bookRepositoryMock;

    @Mock
    private BookService bookServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookMockMvc;

    private Book book;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity(EntityManager em) {
        Book book = new Book()
            .isbn(DEFAULT_ISBN)
            .title(DEFAULT_TITLE)
            .subject(DEFAULT_SUBJECT)
            .target(DEFAULT_TARGET)
            .language(DEFAULT_LANGUAGE)
            .numberOfPages(DEFAULT_NUMBER_OF_PAGES)
            .imageUrl(DEFAULT_IMAGE_URL)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return book;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createUpdatedEntity(EntityManager em) {
        Book book = new Book()
            .isbn(UPDATED_ISBN)
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .target(UPDATED_TARGET)
            .language(UPDATED_LANGUAGE)
            .numberOfPages(UPDATED_NUMBER_OF_PAGES)
            .imageUrl(UPDATED_IMAGE_URL)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return book;
    }

    @BeforeEach
    public void initTest() {
        book = createEntity(em);
    }

    @Test
    @Transactional
    void createBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();
        // Create the Book
        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeCreate + 1);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testBook.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testBook.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testBook.getNumberOfPages()).isEqualTo(DEFAULT_NUMBER_OF_PAGES);
        assertThat(testBook.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testBook.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createBookWithExistingId() throws Exception {
        // Create the Book with an existing ID
        book.setId(1L);

        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsbnIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setIsbn(null);

        // Create the Book, which fails.

        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setTitle(null);

        // Create the Book, which fails.

        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setSubject(null);

        // Create the Book, which fails.

        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setLanguage(null);

        // Create the Book, which fails.

        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfPagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setNumberOfPages(null);

        // Create the Book, which fails.

        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setModifiedDate(null);

        // Create the Book, which fails.

        restBookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].numberOfPages").value(hasItem(DEFAULT_NUMBER_OF_PAGES)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc
            .perform(get(ENTITY_API_URL_ID, book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId().intValue()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.numberOfPages").value(DEFAULT_NUMBER_OF_PAGES))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBooksByIdFiltering() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        Long id = book.getId();

        defaultBookShouldBeFound("id.equals=" + id);
        defaultBookShouldNotBeFound("id.notEquals=" + id);

        defaultBookShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBookShouldNotBeFound("id.greaterThan=" + id);

        defaultBookShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBookShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn equals to DEFAULT_ISBN
        defaultBookShouldBeFound("isbn.equals=" + DEFAULT_ISBN);

        // Get all the bookList where isbn equals to UPDATED_ISBN
        defaultBookShouldNotBeFound("isbn.equals=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn not equals to DEFAULT_ISBN
        defaultBookShouldNotBeFound("isbn.notEquals=" + DEFAULT_ISBN);

        // Get all the bookList where isbn not equals to UPDATED_ISBN
        defaultBookShouldBeFound("isbn.notEquals=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn in DEFAULT_ISBN or UPDATED_ISBN
        defaultBookShouldBeFound("isbn.in=" + DEFAULT_ISBN + "," + UPDATED_ISBN);

        // Get all the bookList where isbn equals to UPDATED_ISBN
        defaultBookShouldNotBeFound("isbn.in=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn is not null
        defaultBookShouldBeFound("isbn.specified=true");

        // Get all the bookList where isbn is null
        defaultBookShouldNotBeFound("isbn.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByIsbnContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn contains DEFAULT_ISBN
        defaultBookShouldBeFound("isbn.contains=" + DEFAULT_ISBN);

        // Get all the bookList where isbn contains UPDATED_ISBN
        defaultBookShouldNotBeFound("isbn.contains=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByIsbnNotContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where isbn does not contain DEFAULT_ISBN
        defaultBookShouldNotBeFound("isbn.doesNotContain=" + DEFAULT_ISBN);

        // Get all the bookList where isbn does not contain UPDATED_ISBN
        defaultBookShouldBeFound("isbn.doesNotContain=" + UPDATED_ISBN);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title equals to DEFAULT_TITLE
        defaultBookShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the bookList where title equals to UPDATED_TITLE
        defaultBookShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title not equals to DEFAULT_TITLE
        defaultBookShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the bookList where title not equals to UPDATED_TITLE
        defaultBookShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBookShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the bookList where title equals to UPDATED_TITLE
        defaultBookShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title is not null
        defaultBookShouldBeFound("title.specified=true");

        // Get all the bookList where title is null
        defaultBookShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByTitleContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title contains DEFAULT_TITLE
        defaultBookShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the bookList where title contains UPDATED_TITLE
        defaultBookShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title does not contain DEFAULT_TITLE
        defaultBookShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the bookList where title does not contain UPDATED_TITLE
        defaultBookShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBooksBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where subject equals to DEFAULT_SUBJECT
        defaultBookShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the bookList where subject equals to UPDATED_SUBJECT
        defaultBookShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllBooksBySubjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where subject not equals to DEFAULT_SUBJECT
        defaultBookShouldNotBeFound("subject.notEquals=" + DEFAULT_SUBJECT);

        // Get all the bookList where subject not equals to UPDATED_SUBJECT
        defaultBookShouldBeFound("subject.notEquals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllBooksBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultBookShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the bookList where subject equals to UPDATED_SUBJECT
        defaultBookShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllBooksBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where subject is not null
        defaultBookShouldBeFound("subject.specified=true");

        // Get all the bookList where subject is null
        defaultBookShouldNotBeFound("subject.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksBySubjectContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where subject contains DEFAULT_SUBJECT
        defaultBookShouldBeFound("subject.contains=" + DEFAULT_SUBJECT);

        // Get all the bookList where subject contains UPDATED_SUBJECT
        defaultBookShouldNotBeFound("subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllBooksBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where subject does not contain DEFAULT_SUBJECT
        defaultBookShouldNotBeFound("subject.doesNotContain=" + DEFAULT_SUBJECT);

        // Get all the bookList where subject does not contain UPDATED_SUBJECT
        defaultBookShouldBeFound("subject.doesNotContain=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllBooksByTargetIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where target equals to DEFAULT_TARGET
        defaultBookShouldBeFound("target.equals=" + DEFAULT_TARGET);

        // Get all the bookList where target equals to UPDATED_TARGET
        defaultBookShouldNotBeFound("target.equals=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllBooksByTargetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where target not equals to DEFAULT_TARGET
        defaultBookShouldNotBeFound("target.notEquals=" + DEFAULT_TARGET);

        // Get all the bookList where target not equals to UPDATED_TARGET
        defaultBookShouldBeFound("target.notEquals=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllBooksByTargetIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where target in DEFAULT_TARGET or UPDATED_TARGET
        defaultBookShouldBeFound("target.in=" + DEFAULT_TARGET + "," + UPDATED_TARGET);

        // Get all the bookList where target equals to UPDATED_TARGET
        defaultBookShouldNotBeFound("target.in=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllBooksByTargetIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where target is not null
        defaultBookShouldBeFound("target.specified=true");

        // Get all the bookList where target is null
        defaultBookShouldNotBeFound("target.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByTargetContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where target contains DEFAULT_TARGET
        defaultBookShouldBeFound("target.contains=" + DEFAULT_TARGET);

        // Get all the bookList where target contains UPDATED_TARGET
        defaultBookShouldNotBeFound("target.contains=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllBooksByTargetNotContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where target does not contain DEFAULT_TARGET
        defaultBookShouldNotBeFound("target.doesNotContain=" + DEFAULT_TARGET);

        // Get all the bookList where target does not contain UPDATED_TARGET
        defaultBookShouldBeFound("target.doesNotContain=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where language equals to DEFAULT_LANGUAGE
        defaultBookShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the bookList where language equals to UPDATED_LANGUAGE
        defaultBookShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where language not equals to DEFAULT_LANGUAGE
        defaultBookShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the bookList where language not equals to UPDATED_LANGUAGE
        defaultBookShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultBookShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the bookList where language equals to UPDATED_LANGUAGE
        defaultBookShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where language is not null
        defaultBookShouldBeFound("language.specified=true");

        // Get all the bookList where language is null
        defaultBookShouldNotBeFound("language.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByLanguageContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where language contains DEFAULT_LANGUAGE
        defaultBookShouldBeFound("language.contains=" + DEFAULT_LANGUAGE);

        // Get all the bookList where language contains UPDATED_LANGUAGE
        defaultBookShouldNotBeFound("language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where language does not contain DEFAULT_LANGUAGE
        defaultBookShouldNotBeFound("language.doesNotContain=" + DEFAULT_LANGUAGE);

        // Get all the bookList where language does not contain UPDATED_LANGUAGE
        defaultBookShouldBeFound("language.doesNotContain=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages equals to DEFAULT_NUMBER_OF_PAGES
        defaultBookShouldBeFound("numberOfPages.equals=" + DEFAULT_NUMBER_OF_PAGES);

        // Get all the bookList where numberOfPages equals to UPDATED_NUMBER_OF_PAGES
        defaultBookShouldNotBeFound("numberOfPages.equals=" + UPDATED_NUMBER_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages not equals to DEFAULT_NUMBER_OF_PAGES
        defaultBookShouldNotBeFound("numberOfPages.notEquals=" + DEFAULT_NUMBER_OF_PAGES);

        // Get all the bookList where numberOfPages not equals to UPDATED_NUMBER_OF_PAGES
        defaultBookShouldBeFound("numberOfPages.notEquals=" + UPDATED_NUMBER_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages in DEFAULT_NUMBER_OF_PAGES or UPDATED_NUMBER_OF_PAGES
        defaultBookShouldBeFound("numberOfPages.in=" + DEFAULT_NUMBER_OF_PAGES + "," + UPDATED_NUMBER_OF_PAGES);

        // Get all the bookList where numberOfPages equals to UPDATED_NUMBER_OF_PAGES
        defaultBookShouldNotBeFound("numberOfPages.in=" + UPDATED_NUMBER_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages is not null
        defaultBookShouldBeFound("numberOfPages.specified=true");

        // Get all the bookList where numberOfPages is null
        defaultBookShouldNotBeFound("numberOfPages.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages is greater than or equal to DEFAULT_NUMBER_OF_PAGES
        defaultBookShouldBeFound("numberOfPages.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PAGES);

        // Get all the bookList where numberOfPages is greater than or equal to UPDATED_NUMBER_OF_PAGES
        defaultBookShouldNotBeFound("numberOfPages.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages is less than or equal to DEFAULT_NUMBER_OF_PAGES
        defaultBookShouldBeFound("numberOfPages.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PAGES);

        // Get all the bookList where numberOfPages is less than or equal to SMALLER_NUMBER_OF_PAGES
        defaultBookShouldNotBeFound("numberOfPages.lessThanOrEqual=" + SMALLER_NUMBER_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsLessThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages is less than DEFAULT_NUMBER_OF_PAGES
        defaultBookShouldNotBeFound("numberOfPages.lessThan=" + DEFAULT_NUMBER_OF_PAGES);

        // Get all the bookList where numberOfPages is less than UPDATED_NUMBER_OF_PAGES
        defaultBookShouldBeFound("numberOfPages.lessThan=" + UPDATED_NUMBER_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByNumberOfPagesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where numberOfPages is greater than DEFAULT_NUMBER_OF_PAGES
        defaultBookShouldNotBeFound("numberOfPages.greaterThan=" + DEFAULT_NUMBER_OF_PAGES);

        // Get all the bookList where numberOfPages is greater than SMALLER_NUMBER_OF_PAGES
        defaultBookShouldBeFound("numberOfPages.greaterThan=" + SMALLER_NUMBER_OF_PAGES);
    }

    @Test
    @Transactional
    void getAllBooksByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultBookShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the bookList where imageUrl equals to UPDATED_IMAGE_URL
        defaultBookShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllBooksByImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where imageUrl not equals to DEFAULT_IMAGE_URL
        defaultBookShouldNotBeFound("imageUrl.notEquals=" + DEFAULT_IMAGE_URL);

        // Get all the bookList where imageUrl not equals to UPDATED_IMAGE_URL
        defaultBookShouldBeFound("imageUrl.notEquals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllBooksByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultBookShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the bookList where imageUrl equals to UPDATED_IMAGE_URL
        defaultBookShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllBooksByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where imageUrl is not null
        defaultBookShouldBeFound("imageUrl.specified=true");

        // Get all the bookList where imageUrl is null
        defaultBookShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where imageUrl contains DEFAULT_IMAGE_URL
        defaultBookShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the bookList where imageUrl contains UPDATED_IMAGE_URL
        defaultBookShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllBooksByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultBookShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the bookList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultBookShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate equals to DEFAULT_MODIFIED_DATE
        defaultBookShouldBeFound("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultBookShouldNotBeFound("modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate not equals to DEFAULT_MODIFIED_DATE
        defaultBookShouldNotBeFound("modifiedDate.notEquals=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookList where modifiedDate not equals to UPDATED_MODIFIED_DATE
        defaultBookShouldBeFound("modifiedDate.notEquals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate in DEFAULT_MODIFIED_DATE or UPDATED_MODIFIED_DATE
        defaultBookShouldBeFound("modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE);

        // Get all the bookList where modifiedDate equals to UPDATED_MODIFIED_DATE
        defaultBookShouldNotBeFound("modifiedDate.in=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate is not null
        defaultBookShouldBeFound("modifiedDate.specified=true");

        // Get all the bookList where modifiedDate is null
        defaultBookShouldNotBeFound("modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate is greater than or equal to DEFAULT_MODIFIED_DATE
        defaultBookShouldBeFound("modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookList where modifiedDate is greater than or equal to UPDATED_MODIFIED_DATE
        defaultBookShouldNotBeFound("modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate is less than or equal to DEFAULT_MODIFIED_DATE
        defaultBookShouldBeFound("modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookList where modifiedDate is less than or equal to SMALLER_MODIFIED_DATE
        defaultBookShouldNotBeFound("modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate is less than DEFAULT_MODIFIED_DATE
        defaultBookShouldNotBeFound("modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookList where modifiedDate is less than UPDATED_MODIFIED_DATE
        defaultBookShouldBeFound("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where modifiedDate is greater than DEFAULT_MODIFIED_DATE
        defaultBookShouldNotBeFound("modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);

        // Get all the bookList where modifiedDate is greater than SMALLER_MODIFIED_DATE
        defaultBookShouldBeFound("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllBooksByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        book.setUser(user);
        bookRepository.saveAndFlush(book);
        Long userId = user.getId();

        // Get all the bookList where user equals to userId
        defaultBookShouldBeFound("userId.equals=" + userId);

        // Get all the bookList where user equals to (userId + 1)
        defaultBookShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByCatagoryIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        Catagory catagory = CatagoryResourceIT.createEntity(em);
        em.persist(catagory);
        em.flush();
        book.setCatagory(catagory);
        bookRepository.saveAndFlush(book);
        Long catagoryId = catagory.getId();

        // Get all the bookList where catagory equals to catagoryId
        defaultBookShouldBeFound("catagoryId.equals=" + catagoryId);

        // Get all the bookList where catagory equals to (catagoryId + 1)
        defaultBookShouldNotBeFound("catagoryId.equals=" + (catagoryId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByPublisherIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        Publisher publisher = PublisherResourceIT.createEntity(em);
        em.persist(publisher);
        em.flush();
        book.setPublisher(publisher);
        bookRepository.saveAndFlush(book);
        Long publisherId = publisher.getId();

        // Get all the bookList where publisher equals to publisherId
        defaultBookShouldBeFound("publisherId.equals=" + publisherId);

        // Get all the bookList where publisher equals to (publisherId + 1)
        defaultBookShouldNotBeFound("publisherId.equals=" + (publisherId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        Author author = AuthorResourceIT.createEntity(em);
        em.persist(author);
        em.flush();
        book.addAuthor(author);
        bookRepository.saveAndFlush(book);
        Long authorId = author.getId();

        // Get all the bookList where author equals to authorId
        defaultBookShouldBeFound("authorId.equals=" + authorId);

        // Get all the bookList where author equals to (authorId + 1)
        defaultBookShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByBookItemIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        BookItem bookItem = BookItemResourceIT.createEntity(em);
        em.persist(bookItem);
        em.flush();
        book.addBookItem(bookItem);
        bookRepository.saveAndFlush(book);
        Long bookItemId = bookItem.getId();

        // Get all the bookList where bookItem equals to bookItemId
        defaultBookShouldBeFound("bookItemId.equals=" + bookItemId);

        // Get all the bookList where bookItem equals to (bookItemId + 1)
        defaultBookShouldNotBeFound("bookItemId.equals=" + (bookItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookShouldBeFound(String filter) throws Exception {
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].numberOfPages").value(hasItem(DEFAULT_NUMBER_OF_PAGES)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookShouldNotBeFound(String filter) throws Exception {
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        Book updatedBook = bookRepository.findById(book.getId()).get();
        // Disconnect from session so that the updates on updatedBook are not directly saved in db
        em.detach(updatedBook);
        updatedBook
            .isbn(UPDATED_ISBN)
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .target(UPDATED_TARGET)
            .language(UPDATED_LANGUAGE)
            .numberOfPages(UPDATED_NUMBER_OF_PAGES)
            .imageUrl(UPDATED_IMAGE_URL)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBook.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testBook.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testBook.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testBook.getNumberOfPages()).isEqualTo(UPDATED_NUMBER_OF_PAGES);
        assertThat(testBook.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testBook.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, book.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookWithPatch() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book using partial update
        Book partialUpdatedBook = new Book();
        partialUpdatedBook.setId(book.getId());

        partialUpdatedBook
            .isbn(UPDATED_ISBN)
            .subject(UPDATED_SUBJECT)
            .numberOfPages(UPDATED_NUMBER_OF_PAGES)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testBook.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testBook.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testBook.getNumberOfPages()).isEqualTo(UPDATED_NUMBER_OF_PAGES);
        assertThat(testBook.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testBook.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateBookWithPatch() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book using partial update
        Book partialUpdatedBook = new Book();
        partialUpdatedBook.setId(book.getId());

        partialUpdatedBook
            .isbn(UPDATED_ISBN)
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .target(UPDATED_TARGET)
            .language(UPDATED_LANGUAGE)
            .numberOfPages(UPDATED_NUMBER_OF_PAGES)
            .imageUrl(UPDATED_IMAGE_URL)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBook.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testBook.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testBook.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testBook.getNumberOfPages()).isEqualTo(UPDATED_NUMBER_OF_PAGES);
        assertThat(testBook.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testBook.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, book.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(book)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Delete the book
        restBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, book.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
