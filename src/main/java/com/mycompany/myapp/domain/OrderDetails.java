package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderDetails.
 */
@Entity
@Table(name = "order_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_qty", nullable = false)
    private Double orderQty;

    @Column(name = "revised_item_sales_price", precision = 21, scale = 2)
    private BigDecimal revisedItemSalesPrice;

    @Column(name = "note")
    private String note;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "masterItem", "unit", "exUser", "ratings", "certificates" }, allowSetters = true)
    private Item item;

    @ManyToMany(mappedBy = "orderDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exUser", "orderStatus", "orderDetails" }, allowSetters = true)
    private Set<Orders> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOrderQty() {
        return this.orderQty;
    }

    public OrderDetails orderQty(Double orderQty) {
        this.setOrderQty(orderQty);
        return this;
    }

    public void setOrderQty(Double orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getRevisedItemSalesPrice() {
        return this.revisedItemSalesPrice;
    }

    public OrderDetails revisedItemSalesPrice(BigDecimal revisedItemSalesPrice) {
        this.setRevisedItemSalesPrice(revisedItemSalesPrice);
        return this;
    }

    public void setRevisedItemSalesPrice(BigDecimal revisedItemSalesPrice) {
        this.revisedItemSalesPrice = revisedItemSalesPrice;
    }

    public String getNote() {
        return this.note;
    }

    public OrderDetails note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public OrderDetails item(Item item) {
        this.setItem(item);
        return this;
    }

    public Set<Orders> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Orders> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.removeOrderDetails(this));
        }
        if (orders != null) {
            orders.forEach(i -> i.addOrderDetails(this));
        }
        this.orders = orders;
    }

    public OrderDetails orders(Set<Orders> orders) {
        this.setOrders(orders);
        return this;
    }

    public OrderDetails addOrders(Orders orders) {
        this.orders.add(orders);
        orders.getOrderDetails().add(this);
        return this;
    }

    public OrderDetails removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.getOrderDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDetails)) {
            return false;
        }
        return id != null && id.equals(((OrderDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDetails{" +
            "id=" + getId() +
            ", orderQty=" + getOrderQty() +
            ", revisedItemSalesPrice=" + getRevisedItemSalesPrice() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
