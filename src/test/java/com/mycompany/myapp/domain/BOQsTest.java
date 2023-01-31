package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BOQsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BOQs.class);
        BOQs bOQs1 = new BOQs();
        bOQs1.setId(1L);
        BOQs bOQs2 = new BOQs();
        bOQs2.setId(bOQs1.getId());
        assertThat(bOQs1).isEqualTo(bOQs2);
        bOQs2.setId(2L);
        assertThat(bOQs1).isNotEqualTo(bOQs2);
        bOQs1.setId(null);
        assertThat(bOQs1).isNotEqualTo(bOQs2);
    }
}
