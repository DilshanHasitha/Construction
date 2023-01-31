package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.BOQs} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BOQsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bo-qs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BOQsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private BooleanFilter isActive;

    private LongFilter constructorsId;

    private LongFilter boqDetailsId;

    private Boolean distinct;

    public BOQsCriteria() {}

    public BOQsCriteria(BOQsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.constructorsId = other.constructorsId == null ? null : other.constructorsId.copy();
        this.boqDetailsId = other.boqDetailsId == null ? null : other.boqDetailsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BOQsCriteria copy() {
        return new BOQsCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public LongFilter getConstructorsId() {
        return constructorsId;
    }

    public LongFilter constructorsId() {
        if (constructorsId == null) {
            constructorsId = new LongFilter();
        }
        return constructorsId;
    }

    public void setConstructorsId(LongFilter constructorsId) {
        this.constructorsId = constructorsId;
    }

    public LongFilter getBoqDetailsId() {
        return boqDetailsId;
    }

    public LongFilter boqDetailsId() {
        if (boqDetailsId == null) {
            boqDetailsId = new LongFilter();
        }
        return boqDetailsId;
    }

    public void setBoqDetailsId(LongFilter boqDetailsId) {
        this.boqDetailsId = boqDetailsId;
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
        final BOQsCriteria that = (BOQsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(constructorsId, that.constructorsId) &&
            Objects.equals(boqDetailsId, that.boqDetailsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, isActive, constructorsId, boqDetailsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BOQsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (constructorsId != null ? "constructorsId=" + constructorsId + ", " : "") +
            (boqDetailsId != null ? "boqDetailsId=" + boqDetailsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
