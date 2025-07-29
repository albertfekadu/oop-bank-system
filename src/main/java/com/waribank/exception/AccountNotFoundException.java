package com.waribank.exception;

/**
 * Custom exception thrown when an account is not found
 * 
 * @author Albert Fekadu Wari
 */
public class AccountNotFoundException extends Exception {
    
    private String accountNumber;
    private int accountId;
    
    public AccountNotFoundException(String message) {
        super(message);
    }
    
    public AccountNotFoundException(String message, String accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }
    
    public AccountNotFoundException(String message, int accountId) {
        super(message);
        this.accountId = accountId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public int getAccountId() {
        return accountId;
    }
    
    @Override
    public String getMessage() {
        if (accountNumber != null) {
            return String.format("Account not found with number: %s", accountNumber);
        } else if (accountId > 0) {
            return String.format("Account not found with ID: %d", accountId);
        }
        return super.getMessage();
    }
} 