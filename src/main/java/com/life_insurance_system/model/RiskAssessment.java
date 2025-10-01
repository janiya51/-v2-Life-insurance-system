package com.life_insurance_system.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "RiskAssessments")
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    private int assessmentId;

    @OneToOne
    @JoinColumn(name = "policy_id", unique = true, nullable = false)
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
    private User advisor;

    @Column(name = "assessment_date", nullable = false)
    private Date assessmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum RiskLevel {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        APPROVED, REJECTED, REVISION_REQUIRED
    }

    // Constructors
    public RiskAssessment() {
    }

    public RiskAssessment(Policy policy, User advisor, Date assessmentDate, RiskLevel riskLevel, String remarks, Status status) {
        this.policy = policy;
        this.advisor = advisor;
        this.assessmentDate = assessmentDate;
        this.riskLevel = riskLevel;
        this.remarks = remarks;
        this.status = status;
    }

    // Getters and Setters
    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public User getAdvisor() {
        return advisor;
    }

    public void setAdvisor(User advisor) {
        this.advisor = advisor;
    }

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}