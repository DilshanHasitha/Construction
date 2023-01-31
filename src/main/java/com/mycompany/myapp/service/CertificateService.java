package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Certificate;
import com.mycompany.myapp.repository.CertificateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Certificate}.
 */
@Service
@Transactional
public class CertificateService {

    private final Logger log = LoggerFactory.getLogger(CertificateService.class);

    private final CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    /**
     * Save a certificate.
     *
     * @param certificate the entity to save.
     * @return the persisted entity.
     */
    public Certificate save(Certificate certificate) {
        log.debug("Request to save Certificate : {}", certificate);
        return certificateRepository.save(certificate);
    }

    /**
     * Update a certificate.
     *
     * @param certificate the entity to save.
     * @return the persisted entity.
     */
    public Certificate update(Certificate certificate) {
        log.debug("Request to update Certificate : {}", certificate);
        return certificateRepository.save(certificate);
    }

    /**
     * Partially update a certificate.
     *
     * @param certificate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Certificate> partialUpdate(Certificate certificate) {
        log.debug("Request to partially update Certificate : {}", certificate);

        return certificateRepository
            .findById(certificate.getId())
            .map(existingCertificate -> {
                if (certificate.getImgUrl() != null) {
                    existingCertificate.setImgUrl(certificate.getImgUrl());
                }
                if (certificate.getDescription() != null) {
                    existingCertificate.setDescription(certificate.getDescription());
                }

                return existingCertificate;
            })
            .map(certificateRepository::save);
    }

    /**
     * Get all the certificates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Certificate> findAll(Pageable pageable) {
        log.debug("Request to get all Certificates");
        return certificateRepository.findAll(pageable);
    }

    /**
     * Get all the certificates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Certificate> findAllWithEagerRelationships(Pageable pageable) {
        return certificateRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one certificate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Certificate> findOne(Long id) {
        log.debug("Request to get Certificate : {}", id);
        return certificateRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the certificate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Certificate : {}", id);
        certificateRepository.deleteById(id);
    }
}
