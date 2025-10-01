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
        return "customer/dashboard"; // Returns customer/dashboard.html
    }

    // --- Profile ---
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        model.addAttribute("customer", getAuthenticatedCustomer(principal));
        return "customer/profile"; // Returns customer/profile.html
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("customer") Customer customer, Principal principal, RedirectAttributes redirectAttributes) {
        Customer authCustomer = getAuthenticatedCustomer(principal);
        // Ensure users can only update their own profile
        customer.setCustomerId(authCustomer.getCustomerId());
        customer.setUser(authCustomer.getUser()); // Preserve the original user link
        customerService.updateCustomerProfile(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/customer/profile";
    }

    // --- Policies ---
    @GetMapping("/policies")
    public String listPolicies(Model model, Principal principal) {
        Customer customer = getAuthenticatedCustomer(principal);
        model.addAttribute("policies", customerService.findPoliciesByCustomerId(customer.getCustomerId()));
        return "customer/policy-list"; // Returns customer/policy-list.html
    }

    // --- Beneficiaries ---
    @GetMapping("/policies/{policyId}/beneficiaries")
    public String listBeneficiaries(@PathVariable int policyId, Model model) {
        model.addAttribute("policy", customerService.findPolicyById(policyId).orElse(null));
        model.addAttribute("beneficiaries", customerService.findBeneficiariesByPolicyId(policyId));
        model.addAttribute("newBeneficiary", new Beneficiary());
        return "customer/beneficiary-list"; // Returns customer/beneficiary-list.html
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


    // --- Claims ---
    @GetMapping("/claims")
    public String listClaims(Model model, Principal principal) {
        Customer customer = getAuthenticatedCustomer(principal);
        model.addAttribute("claims", customerService.findClaimsByCustomerId(customer.getCustomerId()));
        model.addAttribute("newClaim", new Claim());
        model.addAttribute("policies", customerService.findPoliciesByCustomerId(customer.getCustomerId()));
        return "customer/claim-list"; // Returns customer/claim-list.html
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