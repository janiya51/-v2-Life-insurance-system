package com.life_insurance_system.service;

import com.life_insurance_system.model.Customer;
import com.life_insurance_system.model.User;

import java.util.List;
import java.util.Optional;

public interface CustomerServiceExecutiveService {

    /**
     * Finds all customers.
     * @return A list of all customers.
     */
    List<Customer> findAllCustomers();

    /**
     * Finds a customer by their ID.
     * @param id The ID of the customer to find.
     * @return An Optional containing the customer if found.
     */
    Optional<Customer> findCustomerById(int id);

    /**
     * Updates a customer's contact details.
     * @param customerId The ID of the customer to update.
     * @param phone The new phone number.
     * @param address The new address.
     * @return The updated customer.
     */
    Customer updateCustomerContact(int customerId, String phone, String address);

    /**
     * Deactivates a customer's account.
     * @param customerId The ID of the customer to deactivate.
     */
    void deactivateCustomerAccount(int customerId);
}