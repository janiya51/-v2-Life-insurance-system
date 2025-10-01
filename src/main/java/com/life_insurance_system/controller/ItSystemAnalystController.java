package com.life_insurance_system.controller;

import com.life_insurance_system.service.ItSystemAnalystService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/analyst")
public class ItSystemAnalystController {

    private final ItSystemAnalystService analystService;

    @Autowired
    public ItSystemAnalystController(ItSystemAnalystService analystService) {
        this.analystService = analystService;
    }

    @GetMapping("/dashboard")
    public String showAnalystDashboard(Model model) {
        model.addAttribute("logs", analystService.findAllLogs());
        return "analyst/dashboard"; // Returns analyst/dashboard.html
    }

    @GetMapping("/logs")
    public String listLogs(Model model) {
        model.addAttribute("logs", analystService.findAllLogs());
        return "analyst/log-list"; // Returns analyst/log-list.html
    }

    @GetMapping("/logs/delete-old")
    public String deleteOldLogs(RedirectAttributes redirectAttributes) {
        try {
            long deletedCount = analystService.deleteOldLogs();
            redirectAttributes.addFlashAttribute("successMessage", deletedCount + " old logs were deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting old logs: " + e.getMessage());
        }
        return "redirect:/analyst/logs";
    }
}