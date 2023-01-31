package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterItem.class);
        MasterItem masterItem1 = new MasterItem();
        masterItem1.setId(1L);
        MasterItem masterItem2 = new MasterItem();
        masterItem2.setId(masterItem1.getId());
        assertThat(masterItem1).isEqualTo(masterItem2);
        masterItem2.setId(2L);
        assertThat(masterItem1).isNotEqualTo(masterItem2);
        masterItem1.setId(null);
        assertThat(masterItem1).isNotEqualTo(masterItem2);
    }
}
