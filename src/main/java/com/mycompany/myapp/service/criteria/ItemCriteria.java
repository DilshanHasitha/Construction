package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Item} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter itemPrice;

    private BigDecimalFilter itemCost;

    private StringFilter bannerText;

    private BigDecimalFilter specialPrice;

    private BooleanFilter isActive;

    private DoubleFilter minQTY;

    private DoubleFilter maxQTY;

    private DoubleFilter steps;

    private StringFilter longDescription;

    private IntegerFilter leadTime;

    private DoubleFilter reorderQty;

    private StringFilter itemBarcode;

    private LongFilter masterItemId;

    private LongFilter unitId;

    private LongFilter exUserId;

    private LongFilter ratingId;

    private LongFilter certificateId;

    private Boolean distinct;

    public ItemCriteria() {}

    public ItemCriteria(ItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.itemPrice = other.itemPrice == null ? null : other.itemPrice.copy();
        this.itemCost = other.itemCost == null ? null : other.itemCost.copy();
        this.bannerText = other.bannerText == null ? null : other.bannerText.copy();
        this.specialPrice = other.specialPrice == null ? null : other.specialPrice.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.minQTY = other.minQTY == null ? null : other.minQTY.copy();
        this.maxQTY = other.maxQTY == null ? null : other.maxQTY.copy();
        this.steps = other.steps == null ? null : other.steps.copy();
        this.longDescription = other.longDescription == null ? null : other.longDescription.copy();
        this.leadTime = other.leadTime == null ? null : other.leadTime.copy();
        this.reorderQty = other.reorderQty == null ? null : other.reorderQty.copy();
        this.itemBarcode = other.itemBarcode == null ? null : other.itemBarcode.copy();
        this.masterItemId = other.masterItemId == null ? null : other.masterItemId.copy();
        this.unitId = other.unitId == null ? null : other.unitId.copy();
        this.exUserId = other.exUserId == null ? null : other.exUserId.copy();
        this.ratingId = other.ratingId == null ? null : other.ratingId.copy();
        this.certificateId = other.certificateId == null ? null : other.certificateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ItemCriteria copy() {
        return new ItemCriteria(this);
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

    public BigDecimalFilter getItemPrice() {
        return itemPrice;
    }

    public BigDecimalFilter itemPrice() {
        if (itemPrice == null) {
            itemPrice = new BigDecimalFilter();
        }
        return itemPrice;
    }

    public void setItemPrice(BigDecimalFilter itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimalFilter getItemCost() {
        return itemCost;
    }

    public BigDecimalFilter itemCost() {
        if (itemCost == null) {
            itemCost = new BigDecimalFilter();
        }
        return itemCost;
    }

    public void setItemCost(BigDecimalFilter itemCost) {
        this.itemCost = itemCost;
    }

    public StringFilter getBannerText() {
        return bannerText;
    }

    public StringFilter bannerText() {
        if (bannerText == null) {
            bannerText = new StringFilter();
        }
        return bannerText;
    }

    public void setBannerText(StringFilter bannerText) {
        this.bannerText = bannerText;
    }

    public BigDecimalFilter getSpecialPrice() {
        return specialPrice;
    }

    public BigDecimalFilter specialPrice() {
        if (specialPrice == null) {
            specialPrice = new BigDecimalFilter();
        }
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimalFilter specialPrice) {
        this.specialPrice = specialPrice;
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

    public DoubleFilter getMinQTY() {
        return minQTY;
    }

    public DoubleFilter minQTY() {
        if (minQTY == null) {
            minQTY = new DoubleFilter();
        }
        return minQTY;
    }

    public void setMinQTY(DoubleFilter minQTY) {
        this.minQTY = minQTY;
    }

    public DoubleFilter getMaxQTY() {
        return maxQTY;
    }

    public DoubleFilter maxQTY() {
        if (maxQTY == null) {
            maxQTY = new DoubleFilter();
        }
        return maxQTY;
    }

    public void setMaxQTY(DoubleFilter maxQTY) {
        this.maxQTY = maxQTY;
    }

    public DoubleFilter getSteps() {
        return steps;
    }

    public DoubleFilter steps() {
        if (steps == null) {
            steps = new DoubleFilter();
        }
        return steps;
    }

    public void setSteps(DoubleFilter steps) {
        this.steps = steps;
    }

    public StringFilter getLongDescription() {
        return longDescription;
    }

    public StringFilter longDescription() {
        if (longDescription == null) {
            longDescription = new StringFilter();
        }
        return longDescription;
    }

    public void setLongDescription(StringFilter longDescription) {
        this.longDescription = longDescription;
    }

    public IntegerFilter getLeadTime() {
        return leadTime;
    }

    public IntegerFilter leadTime() {
        if (leadTime == null) {
            leadTime = new IntegerFilter();
        }
        return leadTime;
    }

    public void setLeadTime(IntegerFilter leadTime) {
        this.leadTime = leadTime;
    }

    public DoubleFilter getReorderQty() {
        return reorderQty;
    }

    public DoubleFilter reorderQty() {
        if (reorderQty == null) {
            reorderQty = new DoubleFilter();
        }
        return reorderQty;
    }

    public void setReorderQty(DoubleFilter reorderQty) {
        this.reorderQty = reorderQty;
    }

    public StringFilter getItemBarcode() {
        return itemBarcode;
    }

    public StringFilter itemBarcode() {
        if (itemBarcode == null) {
            itemBarcode = new StringFilter();
        }
        return itemBarcode;
    }

    public void setItemBarcode(StringFilter itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public LongFilter getMasterItemId() {
        return masterItemId;
    }

    public LongFilter masterItemId() {
        if (masterItemId == null) {
            masterItemId = new LongFilter();
        }
        return masterItemId;
    }

    public void setMasterItemId(LongFilter masterItemId) {
        this.masterItemId = masterItemId;
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

    public LongFilter getRatingId() {
        return ratingId;
    }

    public LongFilter ratingId() {
        if (ratingId == null) {
            ratingId = new LongFilter();
        }
        return ratingId;
    }

    public void setRatingId(LongFilter ratingId) {
        this.ratingId = ratingId;
    }

    public LongFilter getCertificateId() {
        return certificateId;
    }

    public LongFilter certificateId() {
        if (certificateId == null) {
            certificateId = new LongFilter();
        }
        return certificateId;
    }

    public void setCertificateId(LongFilter certificateId) {
        this.certificateId = certificateId;
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
        final ItemCriteria that = (ItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(itemPrice, that.itemPrice) &&
            Objects.equals(itemCost, that.itemCost) &&
            Objects.equals(bannerText, that.bannerText) &&
            Objects.equals(specialPrice, that.specialPrice) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(minQTY, that.minQTY) &&
            Objects.equals(maxQTY, that.maxQTY) &&
            Objects.equals(steps, that.steps) &&
            Objects.equals(longDescription, that.longDescription) &&
            Objects.equals(leadTime, that.leadTime) &&
            Objects.equals(reorderQty, that.reorderQty) &&
            Objects.equals(itemBarcode, that.itemBarcode) &&
            Objects.equals(masterItemId, that.masterItemId) &&
            Objects.equals(unitId, that.unitId) &&
            Objects.equals(exUserId, that.exUserId) &&
            Objects.equals(ratingId, that.ratingId) &&
            Objects.equals(certificateId, that.certificateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            itemPrice,
            itemCost,
            bannerText,
            specialPrice,
            isActive,
            minQTY,
            maxQTY,
            steps,
            longDescription,
            leadTime,
            reorderQty,
            itemBarcode,
            masterItemId,
            unitId,
            exUserId,
            ratingId,
            certificateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (itemPrice != null ? "itemPrice=" + itemPrice + ", " : "") +
            (itemCost != null ? "itemCost=" + itemCost + ", " : "") +
            (bannerText != null ? "bannerText=" + bannerText + ", " : "") +
            (specialPrice != null ? "specialPrice=" + specialPrice + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (minQTY != null ? "minQTY=" + minQTY + ", " : "") +
            (maxQTY != null ? "maxQTY=" + maxQTY + ", " : "") +
            (steps != null ? "steps=" + steps + ", " : "") +
            (longDescription != null ? "longDescription=" + longDescription + ", " : "") +
            (leadTime != null ? "leadTime=" + leadTime + ", " : "") +
            (reorderQty != null ? "reorderQty=" + reorderQty + ", " : "") +
            (itemBarcode != null ? "itemBarcode=" + itemBarcode + ", " : "") +
            (masterItemId != null ? "masterItemId=" + masterItemId + ", " : "") +
            (unitId != null ? "unitId=" + unitId + ", " : "") +
            (exUserId != null ? "exUserId=" + exUserId + ", " : "") +
            (ratingId != null ? "ratingId=" + ratingId + ", " : "") +
            (certificateId != null ? "certificateId=" + certificateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
