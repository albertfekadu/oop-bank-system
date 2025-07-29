package com.waribank.exception;

/**
 * Custom exception thrown when a customer is not found
 * 
 * @author Albert Fekadu Wari
 */
public class CustomerNotFoundException extends Exception {
    
    private int customerId;
    private String nationalId;
    private String email;
    
    public CustomerNotFoundException(String message) {
        super(message);
    }
    
    public CustomerNotFoundException(String message, int customerId) {
        super(message);
        this.customerId = customerId;
    }
    
    public CustomerNotFoundException(String message, String nationalId) {
        super(message);
        this.nationalId = nationalId;
    }
    
    public CustomerNotFoundException(String message, String email, boolean isEmail) {
        super(message);
        if (isEmail) {
            this.email = email;
        } else {
            this.nationalId = email;
        }
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public String getNationalId() {
        return nationalId;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public String getMessage() {
        if (customerId > 0) {
            return String.format("Customer not found with ID: %d", customerId);
        } else if (nationalId != null) {
            return String.format("Customer not found with National ID: %s", nationalId);
        } else if (email != null) {
            return String.format("Customer not found with Email: %s", email);
        }
        return super.getMessage();
    }
} 