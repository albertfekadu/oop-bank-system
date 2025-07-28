package com.waribank.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

/**
 * Account entity representing a bank account
 * 
 * @author Albert Fekadu Wari
 */
public class Account extends BankEntity implements Transactionable, Reportable {
    private int accountId;
    private int customerId;
    private String accountNumber;
    private String accountType; // SAVINGS, CHECKING, FIXED_DEPOSIT
    private double balance;
    private double interestRate;
    private LocalDateTime openingDate;
    private LocalDateTime lastTransactionDate;
    private String status; // ACTIVE, FROZEN, CLOSED
    private double minimumBalance;
    private double dailyWithdrawalLimit;
    private double monthlyWithdrawalLimit;

    // Default constructor
    public Account() {
        super();
        this.openingDate = LocalDateTime.now();
        this.lastTransactionDate = LocalDateTime.now();
        this.balance = 0.0;
        this.interestRate = 0.0;
        this.minimumBalance = 0.0;
        this.dailyWithdrawalLimit = 10000.0;
        this.monthlyWithdrawalLimit = 100000.0;
    }

    // Parameterized constructor
    public Account(int customerId, String accountType, double initialBalance) {
        this();
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.accountNumber = generateAccountNumber();
        setAccountDefaults();
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
        setAccountDefaults();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public double getMonthlyWithdrawalLimit() {
        return monthlyWithdrawalLimit;
    }

    public void setMonthlyWithdrawalLimit(double monthlyWithdrawalLimit) {
        this.monthlyWithdrawalLimit = monthlyWithdrawalLimit;
    }

    // Business methods
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean hasSufficientBalance(double amount) {
        return balance >= amount;
    }

    public boolean meetsMinimumBalance() {
        return balance >= minimumBalance;
    }

    public void deposit(double amount) {
        if (amount > 0 && isActive()) {
            this.balance += amount;
            this.lastTransactionDate = LocalDateTime.now();
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && isActive() && hasSufficientBalance(amount)) {
            this.balance -= amount;
            this.lastTransactionDate = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public void addInterest() {
        if (isActive() && balance > 0) {
            double interest = balance * (interestRate / 100);
            this.balance += interest;
        }
    }

    public void freeze() {
        this.status = "FROZEN";
    }

    public void unfreeze() {
        this.status = "ACTIVE";
    }

    public void close() {
        this.status = "CLOSED";
    }

    // Private helper methods
    private String generateAccountNumber() {
        // Simple account number generation - in real app, use more sophisticated logic
        return "WB" + System.currentTimeMillis() % 1000000;
    }

    private void setAccountDefaults() {
        switch (accountType) {
            case "SAVINGS":
                this.interestRate = 2.5;
                this.minimumBalance = 100.0;
                this.dailyWithdrawalLimit = 5000.0;
                this.monthlyWithdrawalLimit = 50000.0;
                break;
            case "CHECKING":
                this.interestRate = 0.5;
                this.minimumBalance = 0.0;
                this.dailyWithdrawalLimit = 10000.0;
                this.monthlyWithdrawalLimit = 100000.0;
                break;
            case "FIXED_DEPOSIT":
                this.interestRate = 8.0;
                this.minimumBalance = 1000.0;
                this.dailyWithdrawalLimit = 0.0; // No withdrawals for fixed deposits
                this.monthlyWithdrawalLimit = 0.0;
                break;
            default:
                this.interestRate = 1.0;
                this.minimumBalance = 0.0;
                this.dailyWithdrawalLimit = 5000.0;
                this.monthlyWithdrawalLimit = 50000.0;
        }
    }

    // Override methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return accountId == account.accountId && 
               Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber);
    }

    // Interface implementations
    @Override
    public String getDisplayName() {
        return "Account " + accountNumber;
    }
    
    @Override
    public boolean isValid() {
        return accountNumber != null && !accountNumber.isEmpty() && 
               accountType != null && !accountType.isEmpty();
    }
    
    // Transactionable interface implementation
    @Override
    public boolean canPerformTransaction() {
        return isActive() && balance >= minimumBalance;
    }
    
    @Override
    public void updateBalance(double amount) {
        this.balance += amount;
        this.lastTransactionDate = LocalDateTime.now();
    }
    
    @Override
    public double getTransactionLimit() {
        return dailyWithdrawalLimit;
    }
    
    // Reportable interface implementation
    @Override
    public String generateSummary() {
        return String.format("Account %s: %s, Balance: %.2f, Status: %s", 
                           accountNumber, accountType, balance, status);
    }
    
    @Override
    public List<String> getDetails() {
        List<String> details = new ArrayList<>();
        details.add("Account ID: " + accountId);
        details.add("Account Number: " + accountNumber);
        details.add("Type: " + accountType);
        details.add("Balance: " + balance);
        details.add("Interest Rate: " + interestRate + "%");
        details.add("Status: " + status);
        details.add("Opening Date: " + openingDate);
        return details;
    }
    
    @Override
    public boolean isReportAvailable() {
        return accountId > 0 && accountNumber != null;
    }
    
    // Method overriding example
    @Override
    public void updateStatus(String newStatus) {
        super.updateStatus(newStatus);
        if ("FROZEN".equals(newStatus)) {
            // Additional logic for frozen accounts
        }
    }
    
    @Override
    public String toString() {
        return String.format("Account{id=%d, number='%s', type='%s', balance=%.2f, status='%s'}",
                accountId, accountNumber, accountType, balance, status);
    }
} 