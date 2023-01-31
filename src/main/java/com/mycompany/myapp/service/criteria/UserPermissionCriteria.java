package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.UserPermission} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UserPermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPermissionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter action;

    private StringFilter document;

    private StringFilter description;

    private LongFilter userRoleId;

    private Boolean distinct;

    public UserPermissionCriteria() {}

    public UserPermissionCriteria(UserPermissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.document = other.document == null ? null : other.document.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.userRoleId = other.userRoleId == null ? null : other.userRoleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserPermissionCriteria copy() {
        return new UserPermissionCriteria(this);
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

    public StringFilter getAction() {
        return action;
    }

    public StringFilter action() {
        if (action == null) {
            action = new StringFilter();
        }
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public StringFilter getDocument() {
        return document;
    }

    public StringFilter document() {
        if (document == null) {
            document = new StringFilter();
        }
        return document;
    }

    public void setDocument(StringFilter document) {
        this.document = document;
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

    public LongFilter getUserRoleId() {
        return userRoleId;
    }

    public LongFilter userRoleId() {
        if (userRoleId == null) {
            userRoleId = new LongFilter();
        }
        return userRoleId;
    }

    public void setUserRoleId(LongFilter userRoleId) {
        this.userRoleId = userRoleId;
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
        final UserPermissionCriteria that = (UserPermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(action, that.action) &&
            Objects.equals(document, that.document) &&
            Objects.equals(description, that.description) &&
            Objects.equals(userRoleId, that.userRoleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, document, description, userRoleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPermissionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (action != null ? "action=" + action + ", " : "") +
            (document != null ? "document=" + document + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (userRoleId != null ? "userRoleId=" + userRoleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
