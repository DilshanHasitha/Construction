package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RatingTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingType.class);
        RatingType ratingType1 = new RatingType();
        ratingType1.setId(1L);
        RatingType ratingType2 = new RatingType();
        ratingType2.setId(ratingType1.getId());
        assertThat(ratingType1).isEqualTo(ratingType2);
        ratingType2.setId(2L);
        assertThat(ratingType1).isNotEqualTo(ratingType2);
        ratingType1.setId(null);
        assertThat(ratingType1).isNotEqualTo(ratingType2);
    }
}
