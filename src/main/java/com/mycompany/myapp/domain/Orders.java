package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Orders.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private String orderID;

    @NotNull
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "order_placed_on")
    private LocalDate orderPlacedOn;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "userRole", "company" }, allowSetters = true)
    private ExUser exUser;

    @ManyToOne
    private OrderStatus orderStatus;

    @ManyToMany
    @JoinTable(
        name = "rel_orders__order_details",
        joinColumns = @JoinColumn(name = "orders_id"),
        inverseJoinColumns = @JoinColumn(name = "order_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "orders" }, allowSetters = true)
    private Set<OrderDetails> orderDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderID() {
        return this.orderID;
    }

    public Orders orderID(String orderID) {
        this.setOrderID(orderID);
        return this;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Orders customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Orders isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getOrderPlacedOn() {
        return this.orderPlacedOn;
    }

    public Orders orderPlacedOn(LocalDate orderPlacedOn) {
        this.setOrderPlacedOn(orderPlacedOn);
        return this;
    }

    public void setOrderPlacedOn(LocalDate orderPlacedOn) {
        this.orderPlacedOn = orderPlacedOn;
    }

    public String getNote() {
        return this.note;
    }

    public Orders note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ExUser getExUser() {
        return this.exUser;
    }

    public void setExUser(ExUser exUser) {
        this.exUser = exUser;
    }

    public Orders exUser(ExUser exUser) {
        this.setExUser(exUser);
        return this;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Orders orderStatus(OrderStatus orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public Set<OrderDetails> getOrderDetails() {
        return this.orderDetails;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Orders orderDetails(Set<OrderDetails> orderDetails) {
        this.setOrderDetails(orderDetails);
        return this;
    }

    public Orders addOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.add(orderDetails);
        orderDetails.getOrders().add(this);
        return this;
    }

    public Orders removeOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.remove(orderDetails);
        orderDetails.getOrders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orders)) {
            return false;
        }
        return id != null && id.equals(((Orders) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orders{" +
            "id=" + getId() +
            ", orderID='" + getOrderID() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", orderPlacedOn='" + getOrderPlacedOn() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
