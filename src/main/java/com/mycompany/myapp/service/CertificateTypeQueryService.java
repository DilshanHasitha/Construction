package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CertificateType;
import com.mycompany.myapp.repository.CertificateTypeRepository;
import com.mycompany.myapp.service.criteria.CertificateTypeCriteria;
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
 * Service for executing complex queries for {@link CertificateType} entities in the database.
 * The main input is a {@link CertificateTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CertificateType} or a {@link Page} of {@link CertificateType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CertificateTypeQueryService extends QueryService<CertificateType> {

    private final Logger log = LoggerFactory.getLogger(CertificateTypeQueryService.class);

    private final CertificateTypeRepository certificateTypeRepository;

    public CertificateTypeQueryService(CertificateTypeRepository certificateTypeRepository) {
        this.certificateTypeRepository = certificateTypeRepository;
    }

    /**
     * Return a {@link List} of {@link CertificateType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CertificateType> findByCriteria(CertificateTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CertificateType> specification = createSpecification(criteria);
        return certificateTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CertificateType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CertificateType> findByCriteria(CertificateTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CertificateType> specification = createSpecification(criteria);
        return certificateTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CertificateTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CertificateType> specification = createSpecification(criteria);
        return certificateTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CertificateTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CertificateType> createSpecification(CertificateTypeCriteria criteria) {
        Specification<CertificateType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CertificateType_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), CertificateType_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CertificateType_.name));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), CertificateType_.isActive));
            }
        }
        return specification;
    }
}
