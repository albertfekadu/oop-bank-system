package com.waribank.exception;

/**
 * Custom exception thrown when a transaction is invalid
 * 
 * @author Albert Fekadu Wari
 */
public class InvalidTransactionException extends Exception {
    
    private String transactionType;
    private double amount;
    private String reason;
    
    public InvalidTransactionException(String message) {
        super(message);
    }
    
    public InvalidTransactionException(String message, String transactionType, double amount) {
        super(message);
        this.transactionType = transactionType;
        this.amount = amount;
    }
    
    public InvalidTransactionException(String message, String transactionType, double amount, String reason) {
        super(message);
        this.transactionType = transactionType;
        this.amount = amount;
        this.reason = reason;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getReason() {
        return reason;
    }
    
    @Override
    public String getMessage() {
        if (transactionType != null && amount > 0) {
            return String.format("Invalid %s transaction for amount %.2f. %s", 
                               transactionType, amount, reason != null ? reason : "");
        }
        return super.getMessage();
    }
} 