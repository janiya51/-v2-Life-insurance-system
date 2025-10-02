package com.life_insurance_system.service;

import com.life_insurance_system.model.Policy;
import com.life_insurance_system.model.RiskAssessment;

import java.util.List;
import java.util.Optional;

public interface SeniorInsuranceAdvisorService {

    /**
     * Finds all policies assigned to a specific advisor.
     * @param advisorId The ID of the advisor.
     * @return A list of assigned policies.
     */
    List<Policy> findPoliciesByAdvisorId(int advisorId);

    /**
     * Finds a risk assessment by its ID.
     * @param assessmentId The ID of the assessment.
     * @return An Optional containing the risk assessment if found.
     */
    Optional<RiskAssessment> findRiskAssessmentById(int assessmentId);

    /**
     * Finds a risk assessment by its policy ID.
     * @param policyId The ID of the policy.
     * @return An Optional containing the risk assessment if found.
     */
    Optional<RiskAssessment> findRiskAssessmentByPolicyId(int policyId);

    /**
     * Creates or updates a risk assessment for a policy.
     * @param riskAssessment The risk assessment object to be saved.
     * @return The saved risk assessment.
     */
    RiskAssessment saveRiskAssessment(RiskAssessment riskAssessment);

    /**
     * Deletes a risk assessment.
     * @param assessmentId The ID of the assessment to delete.
     */
    void deleteRiskAssessment(int assessmentId);
}