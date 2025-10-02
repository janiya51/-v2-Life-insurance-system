package com.life_insurance_system.service;

import com.life_insurance_system.model.PolicyDispute;
import com.life_insurance_system.model.User;
import java.util.List;
import java.util.Optional;

public interface AdminService {

    // --- User Management ---
    User createStaffUser(User user) throws Exception;
    List<User> findAllStaffUsers();
    Optional<User> findUserById(int id);
    User updateUser(User user);
    void deactivateUser(int id);

    // --- Policy Dispute Management ---
    List<PolicyDispute> findAllDisputes();
    Optional<PolicyDispute> findDisputeById(int id);
    PolicyDispute updateDisputeStatus(int disputeId, PolicyDispute.Status status);
    void deleteDispute(int id);

    // --- Profile Management ---
    Optional<User> findUserByUsername(String username);
    User updateAdminProfile(User user);
}