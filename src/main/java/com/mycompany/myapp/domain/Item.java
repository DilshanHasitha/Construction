package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_price", precision = 21, scale = 2)
    private BigDecimal itemPrice;

    @Column(name = "item_cost", precision = 21, scale = 2)
    private BigDecimal itemCost;

    @Column(name = "banner_text")
    private String bannerText;

    @Column(name = "special_price", precision = 21, scale = 2)
    private BigDecimal specialPrice;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "min_qty")
    private Double minQTY;

    @Column(name = "max_qty")
    private Double maxQTY;

    @Column(name = "steps")
    private Double steps;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "reorder_qty")
    private Double reorderQty;

    @Column(name = "item_barcode")
    private String itemBarcode;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exUser" }, allowSetters = true)
    private MasterItem masterItem;

    @ManyToOne
    private UnitOfMeasure unit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "userRole", "company" }, allowSetters = true)
    private ExUser exUser;

    @ManyToMany
    @JoinTable(name = "rel_item__rating", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "rating_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ratingType", "exUsers" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_item__certificate",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "certificate_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "certificateType", "items" }, allowSetters = true)
    private Set<Certificate> certificates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getItemPrice() {
        return this.itemPrice;
    }

    public Item itemPrice(BigDecimal itemPrice) {
        this.setItemPrice(itemPrice);
        return this;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemCost() {
        return this.itemCost;
    }

    public Item itemCost(BigDecimal itemCost) {
        this.setItemCost(itemCost);
        return this;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
    }

    public String getBannerText() {
        return this.bannerText;
    }

    public Item bannerText(String bannerText) {
        this.setBannerText(bannerText);
        return this;
    }

    public void setBannerText(String bannerText) {
        this.bannerText = bannerText;
    }

    public BigDecimal getSpecialPrice() {
        return this.specialPrice;
    }

    public Item specialPrice(BigDecimal specialPrice) {
        this.setSpecialPrice(specialPrice);
        return this;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Item isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getMinQTY() {
        return this.minQTY;
    }

    public Item minQTY(Double minQTY) {
        this.setMinQTY(minQTY);
        return this;
    }

    public void setMinQTY(Double minQTY) {
        this.minQTY = minQTY;
    }

    public Double getMaxQTY() {
        return this.maxQTY;
    }

    public Item maxQTY(Double maxQTY) {
        this.setMaxQTY(maxQTY);
        return this;
    }

    public void setMaxQTY(Double maxQTY) {
        this.maxQTY = maxQTY;
    }

    public Double getSteps() {
        return this.steps;
    }

    public Item steps(Double steps) {
        this.setSteps(steps);
        return this;
    }

    public void setSteps(Double steps) {
        this.steps = steps;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public Item longDescription(String longDescription) {
        this.setLongDescription(longDescription);
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Integer getLeadTime() {
        return this.leadTime;
    }

    public Item leadTime(Integer leadTime) {
        this.setLeadTime(leadTime);
        return this;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Double getReorderQty() {
        return this.reorderQty;
    }

    public Item reorderQty(Double reorderQty) {
        this.setReorderQty(reorderQty);
        return this;
    }

    public void setReorderQty(Double reorderQty) {
        this.reorderQty = reorderQty;
    }

    public String getItemBarcode() {
        return this.itemBarcode;
    }

    public Item itemBarcode(String itemBarcode) {
        this.setItemBarcode(itemBarcode);
        return this;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public MasterItem getMasterItem() {
        return this.masterItem;
    }

    public void setMasterItem(MasterItem masterItem) {
        this.masterItem = masterItem;
    }

    public Item masterItem(MasterItem masterItem) {
        this.setMasterItem(masterItem);
        return this;
    }

    public UnitOfMeasure getUnit() {
        return this.unit;
    }

    public void setUnit(UnitOfMeasure unitOfMeasure) {
        this.unit = unitOfMeasure;
    }

    public Item unit(UnitOfMeasure unitOfMeasure) {
        this.setUnit(unitOfMeasure);
        return this;
    }

    public ExUser getExUser() {
        return this.exUser;
    }

    public void setExUser(ExUser exUser) {
        this.exUser = exUser;
    }

    public Item exUser(ExUser exUser) {
        this.setExUser(exUser);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Item ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Item addRating(Rating rating) {
        this.ratings.add(rating);
        rating.getExUsers().add(this);
        return this;
    }

    public Item removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.getExUsers().remove(this);
        return this;
    }

    public Set<Certificate> getCertificates() {
        return this.certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }

    public Item certificates(Set<Certificate> certificates) {
        this.setCertificates(certificates);
        return this;
    }

    public Item addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.getItems().add(this);
        return this;
    }

    public Item removeCertificate(Certificate certificate) {
        this.certificates.remove(certificate);
        certificate.getItems().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", itemPrice=" + getItemPrice() +
            ", itemCost=" + getItemCost() +
            ", bannerText='" + getBannerText() + "'" +
            ", specialPrice=" + getSpecialPrice() +
            ", isActive='" + getIsActive() + "'" +
            ", minQTY=" + getMinQTY() +
            ", maxQTY=" + getMaxQTY() +
            ", steps=" + getSteps() +
            ", longDescription='" + getLongDescription() + "'" +
            ", leadTime=" + getLeadTime() +
            ", reorderQty=" + getReorderQty() +
            ", itemBarcode='" + getItemBarcode() + "'" +
            "}";
    }
}
