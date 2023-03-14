package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BOQs.
 */
@Entity
@Table(name = "bo_qs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BOQs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "userRole", "company" }, allowSetters = true)
    private ExUser constructors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_bo_qs__boq_details",
        joinColumns = @JoinColumn(name = "bo_qs_id"),
        inverseJoinColumns = @JoinColumn(name = "boq_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "per", "unit", "boqs" }, allowSetters = true)
    private Set<BOQDetails> boqDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BOQs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public BOQs code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BOQs isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ExUser getConstructors() {
        return this.constructors;
    }

    public void setConstructors(ExUser exUser) {
        this.constructors = exUser;
    }

    public BOQs constructors(ExUser exUser) {
        this.setConstructors(exUser);
        return this;
    }

    public Set<BOQDetails> getBoqDetails() {
        return this.boqDetails;
    }

    public void setBoqDetails(Set<BOQDetails> bOQDetails) {
        this.boqDetails = bOQDetails;
    }

    public BOQs boqDetails(Set<BOQDetails> bOQDetails) {
        this.setBoqDetails(bOQDetails);
        return this;
    }

    public BOQs addBoqDetails(BOQDetails bOQDetails) {
        this.boqDetails.add(bOQDetails);
        bOQDetails.getBoqs().add(this);
        return this;
    }

    public BOQs removeBoqDetails(BOQDetails bOQDetails) {
        this.boqDetails.remove(bOQDetails);
        bOQDetails.getBoqs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BOQs)) {
            return false;
        }
        return id != null && id.equals(((BOQs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BOQs{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
