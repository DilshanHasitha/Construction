package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Certificate;
import com.mycompany.myapp.repository.CertificateRepository;
import com.mycompany.myapp.service.criteria.CertificateCriteria;
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
 * Service for executing complex queries for {@link Certificate} entities in the database.
 * The main input is a {@link CertificateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Certificate} or a {@link Page} of {@link Certificate} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CertificateQueryService extends QueryService<Certificate> {

    private final Logger log = LoggerFactory.getLogger(CertificateQueryService.class);

    private final CertificateRepository certificateRepository;

    public CertificateQueryService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    /**
     * Return a {@link List} of {@link Certificate} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Certificate> findByCriteria(CertificateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Certificate> specification = createSpecification(criteria);
        return certificateRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Certificate} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Certificate> findByCriteria(CertificateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Certificate> specification = createSpecification(criteria);
        return certificateRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CertificateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Certificate> specification = createSpecification(criteria);
        return certificateRepository.count(specification);
    }

    /**
     * Function to convert {@link CertificateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Certificate> createSpecification(CertificateCriteria criteria) {
        Specification<Certificate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Certificate_.id));
            }
            if (criteria.getImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgUrl(), Certificate_.imgUrl));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Certificate_.description));
            }
            if (criteria.getCertificateTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCertificateTypeId(),
                            root -> root.join(Certificate_.certificateType, JoinType.LEFT).get(CertificateType_.id)
                        )
                    );
            }
            if (criteria.getItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getItemId(), root -> root.join(Certificate_.items, JoinType.LEFT).get(Item_.id))
                    );
            }
        }
        return specification;
    }
}
