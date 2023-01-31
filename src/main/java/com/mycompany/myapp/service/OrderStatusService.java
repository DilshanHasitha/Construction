package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.OrderStatus;
import com.mycompany.myapp.repository.OrderStatusRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderStatus}.
 */
@Service
@Transactional
public class OrderStatusService {

    private final Logger log = LoggerFactory.getLogger(OrderStatusService.class);

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Save a orderStatus.
     *
     * @param orderStatus the entity to save.
     * @return the persisted entity.
     */
    public OrderStatus save(OrderStatus orderStatus) {
        log.debug("Request to save OrderStatus : {}", orderStatus);
        return orderStatusRepository.save(orderStatus);
    }

    /**
     * Update a orderStatus.
     *
     * @param orderStatus the entity to save.
     * @return the persisted entity.
     */
    public OrderStatus update(OrderStatus orderStatus) {
        log.debug("Request to update OrderStatus : {}", orderStatus);
        return orderStatusRepository.save(orderStatus);
    }

    /**
     * Partially update a orderStatus.
     *
     * @param orderStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderStatus> partialUpdate(OrderStatus orderStatus) {
        log.debug("Request to partially update OrderStatus : {}", orderStatus);

        return orderStatusRepository
            .findById(orderStatus.getId())
            .map(existingOrderStatus -> {
                if (orderStatus.getCode() != null) {
                    existingOrderStatus.setCode(orderStatus.getCode());
                }
                if (orderStatus.getDescription() != null) {
                    existingOrderStatus.setDescription(orderStatus.getDescription());
                }
                if (orderStatus.getIsActive() != null) {
                    existingOrderStatus.setIsActive(orderStatus.getIsActive());
                }

                return existingOrderStatus;
            })
            .map(orderStatusRepository::save);
    }

    /**
     * Get all the orderStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderStatus> findAll(Pageable pageable) {
        log.debug("Request to get all OrderStatuses");
        return orderStatusRepository.findAll(pageable);
    }

    /**
     * Get one orderStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderStatus> findOne(Long id) {
        log.debug("Request to get OrderStatus : {}", id);
        return orderStatusRepository.findById(id);
    }

    /**
     * Delete the orderStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderStatus : {}", id);
        orderStatusRepository.deleteById(id);
    }
}
