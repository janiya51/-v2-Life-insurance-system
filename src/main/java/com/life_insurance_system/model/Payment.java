package com.life_insurance_system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "finance_officer_id")
    private User financeOfficer;

    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING'")
    private Status status;

    public enum PaymentType {
        PREMIUM, BENEFIT
    }

    public enum Status {
        PENDING, COMPLETED, FAILED
    }

    // Constructors
    public Payment() {
    }

    public Payment(Policy policy, User financeOfficer, Date paymentDate, BigDecimal amount, PaymentType paymentType, Status status) {
        this.policy = policy;
        this.financeOfficer = financeOfficer;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentType = paymentType;
        this.status = status;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public User getFinanceOfficer() {
        return financeOfficer;
    }

    public void setFinanceOfficer(User financeOfficer) {
        this.financeOfficer = financeOfficer;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}