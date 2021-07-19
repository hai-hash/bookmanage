package com.vnpt.bookmanage.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vnpt.bookmanage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookReservation.class);
        BookReservation bookReservation1 = new BookReservation();
        bookReservation1.setId(1L);
        BookReservation bookReservation2 = new BookReservation();
        bookReservation2.setId(bookReservation1.getId());
        assertThat(bookReservation1).isEqualTo(bookReservation2);
        bookReservation2.setId(2L);
        assertThat(bookReservation1).isNotEqualTo(bookReservation2);
        bookReservation1.setId(null);
        assertThat(bookReservation1).isNotEqualTo(bookReservation2);
    }
}
