package com.life_insurance_system.service;

import com.life_insurance_system.model.Policy;
import com.life_insurance_system.model.RiskAssessment;
import com.life_insurance_system.repository.PolicyRepository;
import com.life_insurance_system.repository.RiskAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SeniorInsuranceAdvisorServiceImpl implements SeniorInsuranceAdvisorService {

    private final PolicyRepository policyRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

    @Autowired
    public SeniorInsuranceAdvisorServiceImpl(PolicyRepository policyRepository, RiskAssessmentRepository riskAssessmentRepository) {
        this.policyRepository = policyRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    @Override
    public List<Policy> findPoliciesByAdvisorId(int advisorId) {
        return policyRepository.findByAdvisor_UserId(advisorId);
    }

    @Override
    public Optional<RiskAssessment> findRiskAssessmentById(int assessmentId) {
        return riskAssessmentRepository.findById(assessmentId);
    }

    @Override
    public Optional<RiskAssessment> findRiskAssessmentByPolicyId(int policyId) {
        return riskAssessmentRepository.findByPolicy_PolicyId(policyId);
    }

    @Override
    @Transactional
    public RiskAssessment saveRiskAssessment(RiskAssessment riskAssessment) {
        // Business rule: Cannot update finalized assessments (APPROVED or REJECTED)
        if (riskAssessment.getAssessmentId() != 0) {
            RiskAssessment existing = riskAssessmentRepository.findById(riskAssessment.getAssessmentId())
                    .orElseThrow(() -> new RuntimeException("Assessment not found"));
            if (existing.getStatus() == RiskAssessment.Status.APPROVED || existing.getStatus() == RiskAssessment.Status.REJECTED) {
                throw new IllegalStateException("Cannot update an assessment that is already APPROVED or REJECTED.");
            }
        }
        return riskAssessmentRepository.save(riskAssessment);
    }

    @Override
    @Transactional
    public void deleteRiskAssessment(int assessmentId) {
        RiskAssessment assessment = riskAssessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + assessmentId));

        // Business rule: Cannot delete finalized assessments.
        if (assessment.getStatus() == RiskAssessment.Status.APPROVED || assessment.getStatus() == RiskAssessment.Status.REJECTED) {
            throw new IllegalStateException("Cannot delete an assessment that has been finalized.");
        }

        riskAssessmentRepository.deleteById(assessmentId);
    }
}