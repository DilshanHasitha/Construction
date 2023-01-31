package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.UserRole} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UserRoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRoleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter userRole;

    private BooleanFilter isActive;

    private LongFilter userPermissionId;

    private Boolean distinct;

    public UserRoleCriteria() {}

    public UserRoleCriteria(UserRoleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.userRole = other.userRole == null ? null : other.userRole.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.userPermissionId = other.userPermissionId == null ? null : other.userPermissionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserRoleCriteria copy() {
        return new UserRoleCriteria(this);
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

    public StringFilter getUserRole() {
        return userRole;
    }

    public StringFilter userRole() {
        if (userRole == null) {
            userRole = new StringFilter();
        }
        return userRole;
    }

    public void setUserRole(StringFilter userRole) {
        this.userRole = userRole;
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

    public LongFilter getUserPermissionId() {
        return userPermissionId;
    }

    public LongFilter userPermissionId() {
        if (userPermissionId == null) {
            userPermissionId = new LongFilter();
        }
        return userPermissionId;
    }

    public void setUserPermissionId(LongFilter userPermissionId) {
        this.userPermissionId = userPermissionId;
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
        final UserRoleCriteria that = (UserRoleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(userRole, that.userRole) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(userPermissionId, that.userPermissionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, userRole, isActive, userPermissionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRoleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (userRole != null ? "userRole=" + userRole + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (userPermissionId != null ? "userPermissionId=" + userPermissionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
