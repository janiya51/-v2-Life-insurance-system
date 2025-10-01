package com.life_insurance_system.controller;

import com.life_insurance_system.model.PolicyDispute;
import com.life_insurance_system.model.User;
import com.life_insurance_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard"; // Returns admin/dashboard.html
    }

    // --- User Management Endpoints ---

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", adminService.findAllStaffUsers());
        return "admin/user-list"; // Returns admin/user-list.html
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values()); // Provide all roles to the form
        return "admin/user-form"; // Returns admin/user-form.html
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
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
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

    // --- Policy Dispute Endpoints ---

    @GetMapping("/disputes")
    public String listDisputes(Model model) {
        model.addAttribute("disputes", adminService.findAllDisputes());
        return "admin/dispute-list"; // Returns admin/dispute-list.html
    }

    @PostMapping("/disputes/update/{id}")
    public String updateDisputeStatus(@PathVariable("id") int id, @RequestParam("status") PolicyDispute.Status status, RedirectAttributes redirectAttributes) {
        try {
            adminService.updateDisputeStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Dispute status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/disputes";
    }

    @GetMapping("/disputes/delete/{id}")
    public String deleteDispute(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        adminService.deleteDispute(id);
        redirectAttributes.addFlashAttribute("successMessage", "Dispute deleted successfully!");
        return "redirect:/admin/disputes";
    }
}