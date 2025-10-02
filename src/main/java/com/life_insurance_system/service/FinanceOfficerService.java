package com.life_insurance_system.service;

import com.life_insurance_system.model.Payment;
import com.life_insurance_system.model.Policy;

import java.util.List;
import java.util.Optional;

public interface FinanceOfficerService {

    /**
     * Finds all payments in the system.
     * @return A list of all payments.
     */
    List<Payment> findAllPayments();

    /**
     * Finds a payment by its ID.
     * @param paymentId The ID of the payment to find.
     * @return An Optional containing the payment if found.
     */
    Optional<Payment> findPaymentById(int paymentId);

    /**
     * Finds all policies to associate with payments.
     * @return A list of all policies.
     */
    List<Policy> findAllPolicies();

    /**
     * Records a new payment.
     * @param payment The payment object to be saved.
     * @return The saved payment.
     */
    Payment recordNewPayment(Payment payment);

    /**
     * Updates the status of an existing payment.
     * @param paymentId The ID of the payment to update.
     * @param status The new status of the payment.
     * @return The updated payment.
     */
    Payment updatePaymentStatus(int paymentId, Payment.Status status);

    /**
     * Cancels a failed payment.
     * @param paymentId The ID of the payment to cancel.
     */
    void cancelFailedPayment(int paymentId);
}