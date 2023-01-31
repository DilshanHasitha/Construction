package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Project} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private BooleanFilter isActive;

    private StringFilter description;

    private LocalDateFilter completionDate;

    private StringFilter regNumber;

    private StringFilter notes;

    private StringFilter address;

    private LongFilter locationId;

    private LongFilter exUserId;

    private Boolean distinct;

    public ProjectCriteria() {}

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.completionDate = other.completionDate == null ? null : other.completionDate.copy();
        this.regNumber = other.regNumber == null ? null : other.regNumber.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.exUserId = other.exUserId == null ? null : other.exUserId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getCompletionDate() {
        return completionDate;
    }

    public LocalDateFilter completionDate() {
        if (completionDate == null) {
            completionDate = new LocalDateFilter();
        }
        return completionDate;
    }

    public void setCompletionDate(LocalDateFilter completionDate) {
        this.completionDate = completionDate;
    }

    public StringFilter getRegNumber() {
        return regNumber;
    }

    public StringFilter regNumber() {
        if (regNumber == null) {
            regNumber = new StringFilter();
        }
        return regNumber;
    }

    public void setRegNumber(StringFilter regNumber) {
        this.regNumber = regNumber;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
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
        final ProjectCriteria that = (ProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(description, that.description) &&
            Objects.equals(completionDate, that.completionDate) &&
            Objects.equals(regNumber, that.regNumber) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(address, that.address) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(exUserId, that.exUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            isActive,
            description,
            completionDate,
            regNumber,
            notes,
            address,
            locationId,
            exUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (completionDate != null ? "completionDate=" + completionDate + ", " : "") +
            (regNumber != null ? "regNumber=" + regNumber + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (exUserId != null ? "exUserId=" + exUserId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
