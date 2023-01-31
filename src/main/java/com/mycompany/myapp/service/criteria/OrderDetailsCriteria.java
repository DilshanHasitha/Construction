package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.OrderDetails} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.OrderDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter orderQty;

    private BigDecimalFilter revisedItemSalesPrice;

    private StringFilter note;

    private LongFilter itemId;

    private LongFilter ordersId;

    private Boolean distinct;

    public OrderDetailsCriteria() {}

    public OrderDetailsCriteria(OrderDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderQty = other.orderQty == null ? null : other.orderQty.copy();
        this.revisedItemSalesPrice = other.revisedItemSalesPrice == null ? null : other.revisedItemSalesPrice.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
        this.ordersId = other.ordersId == null ? null : other.ordersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrderDetailsCriteria copy() {
        return new OrderDetailsCriteria(this);
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

    public DoubleFilter getOrderQty() {
        return orderQty;
    }

    public DoubleFilter orderQty() {
        if (orderQty == null) {
            orderQty = new DoubleFilter();
        }
        return orderQty;
    }

    public void setOrderQty(DoubleFilter orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimalFilter getRevisedItemSalesPrice() {
        return revisedItemSalesPrice;
    }

    public BigDecimalFilter revisedItemSalesPrice() {
        if (revisedItemSalesPrice == null) {
            revisedItemSalesPrice = new BigDecimalFilter();
        }
        return revisedItemSalesPrice;
    }

    public void setRevisedItemSalesPrice(BigDecimalFilter revisedItemSalesPrice) {
        this.revisedItemSalesPrice = revisedItemSalesPrice;
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

    public LongFilter getOrdersId() {
        return ordersId;
    }

    public LongFilter ordersId() {
        if (ordersId == null) {
            ordersId = new LongFilter();
        }
        return ordersId;
    }

    public void setOrdersId(LongFilter ordersId) {
        this.ordersId = ordersId;
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
        final OrderDetailsCriteria that = (OrderDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderQty, that.orderQty) &&
            Objects.equals(revisedItemSalesPrice, that.revisedItemSalesPrice) &&
            Objects.equals(note, that.note) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderQty, revisedItemSalesPrice, note, itemId, ordersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderQty != null ? "orderQty=" + orderQty + ", " : "") +
            (revisedItemSalesPrice != null ? "revisedItemSalesPrice=" + revisedItemSalesPrice + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (itemId != null ? "itemId=" + itemId + ", " : "") +
            (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
