package com.life_insurance_system.controller;

import com.life_insurance_system.model.Customer;
import com.life_insurance_system.service.CustomerServiceExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/executive")
public class CustomerServiceExecutiveController {

    private final CustomerServiceExecutiveService executiveService;

    @Autowired
    public CustomerServiceExecutiveController(CustomerServiceExecutiveService executiveService) {
        this.executiveService = executiveService;
    }

    @GetMapping("/dashboard")
    public String showExecutiveDashboard(Model model) {
        model.addAttribute("customers", executiveService.findAllCustomers());
        return "executive/dashboard"; // Returns executive/dashboard.html
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        model.addAttribute("customers", executiveService.findAllCustomers());
        return "executive/customer-list"; // Returns executive/customer-list.html
    }

    @GetMapping("/customers/edit/{id}")
    public String showEditCustomerForm(@PathVariable("id") int id, Model model) {
        Customer customer = executiveService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "executive/customer-form"; // Returns executive/customer-form.html
    }

    @PostMapping("/customers/update")
    public String updateCustomerContact(@RequestParam("customerId") int customerId,
                                        @RequestParam("phone") String phone,
                                        @RequestParam("address") String address,
                                        RedirectAttributes redirectAttributes) {
        try {
            executiveService.updateCustomerContact(customerId, phone, address);
            redirectAttributes.addFlashAttribute("successMessage", "Customer contact updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/executive/customers";
    }

    @GetMapping("/customers/deactivate/{id}")
    public String deactivateCustomer(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            executiveService.deactivateCustomerAccount(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer account deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/executive/customers";
    }
}