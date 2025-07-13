package com.javaweb.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SellerDashboardDTO {
    private Integer listings;
    private Integer contacts;
    private String membership;

    public Integer getListings() {
        return listings;
    }

    public void setListings(Integer listings) {
        this.listings = listings;
    }

    public Integer getContacts() {
        return contacts;
    }

    public void setContacts(Integer contacts) {
        this.contacts = contacts;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }
}
