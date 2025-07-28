package com.waribank.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction entity representing a bank transaction
 * 
 * @author Albert Fekadu Wari
 */
public class Transaction {
    private int transactionId;
    private int accountId;
    private String transactionType; // DEPOSIT, WITHDRAWAL, TRANSFER, LOAN_DISBURSEMENT, LOAN_REPAYMENT
    private double amount;
    private String description;
    private LocalDateTime transactionDate;
    private String status; // PENDING, COMPLETED, FAILED, CANCELLED
    private String referenceNumber;
    private Integer toAccountId; // For transfers
    private double balanceAfterTransaction;

    // Default constructor
    public Transaction() {
        this.transactionDate = LocalDateTime.now();
        this.status = "PENDING";
        this.referenceNumber = generateReferenceNumber();
    }

    // Parameterized constructor
    public Transaction(int accountId, String transactionType, double amount, String description) {
        this();
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Integer getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(double balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    // Business methods
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    public void markCompleted() {
        this.status = "COMPLETED";
    }

    public void markFailed() {
        this.status = "FAILED";
    }

    public void markCancelled() {
        this.status = "CANCELLED";
    }

    public boolean isCredit() {
        return "DEPOSIT".equals(transactionType) || 
               "TRANSFER".equals(transactionType) && toAccountId != null;
    }

    public boolean isDebit() {
        return "WITHDRAWAL".equals(transactionType) || 
               "TRANSFER".equals(transactionType) && toAccountId == null;
    }

    public String getFormattedAmount() {
        String sign = isCredit() ? "+" : "-";
        return sign + String.format("%.2f", amount);
    }

    // Private helper methods
    private String generateReferenceNumber() {
        // Simple reference number generation
        return "TXN" + System.currentTimeMillis() % 1000000;
    }

    // Override methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction transaction = (Transaction) obj;
        return transactionId == transaction.transactionId && 
               Objects.equals(referenceNumber, transaction.referenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, referenceNumber);
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%d, type='%s', amount=%.2f, status='%s', ref='%s'}",
                transactionId, transactionType, amount, status, referenceNumber);
    }
} 