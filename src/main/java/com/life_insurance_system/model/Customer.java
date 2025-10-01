package com.life_insurance_system.model;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "national_id", unique = true, length = 50)
    private String nationalId;

    @Column(length = 100)
    private String occupation;

    // Constructors
    public Customer() {
    }

    public Customer(User user, Date dateOfBirth, String nationalId, String occupation) {
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.nationalId = nationalId;
        this.occupation = occupation;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}