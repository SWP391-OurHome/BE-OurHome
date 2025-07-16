package com.javaweb.model;

public class ContactOverTimeDTO {
    private String month;
    private int contacts;

    public ContactOverTimeDTO(String month, int contacts) {
        this.month = month;
        this.contacts = contacts;
    }

    // Getters and setters
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getContacts() {
        return contacts;
    }

    public void setContacts(int contacts) {
        this.contacts = contacts;
    }
}
