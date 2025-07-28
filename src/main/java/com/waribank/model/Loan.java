package com.waribank.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Loan entity representing a loan application and disbursement
 * 
 * @author Albert Fekadu Wari
 */
public class Loan {
    private int loanId;
    private int customerId;
    private int accountId;
    private double loanAmount;
    private double interestRate;
    private int termInMonths;
    private String loanType; // PERSONAL, BUSINESS, EDUCATION, AGRICULTURE
    private String purpose;
    private LocalDateTime applicationDate;
    private LocalDateTime approvalDate;
    private LocalDateTime disbursementDate;
    private LocalDateTime dueDate;
    private String status; // PENDING, APPROVED, DISBURSED, ACTIVE, COMPLETED, DEFAULTED
    private double monthlyPayment;
    private double remainingBalance;
    private String approvedBy;
    private String rejectionReason;

    // Default constructor
    public Loan() {
        this.applicationDate = LocalDateTime.now();
        this.status = "PENDING";
        this.remainingBalance = 0.0;
        this.monthlyPayment = 0.0;
    }

    // Parameterized constructor
    public Loan(int customerId, int accountId, double loanAmount, int termInMonths, 
                String loanType, String purpose) {
        this();
        this.customerId = customerId;
        this.accountId = accountId;
        this.loanAmount = loanAmount;
        this.termInMonths = termInMonths;
        this.loanType = loanType;
        this.purpose = purpose;
        this.remainingBalance = loanAmount;
        setLoanDefaults();
    }

    // Getters and Setters
    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getTermInMonths() {
        return termInMonths;
    }

    public void setTermInMonths(int termInMonths) {
        this.termInMonths = termInMonths;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
        setLoanDefaults();
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDateTime disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    // Business methods
    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isApproved() {
        return "APPROVED".equals(status);
    }

    public boolean isDisbursed() {
        return "DISBURSED".equals(status);
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isDefaulted() {
        return "DEFAULTED".equals(status);
    }

    public void approve(String approvedBy) {
        this.status = "APPROVED";
        this.approvalDate = LocalDateTime.now();
        this.approvedBy = approvedBy;
        calculateMonthlyPayment();
    }

    public void reject(String reason) {
        this.status = "REJECTED";
        this.rejectionReason = reason;
    }

    public void disburse() {
        this.status = "DISBURSED";
        this.disbursementDate = LocalDateTime.now();
        this.dueDate = disbursementDate.plusMonths(termInMonths);
        this.status = "ACTIVE";
    }

    public void makePayment(double amount) {
        if (isActive() && amount > 0) {
            this.remainingBalance -= amount;
            if (this.remainingBalance <= 0) {
                this.status = "COMPLETED";
                this.remainingBalance = 0;
            }
        }
    }

    public boolean isOverdue() {
        return isActive() && LocalDateTime.now().isAfter(dueDate);
    }

    public int getMonthsRemaining() {
        if (isActive() && dueDate != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(dueDate)) {
                return (int) java.time.temporal.ChronoUnit.MONTHS.between(now, dueDate);
            }
        }
        return 0;
    }

    public double getTotalInterest() {
        return (loanAmount * interestRate * termInMonths) / (12 * 100);
    }

    public double getTotalAmount() {
        return loanAmount + getTotalInterest();
    }

    // Private helper methods
    private void setLoanDefaults() {
        switch (loanType) {
            case "PERSONAL":
                this.interestRate = 12.0;
                break;
            case "BUSINESS":
                this.interestRate = 10.0;
                break;
            case "EDUCATION":
                this.interestRate = 8.0;
                break;
            case "AGRICULTURE":
                this.interestRate = 6.0;
                break;
            default:
                this.interestRate = 15.0;
        }
    }

    private void calculateMonthlyPayment() {
        if (termInMonths > 0) {
            double monthlyRate = interestRate / (12 * 100);
            double totalAmount = getTotalAmount();
            this.monthlyPayment = totalAmount / termInMonths;
        }
    }

    // Override methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Loan loan = (Loan) obj;
        return loanId == loan.loanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }

    @Override
    public String toString() {
        return String.format("Loan{id=%d, amount=%.2f, type='%s', status='%s', remaining=%.2f}",
                loanId, loanAmount, loanType, status, remainingBalance);
    }
} 