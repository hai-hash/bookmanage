package com.vnpt.bookmanage.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vnpt.bookmanage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reader.class);
        Reader reader1 = new Reader();
        reader1.setId(1L);
        Reader reader2 = new Reader();
        reader2.setId(reader1.getId());
        assertThat(reader1).isEqualTo(reader2);
        reader2.setId(2L);
        assertThat(reader1).isNotEqualTo(reader2);
        reader1.setId(null);
        assertThat(reader1).isNotEqualTo(reader2);
    }
}
