package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rating.
 */
@Entity
@Table(name = "rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rate_value")
    private Double rateValue;

    @ManyToOne
    private RatingType ratingType;

    @ManyToMany(mappedBy = "ratings")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "masterItem", "unit", "exUser", "ratings", "certificates" }, allowSetters = true)
    private Set<Item> exUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rating id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Rating name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRateValue() {
        return this.rateValue;
    }

    public Rating rateValue(Double rateValue) {
        this.setRateValue(rateValue);
        return this;
    }

    public void setRateValue(Double rateValue) {
        this.rateValue = rateValue;
    }

    public RatingType getRatingType() {
        return this.ratingType;
    }

    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    public Rating ratingType(RatingType ratingType) {
        this.setRatingType(ratingType);
        return this;
    }

    public Set<Item> getExUsers() {
        return this.exUsers;
    }

    public void setExUsers(Set<Item> items) {
        if (this.exUsers != null) {
            this.exUsers.forEach(i -> i.removeRating(this));
        }
        if (items != null) {
            items.forEach(i -> i.addRating(this));
        }
        this.exUsers = items;
    }

    public Rating exUsers(Set<Item> items) {
        this.setExUsers(items);
        return this;
    }

    public Rating addExUser(Item item) {
        this.exUsers.add(item);
        item.getRatings().add(this);
        return this;
    }

    public Rating removeExUser(Item item) {
        this.exUsers.remove(item);
        item.getRatings().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rating)) {
            return false;
        }
        return id != null && id.equals(((Rating) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rating{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rateValue=" + getRateValue() +
            "}";
    }
}
