package com.vnpt.bookmanage.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vnpt.bookmanage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookLendingDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookLendingDetails.class);
        BookLendingDetails bookLendingDetails1 = new BookLendingDetails();
        bookLendingDetails1.setId(1L);
        BookLendingDetails bookLendingDetails2 = new BookLendingDetails();
        bookLendingDetails2.setId(bookLendingDetails1.getId());
        assertThat(bookLendingDetails1).isEqualTo(bookLendingDetails2);
        bookLendingDetails2.setId(2L);
        assertThat(bookLendingDetails1).isNotEqualTo(bookLendingDetails2);
        bookLendingDetails1.setId(null);
        assertThat(bookLendingDetails1).isNotEqualTo(bookLendingDetails2);
    }
}
