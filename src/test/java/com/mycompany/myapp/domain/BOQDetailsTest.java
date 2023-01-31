package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BOQDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BOQDetails.class);
        BOQDetails bOQDetails1 = new BOQDetails();
        bOQDetails1.setId(1L);
        BOQDetails bOQDetails2 = new BOQDetails();
        bOQDetails2.setId(bOQDetails1.getId());
        assertThat(bOQDetails1).isEqualTo(bOQDetails2);
        bOQDetails2.setId(2L);
        assertThat(bOQDetails1).isNotEqualTo(bOQDetails2);
        bOQDetails1.setId(null);
        assertThat(bOQDetails1).isNotEqualTo(bOQDetails2);
    }
}
