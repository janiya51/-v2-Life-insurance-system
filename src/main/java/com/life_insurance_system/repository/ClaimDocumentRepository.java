package com.life_insurance_system.repository;

import com.life_insurance_system.model.ClaimDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, Integer> {
}