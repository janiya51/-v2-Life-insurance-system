package com.life_insurance_system.service;

import com.life_insurance_system.model.PolicyDispute;
import com.life_insurance_system.model.User;
import java.util.List;
import java.util.Optional;

public interface AdminService {

    // --- User Management ---

    /**
     * Creates a new staff user (non-customer).
     * @param user The user object to be created.
     * @return The saved user.
     * @throws Exception if username or email already exists.
     */
    User createStaffUser(User user) throws Exception;

    /**
     * Retrieves all non-customer users.
     * @return A list of all staff users.
     */
    List<User> findAllStaffUsers();

    /**
     * Finds a user by their ID.
     * @param id The ID of the user to find.
     * @return An Optional containing the user if found.
     */
    Optional<User> findUserById(int id);

    /**
     * Updates an existing user's details.
     * @param user The user object with updated information.
     * @return The updated user.
     */
    User updateUser(User user);

    /**
     * Deactivates a user account.
     * @param id The ID of the user to deactivate.
     */
    void deactivateUser(int id);

    // --- Policy Dispute Management ---

    /**
     * Retrieves all policy disputes.
     * @return A list of all disputes.
     */
    List<PolicyDispute> findAllDisputes();

    /**
     * Finds a policy dispute by its ID.
     * @param id The ID of the dispute.
     * @return An Optional containing the dispute if found.
     */
    Optional<PolicyDispute> findDisputeById(int id);

    /**
     * Updates the status of a policy dispute.
     * @param disputeId The ID of the dispute to update.
     * @param status The new status.
     * @return The updated policy dispute.
     */
    PolicyDispute updateDisputeStatus(int disputeId, PolicyDispute.Status status);

    /**
     * Deletes a resolved policy dispute.
     * @param id The ID of the dispute to delete.
     */
    void deleteDispute(int id);
}