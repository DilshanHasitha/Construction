package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderStatus.class);
        OrderStatus orderStatus1 = new OrderStatus();
        orderStatus1.setId(1L);
        OrderStatus orderStatus2 = new OrderStatus();
        orderStatus2.setId(orderStatus1.getId());
        assertThat(orderStatus1).isEqualTo(orderStatus2);
        orderStatus2.setId(2L);
        assertThat(orderStatus1).isNotEqualTo(orderStatus2);
        orderStatus1.setId(null);
        assertThat(orderStatus1).isNotEqualTo(orderStatus2);
    }
}
