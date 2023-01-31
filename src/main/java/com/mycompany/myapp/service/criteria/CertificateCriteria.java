package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Certificate} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CertificateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /certificates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CertificateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imgUrl;

    private StringFilter description;

    private LongFilter certificateTypeId;

    private LongFilter itemId;

    private Boolean distinct;

    public CertificateCriteria() {}

    public CertificateCriteria(CertificateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imgUrl = other.imgUrl == null ? null : other.imgUrl.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.certificateTypeId = other.certificateTypeId == null ? null : other.certificateTypeId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CertificateCriteria copy() {
        return new CertificateCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getImgUrl() {
        return imgUrl;
    }

    public StringFilter imgUrl() {
        if (imgUrl == null) {
            imgUrl = new StringFilter();
        }
        return imgUrl;
    }

    public void setImgUrl(StringFilter imgUrl) {
        this.imgUrl = imgUrl;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getCertificateTypeId() {
        return certificateTypeId;
    }

    public LongFilter certificateTypeId() {
        if (certificateTypeId == null) {
            certificateTypeId = new LongFilter();
        }
        return certificateTypeId;
    }

    public void setCertificateTypeId(LongFilter certificateTypeId) {
        this.certificateTypeId = certificateTypeId;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public LongFilter itemId() {
        if (itemId == null) {
            itemId = new LongFilter();
        }
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CertificateCriteria that = (CertificateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imgUrl, that.imgUrl) &&
            Objects.equals(description, that.description) &&
            Objects.equals(certificateTypeId, that.certificateTypeId) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imgUrl, description, certificateTypeId, itemId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CertificateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imgUrl != null ? "imgUrl=" + imgUrl + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (certificateTypeId != null ? "certificateTypeId=" + certificateTypeId + ", " : "") +
            (itemId != null ? "itemId=" + itemId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
