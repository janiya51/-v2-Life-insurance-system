package com.life_insurance_system.controller;

import com.life_insurance_system.model.*;
import com.life_insurance_system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private Customer getAuthenticatedCustomer(Principal principal) {
        return customerService.findCustomerByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated customer not found"));
    }

    @GetMapping("/dashboard")
    public String showCustomerDashboard(Model model, Principal principal) {
        Customer customer = getAuthenticatedCustomer(principal);
        model.addAttribute("customer", customer);
        model.addAttribute("policies", customerService.findPoliciesByCustomerId(customer.getCustomerId()));
        model.addAttribute("claims", customerService.findClaimsByCustomerId(customer.getCustomerId()));
        return "customer/dashboard";
    }

    // --- Profile Management ---
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        model.addAttribute("customer", getAuthenticatedCustomer(principal));
        return "customer/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("customer") Customer customer, Principal principal, RedirectAttributes redirectAttributes) {
        Customer authCustomer = getAuthenticatedCustomer(principal);
        customer.setCustomerId(authCustomer.getCustomerId());
        customer.setUser(authCustomer.getUser());
        customerService.updateCustomerProfile(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/customer/profile";
    }

    // --- Policy & Payment History ---
    @GetMapping("/policies")
    public String listPolicies(Model model, Principal principal) {
        Customer customer = getAuthenticatedCustomer(principal);
        model.addAttribute("policies", customerService.findPoliciesByCustomerId(customer.getCustomerId()));
        return "customer/policy-list";
    }

    @GetMapping("/policies/{policyId}/payments")
    public String showPaymentHistory(@PathVariable int policyId, Model model, Principal principal) {
        Policy policy = customerService.findPolicyById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));
        // Security check: ensure the policy belongs to the logged-in customer
        if (policy.getCustomer().getCustomerId() != getAuthenticatedCustomer(principal).getCustomerId()) {
            throw new IllegalStateException("Unauthorized access to payment history");
        }
        model.addAttribute("policy", policy);
        model.addAttribute("payments", customerService.findPaymentHistoryByPolicyId(policyId));
        return "customer/payment-history";
    }


    // --- Beneficiary Management ---
    @GetMapping("/policies/{policyId}/beneficiaries")
    public String listBeneficiaries(@PathVariable int policyId, Model model) {
        model.addAttribute("policy", customerService.findPolicyById(policyId).orElse(null));
        model.addAttribute("beneficiaries", customerService.findBeneficiariesByPolicyId(policyId));
        model.addAttribute("newBeneficiary", new Beneficiary());
        return "customer/beneficiary-list";
    }

    @PostMapping("/policies/{policyId}/beneficiaries/add")
    public String addBeneficiary(@PathVariable int policyId, @ModelAttribute("newBeneficiary") Beneficiary beneficiary, RedirectAttributes redirectAttributes) {
        Policy policy = customerService.findPolicyById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));
        beneficiary.setPolicy(policy);
        customerService.addBeneficiary(beneficiary);
        redirectAttributes.addFlashAttribute("successMessage", "Beneficiary added successfully!");
        return "redirect:/customer/policies/" + policyId + "/beneficiaries";
    }

    @GetMapping("/beneficiaries/remove/{beneficiaryId}")
    public String removeBeneficiary(@PathVariable int beneficiaryId, RedirectAttributes redirectAttributes) {
        Beneficiary beneficiary = customerService.findBeneficiaryById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + beneficiaryId));
        int policyId = beneficiary.getPolicy().getPolicyId();
        customerService.removeBeneficiary(beneficiaryId);
        redirectAttributes.addFlashAttribute("successMessage", "Beneficiary removed successfully!");
        return "redirect:/customer/policies/" + policyId + "/beneficiaries";
    }

    // --- Claim Management ---
    @GetMapping("/claims")
    public String listClaims(Model model, Principal principal) {
        Customer customer = getAuthenticatedCustomer(principal);
        model.addAttribute("claims", customerService.findClaimsByCustomerId(customer.getCustomerId()));
        model.addAttribute("newClaim", new Claim());
        model.addAttribute("policies", customerService.findPoliciesByCustomerId(customer.getCustomerId()));
        return "customer/claim-list";
    }

    @PostMapping("/claims/submit")
    public String submitClaim(@ModelAttribute("newClaim") Claim claim, Principal principal, RedirectAttributes redirectAttributes) {
        Customer customer = getAuthenticatedCustomer(principal);
        claim.setCustomer(customer);
        customerService.submitNewClaim(claim);
        redirectAttributes.addFlashAttribute("successMessage", "Claim submitted successfully!");
        return "redirect:/customer/claims";
    }
}