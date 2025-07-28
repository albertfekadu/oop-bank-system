package com.waribank.model;

/**
 * Interface for entities that can perform transactions
 * 
 * @author Albert Fekadu Wari
 */
public interface Transactionable {
    
    /**
     * Check if the entity can perform a transaction
     */
    boolean canPerformTransaction();
    
    /**
     * Get the current balance
     */
    double getBalance();
    
    /**
     * Update the balance
     */
    void updateBalance(double amount);
    
    /**
     * Get transaction limit
     */
    double getTransactionLimit();
} 