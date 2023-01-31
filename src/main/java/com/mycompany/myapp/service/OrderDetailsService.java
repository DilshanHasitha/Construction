package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.OrderDetails;
import com.mycompany.myapp.repository.OrderDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderDetails}.
 */
@Service
@Transactional
public class OrderDetailsService {

    private final Logger log = LoggerFactory.getLogger(OrderDetailsService.class);

    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

    /**
     * Save a orderDetails.
     *
     * @param orderDetails the entity to save.
     * @return the persisted entity.
     */
    public OrderDetails save(OrderDetails orderDetails) {
        log.debug("Request to save OrderDetails : {}", orderDetails);
        return orderDetailsRepository.save(orderDetails);
    }

    /**
     * Update a orderDetails.
     *
     * @param orderDetails the entity to save.
     * @return the persisted entity.
     */
    public OrderDetails update(OrderDetails orderDetails) {
        log.debug("Request to update OrderDetails : {}", orderDetails);
        return orderDetailsRepository.save(orderDetails);
    }

    /**
     * Partially update a orderDetails.
     *
     * @param orderDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderDetails> partialUpdate(OrderDetails orderDetails) {
        log.debug("Request to partially update OrderDetails : {}", orderDetails);

        return orderDetailsRepository
            .findById(orderDetails.getId())
            .map(existingOrderDetails -> {
                if (orderDetails.getOrderQty() != null) {
                    existingOrderDetails.setOrderQty(orderDetails.getOrderQty());
                }
                if (orderDetails.getRevisedItemSalesPrice() != null) {
                    existingOrderDetails.setRevisedItemSalesPrice(orderDetails.getRevisedItemSalesPrice());
                }
                if (orderDetails.getNote() != null) {
                    existingOrderDetails.setNote(orderDetails.getNote());
                }

                return existingOrderDetails;
            })
            .map(orderDetailsRepository::save);
    }

    /**
     * Get all the orderDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDetails> findAll(Pageable pageable) {
        log.debug("Request to get all OrderDetails");
        return orderDetailsRepository.findAll(pageable);
    }

    /**
     * Get one orderDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderDetails> findOne(Long id) {
        log.debug("Request to get OrderDetails : {}", id);
        return orderDetailsRepository.findById(id);
    }

    /**
     * Delete the orderDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderDetails : {}", id);
        orderDetailsRepository.deleteById(id);
    }
}
