package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Orders} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.OrdersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter orderID;

    private StringFilter customerName;

    private BooleanFilter isActive;

    private LocalDateFilter orderPlacedOn;

    private StringFilter note;

    private LongFilter exUserId;

    private LongFilter orderStatusId;

    private LongFilter orderDetailsId;

    private Boolean distinct;

    public OrdersCriteria() {}

    public OrdersCriteria(OrdersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderID = other.orderID == null ? null : other.orderID.copy();
        this.customerName = other.customerName == null ? null : other.customerName.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.orderPlacedOn = other.orderPlacedOn == null ? null : other.orderPlacedOn.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.exUserId = other.exUserId == null ? null : other.exUserId.copy();
        this.orderStatusId = other.orderStatusId == null ? null : other.orderStatusId.copy();
        this.orderDetailsId = other.orderDetailsId == null ? null : other.orderDetailsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrdersCriteria copy() {
        return new OrdersCriteria(this);
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

    public StringFilter getOrderID() {
        return orderID;
    }

    public StringFilter orderID() {
        if (orderID == null) {
            orderID = new StringFilter();
        }
        return orderID;
    }

    public void setOrderID(StringFilter orderID) {
        this.orderID = orderID;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public StringFilter customerName() {
        if (customerName == null) {
            customerName = new StringFilter();
        }
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
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

    public StringFilter getNote() {
        return note;
    }

    public StringFilter note() {
        if (note == null) {
            note = new StringFilter();
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
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

    public LongFilter getOrderStatusId() {
        return orderStatusId;
    }

    public LongFilter orderStatusId() {
        if (orderStatusId == null) {
            orderStatusId = new LongFilter();
        }
        return orderStatusId;
    }

    public void setOrderStatusId(LongFilter orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public LongFilter getOrderDetailsId() {
        return orderDetailsId;
    }

    public LongFilter orderDetailsId() {
        if (orderDetailsId == null) {
            orderDetailsId = new LongFilter();
        }
        return orderDetailsId;
    }

    public void setOrderDetailsId(LongFilter orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
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
        final OrdersCriteria that = (OrdersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderID, that.orderID) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(orderPlacedOn, that.orderPlacedOn) &&
            Objects.equals(note, that.note) &&
            Objects.equals(exUserId, that.exUserId) &&
            Objects.equals(orderStatusId, that.orderStatusId) &&
            Objects.equals(orderDetailsId, that.orderDetailsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderID, customerName, isActive, orderPlacedOn, note, exUserId, orderStatusId, orderDetailsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderID != null ? "orderID=" + orderID + ", " : "") +
            (customerName != null ? "customerName=" + customerName + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (orderPlacedOn != null ? "orderPlacedOn=" + orderPlacedOn + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (exUserId != null ? "exUserId=" + exUserId + ", " : "") +
            (orderStatusId != null ? "orderStatusId=" + orderStatusId + ", " : "") +
            (orderDetailsId != null ? "orderDetailsId=" + orderDetailsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
