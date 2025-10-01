package com.life_insurance_system.service;

import com.life_insurance_system.model.Customer;
import com.life_insurance_system.model.User;
import com.life_insurance_system.repository.CustomerRepository;
import com.life_insurance_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceExecutiveServiceImpl implements CustomerServiceExecutiveService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerServiceExecutiveServiceImpl(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findCustomerById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional
    public Customer updateCustomerContact(int customerId, String phone, String address) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        User user = customer.getUser();
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);

        return customer;
    }

    @Override
    @Transactional
    public void deactivateCustomerAccount(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        User user = customer.getUser();
        user.setActive(false);
        userRepository.save(user);
    }
}