package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ExUser} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ExUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ex-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter login;

    private StringFilter userName;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private BooleanFilter isActive;

    private IntegerFilter phone;

    private StringFilter brNumber;

    private LongFilter userId;

    private LongFilter userRoleId;

    private LongFilter companyId;

    private Boolean distinct;

    public ExUserCriteria() {}

    public ExUserCriteria(ExUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.brNumber = other.brNumber == null ? null : other.brNumber.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.userRoleId = other.userRoleId == null ? null : other.userRoleId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExUserCriteria copy() {
        return new ExUserCriteria(this);
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

    public StringFilter getLogin() {
        return login;
    }

    public StringFilter login() {
        if (login == null) {
            login = new StringFilter();
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getUserName() {
        return userName;
    }

    public StringFilter userName() {
        if (userName == null) {
            userName = new StringFilter();
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public IntegerFilter getPhone() {
        return phone;
    }

    public IntegerFilter phone() {
        if (phone == null) {
            phone = new IntegerFilter();
        }
        return phone;
    }

    public void setPhone(IntegerFilter phone) {
        this.phone = phone;
    }

    public StringFilter getBrNumber() {
        return brNumber;
    }

    public StringFilter brNumber() {
        if (brNumber == null) {
            brNumber = new StringFilter();
        }
        return brNumber;
    }

    public void setBrNumber(StringFilter brNumber) {
        this.brNumber = brNumber;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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

    public LongFilter getCompanyId() {
        return companyId;
    }

    public LongFilter companyId() {
        if (companyId == null) {
            companyId = new LongFilter();
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
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
        final ExUserCriteria that = (ExUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(brNumber, that.brNumber) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userRoleId, that.userRoleId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            login,
            userName,
            firstName,
            lastName,
            email,
            isActive,
            phone,
            brNumber,
            userId,
            userRoleId,
            companyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (login != null ? "login=" + login + ", " : "") +
            (userName != null ? "userName=" + userName + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (brNumber != null ? "brNumber=" + brNumber + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (userRoleId != null ? "userRoleId=" + userRoleId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
