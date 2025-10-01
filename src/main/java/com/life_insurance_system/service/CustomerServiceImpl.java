package com.life_insurance_system.service;

import com.life_insurance_system.model.*;
import com.life_insurance_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final ClaimRepository claimRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, PolicyRepository policyRepository,
                               BeneficiaryRepository beneficiaryRepository, ClaimRepository claimRepository,
                               PaymentRepository paymentRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.policyRepository = policyRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.claimRepository = claimRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Beneficiary> findBeneficiaryById(int beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId);
    }

    @Override
    public Optional<Customer> findCustomerByUsername(String username) {
        return customerRepository.findByUser_Username(username);
    }

    @Override
    @Transactional
    public Customer updateCustomerProfile(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Policy> findPoliciesByCustomerId(int customerId) {
        return policyRepository.findByCustomer_CustomerId(customerId);
    }

    @Override
    public Optional<Policy> findPolicyById(int policyId) {
        return policyRepository.findById(policyId);
    }

    @Override
    @Transactional
    public void requestPolicyCancellation(int policyId) {
        policyRepository.findById(policyId).ifPresent(policy -> {
            policy.setStatus(Policy.Status.CANCELLED);
            policyRepository.save(policy);
        });
    }

    @Override
    public List<Beneficiary> findBeneficiariesByPolicyId(int policyId) {
        return beneficiaryRepository.findByPolicy_PolicyId(policyId);
    }

    @Override
    @Transactional
    public Beneficiary addBeneficiary(Beneficiary beneficiary) {
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    @Transactional
    public void removeBeneficiary(int beneficiaryId) {
        beneficiaryRepository.deleteById(beneficiaryId);
    }

    @Override
    public List<Claim> findClaimsByCustomerId(int customerId) {
        return claimRepository.findByCustomer_CustomerId(customerId);
    }

    @Override
    @Transactional
    public Claim submitNewClaim(Claim claim) {
        claim.setStatus(Claim.Status.PENDING);
        return claimRepository.save(claim);
    }

    @Override
    @Transactional
    public Claim updateClaimDetails(Claim claim) {
        Claim existingClaim = claimRepository.findById(claim.getClaimId())
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        if (existingClaim.getStatus() != Claim.Status.PENDING) {
            throw new IllegalStateException("Claim can only be updated while in PENDING status.");
        }
        // Update allowed fields
        existingClaim.setRemarks(claim.getRemarks());
        return claimRepository.save(existingClaim);
    }

    @Override
    public List<Payment> findPaymentHistoryByPolicyId(int policyId) {
        return paymentRepository.findByPolicy_PolicyId(policyId);
    }
}