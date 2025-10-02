package com.life_insurance_system.aop;

import com.life_insurance_system.model.Customer;
import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.UserRepository;
import com.life_insurance_system.service.SystemLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final SystemLogService logService;
    private final UserRepository userRepository;

    @Autowired
    public LoggingAspect(SystemLogService logService, UserRepository userRepository) {
        this.logService = logService;
        this.userRepository = userRepository;
    }

    /**
     * Logs successful user authentications.
     */
    @AfterReturning(
            pointcut = "execution(* com.life_insurance_system.service.CustomAuthenticationSuccessHandler.onAuthenticationSuccess(..)) && args(request, response, authentication)",
            argNames = "request, response, authentication"
    )
    public void logSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            userRepository.findByUsername(username).ifPresent(user -> {
                logService.logAction(user, "LOGIN_SUCCESS");
            });
        }
    }

    /**
     * Logs when a customer's contact information is updated by an executive.
     */
    @AfterReturning(
            pointcut = "execution(* com.life_insurance_system.service.CustomerServiceExecutiveService.updateCustomerContact(..))",
            returning = "result"
    )
    public void logCustomerUpdate(JoinPoint joinPoint, Object result) {
        if (result instanceof Customer customer) {
            // In a real scenario, we'd get the currently logged-in executive.
            // For simplicity, we'll log this as a system-like event.
            logService.logAction(null, "UPDATE_CUSTOMER_CONTACT", "Customer", customer.getCustomerId());
        }
    }
}