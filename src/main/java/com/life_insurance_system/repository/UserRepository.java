package com.life_insurance_system.repository;

import com.life_insurance_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email.
     * @param email The email to search for.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    Optional<User> findByEmail(String email);
}