package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CertificateType;
import com.mycompany.myapp.repository.CertificateTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CertificateType}.
 */
@Service
@Transactional
public class CertificateTypeService {

    private final Logger log = LoggerFactory.getLogger(CertificateTypeService.class);

    private final CertificateTypeRepository certificateTypeRepository;

    public CertificateTypeService(CertificateTypeRepository certificateTypeRepository) {
        this.certificateTypeRepository = certificateTypeRepository;
    }

    /**
     * Save a certificateType.
     *
     * @param certificateType the entity to save.
     * @return the persisted entity.
     */
    public CertificateType save(CertificateType certificateType) {
        log.debug("Request to save CertificateType : {}", certificateType);
        return certificateTypeRepository.save(certificateType);
    }

    /**
     * Update a certificateType.
     *
     * @param certificateType the entity to save.
     * @return the persisted entity.
     */
    public CertificateType update(CertificateType certificateType) {
        log.debug("Request to update CertificateType : {}", certificateType);
        return certificateTypeRepository.save(certificateType);
    }

    /**
     * Partially update a certificateType.
     *
     * @param certificateType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CertificateType> partialUpdate(CertificateType certificateType) {
        log.debug("Request to partially update CertificateType : {}", certificateType);

        return certificateTypeRepository
            .findById(certificateType.getId())
            .map(existingCertificateType -> {
                if (certificateType.getCode() != null) {
                    existingCertificateType.setCode(certificateType.getCode());
                }
                if (certificateType.getName() != null) {
                    existingCertificateType.setName(certificateType.getName());
                }
                if (certificateType.getIsActive() != null) {
                    existingCertificateType.setIsActive(certificateType.getIsActive());
                }

                return existingCertificateType;
            })
            .map(certificateTypeRepository::save);
    }

    /**
     * Get all the certificateTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CertificateType> findAll(Pageable pageable) {
        log.debug("Request to get all CertificateTypes");
        return certificateTypeRepository.findAll(pageable);
    }

    /**
     * Get one certificateType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CertificateType> findOne(Long id) {
        log.debug("Request to get CertificateType : {}", id);
        return certificateTypeRepository.findById(id);
    }

    /**
     * Delete the certificateType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CertificateType : {}", id);
        certificateTypeRepository.deleteById(id);
    }
}
