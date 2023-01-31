package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.OrderStatus;
import com.mycompany.myapp.repository.OrderStatusRepository;
import com.mycompany.myapp.service.criteria.OrderStatusCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OrderStatus} entities in the database.
 * The main input is a {@link OrderStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderStatus} or a {@link Page} of {@link OrderStatus} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderStatusQueryService extends QueryService<OrderStatus> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusQueryService.class);

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusQueryService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Return a {@link List} of {@link OrderStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderStatus> findByCriteria(OrderStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderStatus> specification = createSpecification(criteria);
        return orderStatusRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrderStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderStatus> findByCriteria(OrderStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderStatus> specification = createSpecification(criteria);
        return orderStatusRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderStatus> specification = createSpecification(criteria);
        return orderStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderStatus> createSpecification(OrderStatusCriteria criteria) {
        Specification<OrderStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderStatus_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OrderStatus_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), OrderStatus_.description));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), OrderStatus_.isActive));
            }
        }
        return specification;
    }
}
