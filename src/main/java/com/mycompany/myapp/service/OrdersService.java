package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Orders;
import com.mycompany.myapp.repository.OrdersRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Orders}.
 */
@Service
@Transactional
public class OrdersService {

    private final Logger log = LoggerFactory.getLogger(OrdersService.class);

    private final OrdersRepository ordersRepository;

    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    /**
     * Save a orders.
     *
     * @param orders the entity to save.
     * @return the persisted entity.
     */
    public Orders save(Orders orders) {
        log.debug("Request to save Orders : {}", orders);
        return ordersRepository.save(orders);
    }

    /**
     * Update a orders.
     *
     * @param orders the entity to save.
     * @return the persisted entity.
     */
    public Orders update(Orders orders) {
        log.debug("Request to update Orders : {}", orders);
        return ordersRepository.save(orders);
    }

    /**
     * Partially update a orders.
     *
     * @param orders the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Orders> partialUpdate(Orders orders) {
        log.debug("Request to partially update Orders : {}", orders);

        return ordersRepository
            .findById(orders.getId())
            .map(existingOrders -> {
                if (orders.getOrderID() != null) {
                    existingOrders.setOrderID(orders.getOrderID());
                }
                if (orders.getCustomerName() != null) {
                    existingOrders.setCustomerName(orders.getCustomerName());
                }
                if (orders.getIsActive() != null) {
                    existingOrders.setIsActive(orders.getIsActive());
                }
                if (orders.getOrderPlacedOn() != null) {
                    existingOrders.setOrderPlacedOn(orders.getOrderPlacedOn());
                }
                if (orders.getNote() != null) {
                    existingOrders.setNote(orders.getNote());
                }

                return existingOrders;
            })
            .map(ordersRepository::save);
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Orders> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return ordersRepository.findAll(pageable);
    }

    /**
     * Get all the orders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Orders> findAllWithEagerRelationships(Pageable pageable) {
        return ordersRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orders by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Orders> findOne(Long id) {
        log.debug("Request to get Orders : {}", id);
        return ordersRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orders by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Orders : {}", id);
        ordersRepository.deleteById(id);
    }
}
