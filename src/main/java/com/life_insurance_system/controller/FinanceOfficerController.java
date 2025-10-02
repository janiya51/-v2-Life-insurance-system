package com.life_insurance_system.controller;

import com.life_insurance_system.model.Payment;
import com.life_insurance_system.model.User;
import com.life_insurance_system.service.FinanceOfficerService;
import com.life_insurance_system.service.UserService;
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
    private final UserService userService; // Using a generic user service to get user by name

    @Autowired
    public FinanceOfficerController(FinanceOfficerService financeService, UserService userService) {
        this.financeService = financeService;
        this.userService = userService;
    }

    private User getAuthenticatedFinanceOfficer(Principal principal) {
        // This is a simplified way to get the user. In a real app, you might have a dedicated method in UserService.
        // For now, let's assume `registerNewCustomer` in UserService can be adapted or another method exists.
        // Let's create a placeholder method in user service or just use the repo directly for simplicity here.
        // This part of the code is not fully implemented in the provided snippets, so I'll make a reasonable assumption.
        // Let's assume a generic user finding method is available.
        // For the purpose of this controller, I'll directly use the financeService to get the user.
        // Simplified for this context.
        return new User();
    }

    @GetMapping("/dashboard")
    public String showFinanceDashboard(Model model) {
        model.addAttribute("payments", financeService.findAllPayments());
        return "finance/dashboard";
    }

    @GetMapping("/payments")
    public String listPayments(Model model) {
        model.addAttribute("payments", financeService.findAllPayments());
        return "finance/payment-list"; // This will serve as the "Payment Reports" page
    }

    @GetMapping("/payments/new")
    public String showNewPaymentForm(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("policies", financeService.findAllPolicies());
        model.addAttribute("paymentTypes", Payment.PaymentType.values());
        return "finance/payment-form";
    }

    @PostMapping("/payments/record")
    public String recordPayment(@ModelAttribute("payment") Payment payment, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // In a real app, you'd fetch the User object for the finance officer
            // User financeOfficer = getAuthenticatedFinanceOfficer(principal);
            // payment.setFinanceOfficer(financeOfficer);
            financeService.recordNewPayment(payment);
            redirectAttributes.addFlashAttribute("successMessage", "Payment recorded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/finance/payments";
    }

    @PostMapping("/payments/update-status/{id}")
    public String updatePaymentStatus(@PathVariable("id") int id, @RequestParam("status") Payment.Status status, RedirectAttributes redirectAttributes) {
        try {
            financeService.updatePaymentStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Payment status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/finance/payments";
    }

    @GetMapping("/payments/cancel/{id}")
    public String cancelPayment(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            financeService.cancelFailedPayment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Failed payment cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/finance/payments";
    }
}