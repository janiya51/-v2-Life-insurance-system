package com.life_insurance_system.service;

import com.life_insurance_system.model.PolicyDispute;
import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.PolicyDisputeRepository;
import com.life_insurance_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PolicyDisputeRepository policyDisputeRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, PolicyDisputeRepository policyDisputeRepository) {
        this.userRepository = userRepository;
        this.policyDisputeRepository = policyDisputeRepository;
    }

    // --- User Management ---

    @Override
    @Transactional
    public User createStaffUser(User user) throws Exception {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new Exception("Username already exists: " + user.getUsername());
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email already exists: " + user.getEmail());
        }
        // Ensure only non-customer roles can be created here
        if (user.getRole() == User.Role.CUSTOMER) {
            throw new IllegalArgumentException("Cannot create a CUSTOMER role via the admin service.");
        }
        user.setActive(true); // New users are active by default
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllStaffUsers() {
        // Filter out customers to return only staff members
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() != User.Role.CUSTOMER)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        // The save method handles both create and update operations
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateUser(int id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(false);
            userRepository.save(user);
        });
    }

    // --- Policy Dispute Management ---

    @Override
    public List<PolicyDispute> findAllDisputes() {
        return policyDisputeRepository.findAll();
    }

    @Override
    public Optional<PolicyDispute> findDisputeById(int id) {
        return policyDisputeRepository.findById(id);
    }

    @Override
    @Transactional
    public PolicyDispute updateDisputeStatus(int disputeId, PolicyDispute.Status status) {
        PolicyDispute dispute = policyDisputeRepository.findById(disputeId)
                .orElseThrow(() -> new RuntimeException("Dispute not found with id: " + disputeId));
        dispute.setStatus(status);
        return policyDisputeRepository.save(dispute);
    }

    @Override
    @Transactional
    public void deleteDispute(int id) {
        policyDisputeRepository.deleteById(id);
    }
}