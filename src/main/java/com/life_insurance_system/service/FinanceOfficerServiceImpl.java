package com.life_insurance_system.service;

import com.life_insurance_system.model.Payment;
import com.life_insurance_system.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FinanceOfficerServiceImpl implements FinanceOfficerService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public FinanceOfficerServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> findPaymentById(int paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    @Transactional
    public Payment recordNewPayment(Payment payment) {
        // New payments default to PENDING status as per the database schema
        if (payment.getStatus() == null) {
            payment.setStatus(Payment.Status.PENDING);
        }
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(int paymentId, Payment.Status status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        // Business Rule: Only PENDING or FAILED payments can be updated.
        if (payment.getStatus() != Payment.Status.PENDING && payment.getStatus() != Payment.Status.FAILED) {
            throw new IllegalStateException("Only payments with PENDING or FAILED status can be updated. Current status: " + payment.getStatus());
        }

        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void cancelFailedPayment(int paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        // Business Rule: Only FAILED payments can be deleted (cancelled).
        if (payment.getStatus() != Payment.Status.FAILED) {
            throw new IllegalStateException("Only payments with FAILED status can be cancelled. Current status: " + payment.getStatus());
        }

        paymentRepository.deleteById(paymentId);
    }
}