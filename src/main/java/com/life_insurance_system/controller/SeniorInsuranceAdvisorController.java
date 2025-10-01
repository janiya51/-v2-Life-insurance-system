package com.life_insurance_system.controller;

import com.life_insurance_system.model.Policy;
import com.life_insurance_system.model.RiskAssessment;
import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.UserRepository;
import com.life_insurance_system.service.SeniorInsuranceAdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/advisor")
public class SeniorInsuranceAdvisorController {

    private final SeniorInsuranceAdvisorService advisorService;
    private final UserRepository userRepository;

    @Autowired
    public SeniorInsuranceAdvisorController(SeniorInsuranceAdvisorService advisorService, UserRepository userRepository) {
        this.advisorService = advisorService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedAdvisor(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated advisor not found"));
    }

    @GetMapping("/dashboard")
    public String showAdvisorDashboard(Model model, Principal principal) {
        User advisor = getAuthenticatedAdvisor(principal);
        model.addAttribute("policies", advisorService.findPoliciesByAdvisorId(advisor.getUserId()));
        return "advisor/dashboard"; // Returns advisor/dashboard.html
    }

    @GetMapping("/assessments/manage/{policyId}")
    public String showManageAssessmentForm(@PathVariable int policyId, Model model, Principal principal) {
        User advisor = getAuthenticatedAdvisor(principal);
        RiskAssessment assessment = advisorService.findRiskAssessmentByPolicyId(policyId)
                .orElse(new RiskAssessment());

        // If it's a new assessment, pre-populate necessary fields
        if (assessment.getAssessmentId() == 0) {
            Policy policy = advisorService.findPoliciesByAdvisorId(advisor.getUserId()).stream()
                    .filter(p -> p.getPolicyId() == policyId).findFirst()
                    .orElseThrow(() -> new RuntimeException("Policy not found or not assigned to advisor"));
            assessment.setPolicy(policy);
            assessment.setAdvisor(advisor);
            assessment.setAssessmentDate(Date.valueOf(LocalDate.now()));
        }

        model.addAttribute("assessment", assessment);
        model.addAttribute("riskLevels", RiskAssessment.RiskLevel.values());
        model.addAttribute("statuses", RiskAssessment.Status.values());
        return "advisor/assessment-form"; // Returns advisor/assessment-form.html
    }

    @PostMapping("/assessments/save")
    public String saveAssessment(@ModelAttribute("assessment") RiskAssessment assessment, RedirectAttributes redirectAttributes) {
        try {
            advisorService.saveRiskAssessment(assessment);
            redirectAttributes.addFlashAttribute("successMessage", "Risk assessment saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/advisor/dashboard";
    }
}