package com.life_insurance_system.controller;

import com.life_insurance_system.dto.UserRegistrationDto;
import com.life_insurance_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Returns login.html
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register"; // Returns register.html
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("userDto") UserRegistrationDto registrationDto, RedirectAttributes redirectAttributes) {
        try {
            userService.registerNewCustomer(registrationDto);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }
}