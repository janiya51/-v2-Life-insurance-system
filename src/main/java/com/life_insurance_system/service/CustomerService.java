package com.life_insurance_system.service;

import com.life_insurance_system.model.*;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    // --- Profile Management ---
    Optional<Customer> findCustomerByUsername(String username);
    Customer updateCustomerProfile(Customer customer);

    // --- Policy Management ---
    List<Policy> findPoliciesByCustomerId(int customerId);
    Optional<Policy> findPolicyById(int policyId);
    void requestPolicyCancellation(int policyId);

    // --- Beneficiary Management ---
    List<Beneficiary> findBeneficiariesByPolicyId(int policyId);
    Optional<Beneficiary> findBeneficiaryById(int beneficiaryId);
    Beneficiary addBeneficiary(Beneficiary beneficiary);
    void removeBeneficiary(int beneficiaryId);

    // --- Claim Management ---
    List<Claim> findClaimsByCustomerId(int customerId);
    Claim submitNewClaim(Claim claim);
    Claim updateClaimDetails(Claim claim);

    // --- Payment History ---
    List<Payment> findPaymentHistoryByPolicyId(int policyId);
}