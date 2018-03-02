package com.madret.icardadmin.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tigani on 11/13/2017.
 */

public class Card
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("identification_number")
    @Expose
    private String identificationNumber;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("profession")
    @Expose
    private String profession;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("person_image")
    @Expose
    private String personImage;
    @SerializedName("issue_date")
    @Expose
    private String issueDate;
    @SerializedName("expire_date")
    @Expose
    private String expireDate;
    @SerializedName("companylink")
    @Expose
    private String companylink;
    @SerializedName("adminphone")
    @Expose
    private String adminphone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonImage() {
        return personImage;
    }

    public void setPersonImage(String personImage) {
        this.personImage = personImage;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCompanylink() {
        return companylink;
    }

    public void setCompanylink(String companylink) {
        this.companylink = companylink;
    }

    public String getAdminphone() {
        return adminphone;
    }

    public void setAdminphone(String adminphone) {
        this.adminphone = adminphone;
    }
}
