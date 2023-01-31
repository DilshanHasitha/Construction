package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Rating} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.RatingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ratings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RatingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DoubleFilter rateValue;

    private LongFilter ratingTypeId;

    private LongFilter exUserId;

    private Boolean distinct;

    public RatingCriteria() {}

    public RatingCriteria(RatingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.rateValue = other.rateValue == null ? null : other.rateValue.copy();
        this.ratingTypeId = other.ratingTypeId == null ? null : other.ratingTypeId.copy();
        this.exUserId = other.exUserId == null ? null : other.exUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RatingCriteria copy() {
        return new RatingCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getRateValue() {
        return rateValue;
    }

    public DoubleFilter rateValue() {
        if (rateValue == null) {
            rateValue = new DoubleFilter();
        }
        return rateValue;
    }

    public void setRateValue(DoubleFilter rateValue) {
        this.rateValue = rateValue;
    }

    public LongFilter getRatingTypeId() {
        return ratingTypeId;
    }

    public LongFilter ratingTypeId() {
        if (ratingTypeId == null) {
            ratingTypeId = new LongFilter();
        }
        return ratingTypeId;
    }

    public void setRatingTypeId(LongFilter ratingTypeId) {
        this.ratingTypeId = ratingTypeId;
    }

    public LongFilter getExUserId() {
        return exUserId;
    }

    public LongFilter exUserId() {
        if (exUserId == null) {
            exUserId = new LongFilter();
        }
        return exUserId;
    }

    public void setExUserId(LongFilter exUserId) {
        this.exUserId = exUserId;
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
        final RatingCriteria that = (RatingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(rateValue, that.rateValue) &&
            Objects.equals(ratingTypeId, that.ratingTypeId) &&
            Objects.equals(exUserId, that.exUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rateValue, ratingTypeId, exUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RatingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (rateValue != null ? "rateValue=" + rateValue + ", " : "") +
            (ratingTypeId != null ? "ratingTypeId=" + ratingTypeId + ", " : "") +
            (exUserId != null ? "exUserId=" + exUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
