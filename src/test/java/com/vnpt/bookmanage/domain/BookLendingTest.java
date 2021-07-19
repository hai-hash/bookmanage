package com.vnpt.bookmanage.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vnpt.bookmanage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookLendingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookLending.class);
        BookLending bookLending1 = new BookLending();
        bookLending1.setId(1L);
        BookLending bookLending2 = new BookLending();
        bookLending2.setId(bookLending1.getId());
        assertThat(bookLending1).isEqualTo(bookLending2);
        bookLending2.setId(2L);
        assertThat(bookLending1).isNotEqualTo(bookLending2);
        bookLending1.setId(null);
        assertThat(bookLending1).isNotEqualTo(bookLending2);
    }
}
