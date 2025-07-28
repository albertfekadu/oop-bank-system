package com.waribank.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

/**
 * Customer entity representing a bank customer
 * 
 * @author Albert Fekadu Wari
 */
public class Customer extends BankEntity implements Reportable {
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String nationalId;
    private LocalDateTime registrationDate;
    private String status; // ACTIVE, INACTIVE, SUSPENDED
    private double creditScore;

    // Default constructor
    public Customer() {
        super();
        this.registrationDate = LocalDateTime.now();
        this.creditScore = 0.0;
    }

    // Parameterized constructor
    public Customer(String firstName, String lastName, String email, String phoneNumber, 
                   String address, String nationalId) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nationalId = nationalId;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(double creditScore) {
        this.creditScore = creditScore;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Business methods
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public void activate() {
        this.status = "ACTIVE";
    }

    public void suspend() {
        this.status = "SUSPENDED";
    }

    public void deactivate() {
        this.status = "INACTIVE";
    }

    public void updateCreditScore(double newScore) {
        this.creditScore = Math.max(0, Math.min(1000, newScore));
    }

    // Override methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return customerId == customer.customerId && 
               Objects.equals(nationalId, customer.nationalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, nationalId);
    }

    // Interface implementations
    @Override
    public String getDisplayName() {
        return getFullName();
    }
    
    @Override
    public boolean isValid() {
        return firstName != null && !firstName.isEmpty() && 
               lastName != null && !lastName.isEmpty() && 
               email != null && !email.isEmpty();
    }
    
    // Reportable interface implementation
    @Override
    public String generateSummary() {
        return String.format("Customer %s: %s, Email: %s, Status: %s", 
                           customerId, getFullName(), email, status);
    }
    
    @Override
    public List<String> getDetails() {
        List<String> details = new ArrayList<>();
        details.add("Customer ID: " + customerId);
        details.add("Name: " + getFullName());
        details.add("Email: " + email);
        details.add("Phone: " + phoneNumber);
        details.add("Address: " + address);
        details.add("National ID: " + nationalId);
        details.add("Status: " + status);
        details.add("Credit Score: " + creditScore);
        details.add("Registration Date: " + registrationDate);
        return details;
    }
    
    @Override
    public boolean isReportAvailable() {
        return customerId > 0 && firstName != null && lastName != null;
    }
    
    @Override
    public String toString() {
        return String.format("Customer{id=%d, name='%s %s', email='%s', status='%s', creditScore=%.2f}",
                customerId, firstName, lastName, email, status, creditScore);
    }
} 