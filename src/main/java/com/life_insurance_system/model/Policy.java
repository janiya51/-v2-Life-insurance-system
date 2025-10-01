package com.life_insurance_system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "Policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private int policyId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private User advisor;

    @Column(name = "policy_type", nullable = false, length = 100)
    private String policyType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'ACTIVE', 'REJECTED', 'CANCELLED', 'EXPIRED') DEFAULT 'PENDING'")
    private Status status;

    @Column(name = "sum_assured", precision = 12, scale = 2)
    private BigDecimal sumAssured;

    public enum Status {
        PENDING, ACTIVE, REJECTED, CANCELLED, EXPIRED
    }

    // Constructors
    public Policy() {
    }

    public Policy(Customer customer, User advisor, String policyType, Date startDate, Date endDate, Status status, BigDecimal sumAssured) {
        this.customer = customer;
        this.advisor = advisor;
        this.policyType = policyType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.sumAssured = sumAssured;
    }

    // Getters and Setters
    public int getPolicyId() {
        return policyId;
    }

    public void setPolicyId(int policyId) {
        this.policyId = policyId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getAdvisor() {
        return advisor;
    }

    public void setAdvisor(User advisor) {
        this.advisor = advisor;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(BigDecimal sumAssured) {
        this.sumAssured = sumAssured;
    }
}