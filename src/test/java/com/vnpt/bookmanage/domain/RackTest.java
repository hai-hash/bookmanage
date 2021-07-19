package com.vnpt.bookmanage.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.vnpt.bookmanage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rack.class);
        Rack rack1 = new Rack();
        rack1.setId(1L);
        Rack rack2 = new Rack();
        rack2.setId(rack1.getId());
        assertThat(rack1).isEqualTo(rack2);
        rack2.setId(2L);
        assertThat(rack1).isNotEqualTo(rack2);
        rack1.setId(null);
        assertThat(rack1).isNotEqualTo(rack2);
    }
}
