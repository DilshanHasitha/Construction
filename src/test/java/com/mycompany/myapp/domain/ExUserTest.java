package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExUser.class);
        ExUser exUser1 = new ExUser();
        exUser1.setId(1L);
        ExUser exUser2 = new ExUser();
        exUser2.setId(exUser1.getId());
        assertThat(exUser1).isEqualTo(exUser2);
        exUser2.setId(2L);
        assertThat(exUser1).isNotEqualTo(exUser2);
        exUser1.setId(null);
        assertThat(exUser1).isNotEqualTo(exUser2);
    }
}
