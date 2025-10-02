package com.life_insurance_system.controller;

import com.life_insurance_system.dto.UserRegistrationDto;
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
        return "executive/dashboard";
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        model.addAttribute("customers", executiveService.findAllCustomers());
        return "executive/customer-list";
    }

    @GetMapping("/customers/register")
    public String showRegisterNewCustomerForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "executive/register-customer";
    }

    @PostMapping("/customers/register")
    public String registerNewCustomer(@ModelAttribute("userDto") UserRegistrationDto registrationDto, RedirectAttributes redirectAttributes) {
        try {
            executiveService.registerNewCustomer(registrationDto);
            redirectAttributes.addFlashAttribute("successMessage", "New customer registered successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/executive/customers";
    }


    @GetMapping("/customers/edit/{id}")
    public String showEditCustomerForm(@PathVariable("id") int id, Model model) {
        Customer customer = executiveService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "executive/customer-form";
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
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/executive/customers";
    }

    @GetMapping("/customers/deactivate/{id}")
    public String deactivateCustomer(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            executiveService.deactivateCustomerAccount(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer account deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/executive/customers";
    }
}