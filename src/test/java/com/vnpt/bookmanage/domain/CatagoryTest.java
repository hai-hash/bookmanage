package com.vnpt.bookmanage.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vnpt.bookmanage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatagoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catagory.class);
        Catagory catagory1 = new Catagory();
        catagory1.setId(1L);
        Catagory catagory2 = new Catagory();
        catagory2.setId(catagory1.getId());
        assertThat(catagory1).isEqualTo(catagory2);
        catagory2.setId(2L);
        assertThat(catagory1).isNotEqualTo(catagory2);
        catagory1.setId(null);
        assertThat(catagory1).isNotEqualTo(catagory2);
    }
}
