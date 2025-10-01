package com.life_insurance_system.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            switch (authorityName) {
                case "ROLE_CUSTOMER":
                    return "/customer/dashboard";
                case "ROLE_CUSTOMER_SERVICE_EXEC":
                    return "/executive/dashboard";
                case "ROLE_SENIOR_ADVISOR":
                    return "/advisor/dashboard";
                case "ROLE_FINANCE_OFFICER":
                    return "/finance/dashboard";
                case "ROLE_IT_ANALYST":
                    return "/analyst/dashboard";
                case "ROLE_HR_ADMIN":
                    return "/admin/dashboard";
                default:
                    break;
            }
        }
        // If no role matches, redirect to a generic error or login page
        throw new IllegalStateException("User has no recognized role.");
    }
}