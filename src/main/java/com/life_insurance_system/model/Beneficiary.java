package com.life_insurance_system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beneficiary_id")
    private int beneficiaryId;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String relation;

    @Column(name = "contact_info", length = 200)
    private String contactInfo;

    @Column(name = "share_percentage", precision = 5, scale = 2)
    private BigDecimal sharePercentage;

    // Constructors
    public Beneficiary() {
    }

    public Beneficiary(Policy policy, String name, String relation, String contactInfo, BigDecimal sharePercentage) {
        this.policy = policy;
        this.name = name;
        this.relation = relation;
        this.contactInfo = contactInfo;
        this.sharePercentage = sharePercentage;
    }

    // Getters and Setters
    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(int beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public BigDecimal getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(BigDecimal sharePercentage) {
        this.sharePercentage = sharePercentage;
    }
}