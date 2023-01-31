package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Orders;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OrdersRepositoryWithBagRelationshipsImpl implements OrdersRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Orders> fetchBagRelationships(Optional<Orders> orders) {
        return orders.map(this::fetchOrderDetails);
    }

    @Override
    public Page<Orders> fetchBagRelationships(Page<Orders> orders) {
        return new PageImpl<>(fetchBagRelationships(orders.getContent()), orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public List<Orders> fetchBagRelationships(List<Orders> orders) {
        return Optional.of(orders).map(this::fetchOrderDetails).orElse(Collections.emptyList());
    }

    Orders fetchOrderDetails(Orders result) {
        return entityManager
            .createQuery("select orders from Orders orders left join fetch orders.orderDetails where orders is :orders", Orders.class)
            .setParameter("orders", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Orders> fetchOrderDetails(List<Orders> orders) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, orders.size()).forEach(index -> order.put(orders.get(index).getId(), index));
        List<Orders> result = entityManager
            .createQuery(
                "select distinct orders from Orders orders left join fetch orders.orderDetails where orders in :orders",
                Orders.class
            )
            .setParameter("orders", orders)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
