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
 * A BOQDetails.
 */
@Entity
@Table(name = "boq_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BOQDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "order_placed_on")
    private LocalDate orderPlacedOn;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exUser" }, allowSetters = true)
    private MasterItem item;

    @ManyToOne
    private UnitOfMeasure per;

    @ManyToOne
    private UnitOfMeasure unit;

    @ManyToMany(mappedBy = "boqDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "constructors", "boqDetails" }, allowSetters = true)
    private Set<BOQs> boqs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BOQDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public BOQDetails code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getOrderPlacedOn() {
        return this.orderPlacedOn;
    }

    public BOQDetails orderPlacedOn(LocalDate orderPlacedOn) {
        this.setOrderPlacedOn(orderPlacedOn);
        return this;
    }

    public void setOrderPlacedOn(LocalDate orderPlacedOn) {
        this.orderPlacedOn = orderPlacedOn;
    }

    public Double getQty() {
        return this.qty;
    }

    public BOQDetails qty(Double qty) {
        this.setQty(qty);
        return this;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BOQDetails isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public MasterItem getItem() {
        return this.item;
    }

    public void setItem(MasterItem masterItem) {
        this.item = masterItem;
    }

    public BOQDetails item(MasterItem masterItem) {
        this.setItem(masterItem);
        return this;
    }

    public UnitOfMeasure getPer() {
        return this.per;
    }

    public void setPer(UnitOfMeasure unitOfMeasure) {
        this.per = unitOfMeasure;
    }

    public BOQDetails per(UnitOfMeasure unitOfMeasure) {
        this.setPer(unitOfMeasure);
        return this;
    }

    public UnitOfMeasure getUnit() {
        return this.unit;
    }

    public void setUnit(UnitOfMeasure unitOfMeasure) {
        this.unit = unitOfMeasure;
    }

    public BOQDetails unit(UnitOfMeasure unitOfMeasure) {
        this.setUnit(unitOfMeasure);
        return this;
    }

    public Set<BOQs> getBoqs() {
        return this.boqs;
    }

    public void setBoqs(Set<BOQs> bOQs) {
        if (this.boqs != null) {
            this.boqs.forEach(i -> i.removeBoqDetails(this));
        }
        if (bOQs != null) {
            bOQs.forEach(i -> i.addBoqDetails(this));
        }
        this.boqs = bOQs;
    }

    public BOQDetails boqs(Set<BOQs> bOQs) {
        this.setBoqs(bOQs);
        return this;
    }

    public BOQDetails addBoqs(BOQs bOQs) {
        this.boqs.add(bOQs);
        bOQs.getBoqDetails().add(this);
        return this;
    }

    public BOQDetails removeBoqs(BOQs bOQs) {
        this.boqs.remove(bOQs);
        bOQs.getBoqDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BOQDetails)) {
            return false;
        }
        return id != null && id.equals(((BOQDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BOQDetails{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", orderPlacedOn='" + getOrderPlacedOn() + "'" +
            ", qty=" + getQty() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
