package com.javaweb.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDTO {
    //for admin
    private Integer properties;
    private Integer users;
    private Long revunements;
    private Integer reports;
    private Integer transactions;

    public Integer getTransactions() {
        return transactions;
    }

    public void setTransactions(Integer transactions) {
        this.transactions = transactions;
    }

    public Integer getProperties() {
        return properties;
    }

    public void setProperties(Integer properties) {
        this.properties = properties;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Long getRevunements() {
        return revunements;
    }

    public void setRevunements(Long revunements) {
        this.revunements = revunements;
    }

    public Integer getReports() {
        return reports;
    }

    public void setReports(Integer reports) {
        this.reports = reports;
    }

}
