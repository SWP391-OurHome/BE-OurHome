package com.javaweb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContactDTO {
    private Integer contactId;
    private LocalDate contactDate;
    private boolean status;
    private String response;
    private String buyerFirstName;
    private String buyerLastName;
    private String buyerPhone;

    // Constructor for JPQL
    public ContactDTO(Integer contactId, LocalDate contactDate, boolean status, String response,
                      String buyerFirstName, String buyerLastName, String buyerPhone) {
        this.contactId = contactId;
        this.contactDate = contactDate;
        this.status = status;
        this.response = response;
        this.buyerFirstName = buyerFirstName;
        this.buyerLastName = buyerLastName;
        this.buyerPhone = buyerPhone;
    }

    // Getters and setters
    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public LocalDate getContactDate() {
        return contactDate;
    }

    public void setContactDate(LocalDate contactDate) {
        this.contactDate = contactDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getBuyerFirstName() {
        return buyerFirstName;
    }

    public void setBuyerFirstName(String buyerFirstName) {
        this.buyerFirstName = buyerFirstName;
    }

    public String getBuyerLastName() {
        return buyerLastName;
    }

    public void setBuyerLastName(String buyerLastName) {
        this.buyerLastName = buyerLastName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }
}