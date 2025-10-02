package com.life_insurance_system.repository;

import com.life_insurance_system.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer> {

    List<Claim> findByCustomer_CustomerId(int customerId);
}