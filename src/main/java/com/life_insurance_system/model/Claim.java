package com.life_insurance_system.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private int claimId;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "submitted_date", nullable = false)
    private Date submittedDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'APPROVED', 'REJECTED', 'UNDER_INVESTIGATION') DEFAULT 'PENDING'")
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    public enum Status {
        PENDING, APPROVED, REJECTED, UNDER_INVESTIGATION
    }

    // Constructors
    public Claim() {
    }

    public Claim(Policy policy, Customer customer, Date submittedDate, Status status, String remarks) {
        this.policy = policy;
        this.customer = customer;
        this.submittedDate = submittedDate;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters and Setters
    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}