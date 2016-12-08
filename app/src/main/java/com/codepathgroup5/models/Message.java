package com.codepathgroup5.models;


import com.yelp.clientlib.entities.Business;

import org.parceler.Parcel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Parcel
public class Message {
    String planPurpouse;
    String description;
    String contactEmail;
    String contactPhone;
    String postalCode;
    boolean permitCalls;
    Date whenDate;
    List<Business> listYelp;

    public Message() {
        super();
    }

    @Override
    public String toString() {
        return "Message{" +
                "planPurpouse='" + planPurpouse + '\'' +
                ", description='" + description + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", permitCalls=" + permitCalls +
                ", whenDate=" + whenDate +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public boolean isPermitCalls() {
        return permitCalls;
    }

    public void setPermitCalls(boolean permitCalls) {
        this.permitCalls = permitCalls;
    }

    public Date getWhenDate() {
        return whenDate;
    }

    public void setWhenDate(Date whenDate) {
        this.whenDate = whenDate;
    }

    public String getPlanPurpouse() {
        return planPurpouse;
    }

    public void setPlanPurpouse(String planPurpouse) {
        this.planPurpouse = planPurpouse;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setListYelp(LinkedList<Business> listYelp) {
        this.listYelp = listYelp;
    }

    public List<Business> listYelp(){
        return listYelp;
    }
}
