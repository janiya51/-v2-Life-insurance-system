package com.life_insurance_system.controller;

import com.life_insurance_system.model.PolicyDispute;
import com.life_insurance_system.model.User;
import com.life_insurance_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    private User getAuthenticatedAdmin(Principal principal) {
        return adminService.findUserByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated admin not found"));
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard";
    }

    // --- User Management ---
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", adminService.findAllStaffUsers());
        return "admin/user-list";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        return "admin/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            if (user.getUserId() == 0) {
                adminService.createStaffUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            } else {
                adminService.updateUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") int id, Model model) {
        User user = adminService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", User.Role.values());
        return "admin/user-form";
    }

    @GetMapping("/users/deactivate/{id}")
    public String deactivateUser(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        adminService.deactivateUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deactivated successfully!");
        return "redirect:/admin/users";
    }

    // --- Policy Dispute Management ---
    @GetMapping("/disputes")
    public String listDisputes(Model model) {
        model.addAttribute("disputes", adminService.findAllDisputes());
        return "admin/dispute-list";
    }

    @PostMapping("/disputes/update/{id}")
    public String updateDisputeStatus(@PathVariable("id") int id, @RequestParam("status") PolicyDispute.Status status, RedirectAttributes redirectAttributes) {
        try {
            adminService.updateDisputeStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Dispute status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/disputes";
    }

    @GetMapping("/disputes/delete/{id}")
    public String deleteDispute(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteDispute(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dispute deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/disputes";
    }

    // --- Admin Profile Management ---
    @GetMapping("/profile")
    public String showAdminProfile(Model model, Principal principal) {
        model.addAttribute("user", getAuthenticatedAdmin(principal));
        return "admin/profile";
    }

    @PostMapping("/profile/update")
    public String updateAdminProfile(@ModelAttribute("user") User user, Principal principal, RedirectAttributes redirectAttributes) {
        User authUser = getAuthenticatedAdmin(principal);
        // Ensure admins can only update their own profile and not change their ID or role
        user.setUserId(authUser.getUserId());
        user.setRole(authUser.getRole());
        user.setPassword(authUser.getPassword()); // Prevent password changes on this form
        adminService.updateAdminProfile(user);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/admin/profile";
    }
}