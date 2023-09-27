package com.mycompany.myapp.service.dto;

import java.io.Serializable;

public class ExUserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String login;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private Integer phone;
    private String brNumber;
    private String companyName;
    private String companyCode;
    private String userTypeCode;

    public ExUserLoginDTO(
        Long id,
        String login,
        String userName,
        String firstName,
        String lastName,
        String email,
        Integer phone,
        String brNumber,
        String companyName,
        String companyCode,
        String userTypeCode
    ) {
        this.id = id;
        this.login = login;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.brNumber = brNumber;
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.userTypeCode = userTypeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getBrNumber() {
        return brNumber;
    }

    public void setBrNumber(String brNumber) {
        this.brNumber = brNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode;
    }
}
