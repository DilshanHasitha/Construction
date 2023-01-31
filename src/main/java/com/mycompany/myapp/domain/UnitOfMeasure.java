package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UnitOfMeasure.
 */
@Entity
@Table(name = "unit_of_measure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "unit_of_measure_code", nullable = false)
    private String unitOfMeasureCode;

    @NotNull
    @Column(name = "unit_of_measure_description", nullable = false)
    private String unitOfMeasureDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitOfMeasure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitOfMeasureCode() {
        return this.unitOfMeasureCode;
    }

    public UnitOfMeasure unitOfMeasureCode(String unitOfMeasureCode) {
        this.setUnitOfMeasureCode(unitOfMeasureCode);
        return this;
    }

    public void setUnitOfMeasureCode(String unitOfMeasureCode) {
        this.unitOfMeasureCode = unitOfMeasureCode;
    }

    public String getUnitOfMeasureDescription() {
        return this.unitOfMeasureDescription;
    }

    public UnitOfMeasure unitOfMeasureDescription(String unitOfMeasureDescription) {
        this.setUnitOfMeasureDescription(unitOfMeasureDescription);
        return this;
    }

    public void setUnitOfMeasureDescription(String unitOfMeasureDescription) {
        this.unitOfMeasureDescription = unitOfMeasureDescription;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public UnitOfMeasure isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasure)) {
            return false;
        }
        return id != null && id.equals(((UnitOfMeasure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasure{" +
            "id=" + getId() +
            ", unitOfMeasureCode='" + getUnitOfMeasureCode() + "'" +
            ", unitOfMeasureDescription='" + getUnitOfMeasureDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
