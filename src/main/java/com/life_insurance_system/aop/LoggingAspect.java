package com.life_insurance_system.aop;

import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.UserRepository;
import com.life_insurance_system.service.SystemLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
     * This advice runs after the onAuthenticationSuccess method in CustomAuthenticationSuccessHandler completes successfully.
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
}