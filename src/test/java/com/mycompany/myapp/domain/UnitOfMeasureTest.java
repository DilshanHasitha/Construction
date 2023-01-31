package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitOfMeasureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasure.class);
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setId(1L);
        UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure2.setId(unitOfMeasure1.getId());
        assertThat(unitOfMeasure1).isEqualTo(unitOfMeasure2);
        unitOfMeasure2.setId(2L);
        assertThat(unitOfMeasure1).isNotEqualTo(unitOfMeasure2);
        unitOfMeasure1.setId(null);
        assertThat(unitOfMeasure1).isNotEqualTo(unitOfMeasure2);
    }
}
