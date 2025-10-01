package com.life_insurance_system.controller;

import com.life_insurance_system.model.Payment;
import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.PolicyRepository;
import com.life_insurance_system.repository.UserRepository;
import com.life_insurance_system.service.FinanceOfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/finance")
public class FinanceOfficerController {

    private final FinanceOfficerService financeService;
    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;

    @Autowired
    public FinanceOfficerController(FinanceOfficerService financeService, UserRepository userRepository, PolicyRepository policyRepository) {
        this.financeService = financeService;
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
    }

    private User getAuthenticatedFinanceOfficer(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated finance officer not found"));
    }

    @GetMapping("/dashboard")
    public String showFinanceDashboard(Model model) {
        model.addAttribute("payments", financeService.findAllPayments());
        return "finance/dashboard"; // Returns finance/dashboard.html
    }

    @GetMapping("/payments")
    public String listPayments(Model model) {
        model.addAttribute("payments", financeService.findAllPayments());
        return "finance/payment-list"; // Returns finance/payment-list.html
    }

    @GetMapping("/payments/new")
    public String showNewPaymentForm(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("policies", policyRepository.findAll());
        model.addAttribute("paymentTypes", Payment.PaymentType.values());
        return "finance/payment-form"; // Returns finance/payment-form.html
    }

    @PostMapping("/payments/record")
    public String recordPayment(@ModelAttribute("payment") Payment payment, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User financeOfficer = getAuthenticatedFinanceOfficer(principal);
            payment.setFinanceOfficer(financeOfficer);
            financeService.recordNewPayment(payment);
            redirectAttributes.addFlashAttribute("successMessage", "Payment recorded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/finance/payments";
    }

    @PostMapping("/payments/update-status/{id}")
    public String updatePaymentStatus(@PathVariable("id") int id, @RequestParam("status") Payment.Status status, RedirectAttributes redirectAttributes) {
        try {
            financeService.updatePaymentStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Payment status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/finance/payments";
    }

    @GetMapping("/payments/cancel/{id}")
    public String cancelPayment(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            financeService.cancelFailedPayment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Failed payment cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/finance/payments";
    }
}