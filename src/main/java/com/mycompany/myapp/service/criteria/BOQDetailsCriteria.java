package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.BOQDetails} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BOQDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /boq-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BOQDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private LocalDateFilter orderPlacedOn;

    private DoubleFilter qty;

    private BooleanFilter isActive;

    private LongFilter itemId;

    private LongFilter perId;

    private LongFilter unitId;

    private LongFilter boqsId;

    private Boolean distinct;

    public BOQDetailsCriteria() {}

    public BOQDetailsCriteria(BOQDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.orderPlacedOn = other.orderPlacedOn == null ? null : other.orderPlacedOn.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.perId = other.perId == null ? null : other.perId.copy();
        this.unitId = other.unitId == null ? null : other.unitId.copy();
        this.boqsId = other.boqsId == null ? null : other.boqsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BOQDetailsCriteria copy() {
        return new BOQDetailsCriteria(this);
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

    public LocalDateFilter getOrderPlacedOn() {
        return orderPlacedOn;
    }

    public LocalDateFilter orderPlacedOn() {
        if (orderPlacedOn == null) {
            orderPlacedOn = new LocalDateFilter();
        }
        return orderPlacedOn;
    }

    public void setOrderPlacedOn(LocalDateFilter orderPlacedOn) {
        this.orderPlacedOn = orderPlacedOn;
    }

    public DoubleFilter getQty() {
        return qty;
    }

    public DoubleFilter qty() {
        if (qty == null) {
            qty = new DoubleFilter();
        }
        return qty;
    }

    public void setQty(DoubleFilter qty) {
        this.qty = qty;
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

    public LongFilter getPerId() {
        return perId;
    }

    public LongFilter perId() {
        if (perId == null) {
            perId = new LongFilter();
        }
        return perId;
    }

    public void setPerId(LongFilter perId) {
        this.perId = perId;
    }

    public LongFilter getUnitId() {
        return unitId;
    }

    public LongFilter unitId() {
        if (unitId == null) {
            unitId = new LongFilter();
        }
        return unitId;
    }

    public void setUnitId(LongFilter unitId) {
        this.unitId = unitId;
    }

    public LongFilter getBoqsId() {
        return boqsId;
    }

    public LongFilter boqsId() {
        if (boqsId == null) {
            boqsId = new LongFilter();
        }
        return boqsId;
    }

    public void setBoqsId(LongFilter boqsId) {
        this.boqsId = boqsId;
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
        final BOQDetailsCriteria that = (BOQDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(orderPlacedOn, that.orderPlacedOn) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(perId, that.perId) &&
            Objects.equals(unitId, that.unitId) &&
            Objects.equals(boqsId, that.boqsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, orderPlacedOn, qty, isActive, itemId, perId, unitId, boqsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BOQDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (orderPlacedOn != null ? "orderPlacedOn=" + orderPlacedOn + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (itemId != null ? "itemId=" + itemId + ", " : "") +
            (perId != null ? "perId=" + perId + ", " : "") +
            (unitId != null ? "unitId=" + unitId + ", " : "") +
            (boqsId != null ? "boqsId=" + boqsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
