package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.UnitOfMeasure} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UnitOfMeasureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /unit-of-measures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasureCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter unitOfMeasureCode;

    private StringFilter unitOfMeasureDescription;

    private BooleanFilter isActive;

    private Boolean distinct;

    public UnitOfMeasureCriteria() {}

    public UnitOfMeasureCriteria(UnitOfMeasureCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.unitOfMeasureCode = other.unitOfMeasureCode == null ? null : other.unitOfMeasureCode.copy();
        this.unitOfMeasureDescription = other.unitOfMeasureDescription == null ? null : other.unitOfMeasureDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UnitOfMeasureCriteria copy() {
        return new UnitOfMeasureCriteria(this);
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

    public StringFilter getUnitOfMeasureCode() {
        return unitOfMeasureCode;
    }

    public StringFilter unitOfMeasureCode() {
        if (unitOfMeasureCode == null) {
            unitOfMeasureCode = new StringFilter();
        }
        return unitOfMeasureCode;
    }

    public void setUnitOfMeasureCode(StringFilter unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public StringFilter getUnitOfMeasureDescription() {
        return unitOfMeasureDescription;
    }

    public StringFilter unitOfMeasureDescription() {
        if (unitOfMeasureDescription == null) {
            unitOfMeasureDescription = new StringFilter();
        }
        return unitOfMeasureDescription;
    }

    public void setUnitOfMeasureDescription(StringFilter unitOfMeasureDescription) {
        this.unitOfMeasureDescription = unitOfMeasureDescription;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        final UnitOfMeasureCriteria that = (UnitOfMeasureCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(unitOfMeasureCode, that.unitOfMeasureCode) &&
            Objects.equals(unitOfMeasureDescription, that.unitOfMeasureDescription) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitOfMeasureCode, unitOfMeasureDescription, isActive, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasureCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (unitOfMeasureCode != null ? "unitOfMeasureCode=" + unitOfMeasureCode + ", " : "") +
            (unitOfMeasureDescription != null ? "unitOfMeasureDescription=" + unitOfMeasureDescription + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
