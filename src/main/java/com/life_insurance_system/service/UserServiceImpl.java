package com.life_insurance_system.service;

import com.life_insurance_system.dto.UserRegistrationDto;
import com.life_insurance_system.model.Customer;
import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.CustomerRepository;
import com.life_insurance_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public User registerNewCustomer(UserRegistrationDto registrationDto) throws Exception {
        // Check if username or email already exists
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new Exception("Username already exists: " + registrationDto.getUsername());
        }
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new Exception("Email already exists: " + registrationDto.getEmail());
        }

        // Create a new User entity
        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(registrationDto.getPassword()); // Storing as plain text as per requirement
        newUser.setFullName(registrationDto.getFullName());
        newUser.setPhone(registrationDto.getPhone());
        newUser.setAddress(registrationDto.getAddress());
        newUser.setRole(User.Role.CUSTOMER); // Default role for public registration

        // Save the new user
        User savedUser = userRepository.save(newUser);

        // Create and save the corresponding Customer entity
        Customer newCustomer = new Customer();
        newCustomer.setUser(savedUser);
        // Other customer-specific fields like dateOfBirth can be set here if collected
        customerRepository.save(newCustomer);

        return savedUser;
    }
}