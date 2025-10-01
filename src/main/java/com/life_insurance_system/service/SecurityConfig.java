package com.life_insurance_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, AuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        // NOTE: NoOpPasswordEncoder is for development and testing ONLY.
        // For production, use a strong encoder like BCryptPasswordEncoder.
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access to static resources, login, and registration pages
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/register").permitAll()
                        // Restrict access based on roles
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/executive/**").hasRole("CUSTOMER_SERVICE_EXEC")
                        .requestMatchers("/advisor/**").hasRole("SENIOR_ADVISOR")
                        .requestMatchers("/finance/**").hasRole("FINANCE_OFFICER")
                        .requestMatchers("/analyst/**").hasRole("IT_ANALYST")
                        .requestMatchers("/admin/**").hasRole("HR_ADMIN")
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // The URL to submit the username and password to
                        .successHandler(customAuthenticationSuccessHandler) // Use custom handler for role-based redirect
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}