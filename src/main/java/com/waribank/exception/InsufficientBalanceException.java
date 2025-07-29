package com.waribank.exception;

/**
 * Custom exception thrown when account has insufficient balance for a transaction
 * 
 * @author Albert Fekadu Wari
 */
public class InsufficientBalanceException extends Exception {
    
    private double requestedAmount;
    private double availableBalance;
    
    public InsufficientBalanceException(String message) {
        super(message);
    }
    
    public InsufficientBalanceException(String message, double requestedAmount, double availableBalance) {
        super(message);
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }
    
    public double getRequestedAmount() {
        return requestedAmount;
    }
    
    public double getAvailableBalance() {
        return availableBalance;
    }
    
    @Override
    public String getMessage() {
        if (requestedAmount > 0 && availableBalance >= 0) {
            return String.format("Insufficient balance. Requested: %.2f, Available: %.2f", 
                               requestedAmount, availableBalance);
        }
        return super.getMessage();
    }
} 