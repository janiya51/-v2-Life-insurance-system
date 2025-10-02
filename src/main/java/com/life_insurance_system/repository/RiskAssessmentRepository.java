package com.life_insurance_system.repository;

import com.life_insurance_system.model.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Integer> {

    Optional<RiskAssessment> findByPolicy_PolicyId(int policyId);
}