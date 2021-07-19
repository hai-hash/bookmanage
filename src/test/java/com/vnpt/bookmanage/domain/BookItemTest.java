package com.vnpt.bookmanage.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vnpt.bookmanage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookItem.class);
        BookItem bookItem1 = new BookItem();
        bookItem1.setId(1L);
        BookItem bookItem2 = new BookItem();
        bookItem2.setId(bookItem1.getId());
        assertThat(bookItem1).isEqualTo(bookItem2);
        bookItem2.setId(2L);
        assertThat(bookItem1).isNotEqualTo(bookItem2);
        bookItem1.setId(null);
        assertThat(bookItem1).isNotEqualTo(bookItem2);
    }
}
