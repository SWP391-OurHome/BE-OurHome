package com.javaweb.repository.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReportID")
    private Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ListingID", referencedColumnName = "Listing_ID", nullable = false)
    private ListingEntity listing;

    @Column(name = "ReportDate", nullable = false)
    private LocalDateTime reportDate;

    @Column(name = "Reason", nullable = false)
    private String reason;

    @Column(name = "Comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "Response", columnDefinition = "TEXT")
    private String response;

    public ReportEntity() {}

    public ReportEntity(UserEntity user, ListingEntity listing, LocalDateTime reportDate, String reason, String comment, String status, String response) {
        this.user = user;
        this.listing = listing;
        this.reportDate = reportDate;
        this.reason = reason;
        this.comment = comment;
        this.status = status;
        this.response = response;
    }

    // Getters and Setters
    public Integer getReportId() { return reportId; }
    public void setReportId(Integer reportId) { this.reportId = reportId; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public ListingEntity getListing() { return listing; }
    public void setListing(ListingEntity listing) { this.listing = listing; }
    public LocalDateTime getReportDate() { return reportDate; }
    public void setReportDate(LocalDateTime reportDate) { this.reportDate = reportDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}