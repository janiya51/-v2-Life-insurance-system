package com.life_insurance_system.service;

import com.life_insurance_system.dto.UserRegistrationDto;
import com.life_insurance_system.model.User;

public interface UserService {
    /**
     * Registers a new customer in the system.
     *
     * @param registrationDto DTO containing the new user's information.
     * @return The created User object.
     * @throws Exception if the username or email already exists.
     */
    User registerNewCustomer(UserRegistrationDto registrationDto) throws Exception;
}