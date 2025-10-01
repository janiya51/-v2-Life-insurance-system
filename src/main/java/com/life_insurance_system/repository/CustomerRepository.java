package com.life_insurance_system.repository;

import com.life_insurance_system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    /**
     * Finds a customer by the username of their associated User account.
     * @param username The username to search for.
     * @return An Optional containing the customer if found.
     */
    Optional<Customer> findByUser_Username(String username);
}