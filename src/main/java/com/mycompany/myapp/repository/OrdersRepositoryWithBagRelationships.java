package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Orders;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OrdersRepositoryWithBagRelationships {
    Optional<Orders> fetchBagRelationships(Optional<Orders> orders);

    List<Orders> fetchBagRelationships(List<Orders> orders);

    Page<Orders> fetchBagRelationships(Page<Orders> orders);
}
