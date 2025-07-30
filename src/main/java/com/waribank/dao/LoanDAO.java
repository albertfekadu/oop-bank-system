package com.waribank.dao;

import com.waribank.database.DatabaseManager;
import com.waribank.model.Loan;
import com.waribank.exception.CustomerNotFoundException;
import com.waribank.exception.AccountNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Data Access Object for Loan entity
 * 
 * @author Albert Fekadu Wari
 */
public class LoanDAO {
    private static final Logger LOGGER = Logger.getLogger(LoanDAO.class.getName());
    private final DatabaseManager dbManager;
    
    public LoanDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Create a new loan
     */
    public Loan createLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (customer_id, account_id, loan_amount, interest_rate, term_in_months, " +
                    "loan_type, purpose, application_date, status, monthly_payment, remaining_balance) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, loan.getCustomerId());
            pstmt.setInt(2, loan.getAccountId());
            pstmt.setDouble(3, loan.getLoanAmount());
            pstmt.setDouble(4, loan.getInterestRate());
            pstmt.setInt(5, loan.getTermInMonths());
            pstmt.setString(6, loan.getLoanType());
            pstmt.setString(7, loan.getPurpose());
            pstmt.setTimestamp(8, Timestamp.valueOf(loan.getApplicationDate()));
            pstmt.setString(9, loan.getStatus());
            pstmt.setDouble(10, loan.getMonthlyPayment());
            pstmt.setDouble(11, loan.getRemainingBalance());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating loan failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setLoanId(generatedKeys.getInt(1));
                    LOGGER.info("Loan created with ID: " + loan.getLoanId());
                    return loan;
                } else {
                    throw new SQLException("Creating loan failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Find loan by ID
     */
    public Loan findById(int loanId) throws SQLException {
        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, loanId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoan(rs);
                } else {
                    return null;
                }
            }
        }
    }
    
    /**
     * Find loans by customer ID
     */
    public List<Loan> findByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM loans WHERE customer_id = ? ORDER BY application_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
        }
        
        return loans;
    }
    
    /**
     * Find loans by account ID
     */
    public List<Loan> findByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM loans WHERE account_id = ? ORDER BY application_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
        }
        
        return loans;
    }
    
    /**
     * Get all loans
     */
    public List<Loan> findAll() throws SQLException {
        String sql = "SELECT * FROM loans ORDER BY application_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        }
        
        return loans;
    }
    
    /**
     * Get pending loans
     */
    public List<Loan> findPendingLoans() throws SQLException {
        String sql = "SELECT * FROM loans WHERE status = 'PENDING' ORDER BY application_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        }
        
        return loans;
    }
    
    /**
     * Get approved loans
     */
    public List<Loan> findApprovedLoans() throws SQLException {
        String sql = "SELECT * FROM loans WHERE status = 'APPROVED' ORDER BY application_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        }
        
        return loans;
    }
    
    /**
     * Get active loans
     */
    public List<Loan> findActiveLoans() throws SQLException {
        String sql = "SELECT * FROM loans WHERE status = 'ACTIVE' ORDER BY application_date DESC";
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        }
        
        return loans;
    }
    
    /**
     * Update loan
     */
    public boolean updateLoan(Loan loan) throws SQLException {
        String sql = "UPDATE loans SET customer_id = ?, account_id = ?, loan_amount = ?, interest_rate = ?, " +
                    "term_in_months = ?, loan_type = ?, purpose = ?, application_date = ?, " +
                    "approval_date = ?, disbursement_date = ?, due_date = ?, status = ?, " +
                    "monthly_payment = ?, remaining_balance = ?, approved_by = ?, rejection_reason = ? " +
                    "WHERE loan_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, loan.getCustomerId());
            pstmt.setInt(2, loan.getAccountId());
            pstmt.setDouble(3, loan.getLoanAmount());
            pstmt.setDouble(4, loan.getInterestRate());
            pstmt.setInt(5, loan.getTermInMonths());
            pstmt.setString(6, loan.getLoanType());
            pstmt.setString(7, loan.getPurpose());
            pstmt.setTimestamp(8, Timestamp.valueOf(loan.getApplicationDate()));
            
            if (loan.getApprovalDate() != null) {
                pstmt.setTimestamp(9, Timestamp.valueOf(loan.getApprovalDate()));
            } else {
                pstmt.setNull(9, Types.TIMESTAMP);
            }
            
            if (loan.getDisbursementDate() != null) {
                pstmt.setTimestamp(10, Timestamp.valueOf(loan.getDisbursementDate()));
            } else {
                pstmt.setNull(10, Types.TIMESTAMP);
            }
            
            if (loan.getDueDate() != null) {
                pstmt.setTimestamp(11, Timestamp.valueOf(loan.getDueDate()));
            } else {
                pstmt.setNull(11, Types.TIMESTAMP);
            }
            
            pstmt.setString(12, loan.getStatus());
            pstmt.setDouble(13, loan.getMonthlyPayment());
            pstmt.setDouble(14, loan.getRemainingBalance());
            pstmt.setString(15, loan.getApprovedBy());
            pstmt.setString(16, loan.getRejectionReason());
            pstmt.setInt(17, loan.getLoanId());
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Loan updated. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete loan
     */
    public boolean deleteLoan(int loanId) throws SQLException {
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, loanId);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Loan deleted. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Get loan statistics
     */
    public LoanStatistics getLoanStatistics() throws SQLException {
        String sql = """
            SELECT 
                COUNT(*) as total_loans,
                SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) as pending_loans,
                SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END) as approved_loans,
                SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) as active_loans,
                SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) as completed_loans,
                SUM(loan_amount) as total_loan_amount,
                SUM(remaining_balance) as total_remaining_balance
            FROM loans
        """;
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return new LoanStatistics(
                    rs.getLong("total_loans"),
                    rs.getLong("pending_loans"),
                    rs.getLong("approved_loans"),
                    rs.getLong("active_loans"),
                    rs.getLong("completed_loans"),
                    rs.getDouble("total_loan_amount"),
                    rs.getDouble("total_remaining_balance")
                );
            }
            
            return new LoanStatistics(0, 0, 0, 0, 0, 0.0, 0.0);
        }
    }
    
    /**
     * Map ResultSet to Loan object
     */
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setCustomerId(rs.getInt("customer_id"));
        loan.setAccountId(rs.getInt("account_id"));
        loan.setLoanAmount(rs.getDouble("loan_amount"));
        loan.setInterestRate(rs.getDouble("interest_rate"));
        loan.setTermInMonths(rs.getInt("term_in_months"));
        loan.setLoanType(rs.getString("loan_type"));
        loan.setPurpose(rs.getString("purpose"));
        loan.setApplicationDate(rs.getTimestamp("application_date").toLocalDateTime());
        
        Timestamp approvalDate = rs.getTimestamp("approval_date");
        if (approvalDate != null) {
            loan.setApprovalDate(approvalDate.toLocalDateTime());
        }
        
        Timestamp disbursementDate = rs.getTimestamp("disbursement_date");
        if (disbursementDate != null) {
            loan.setDisbursementDate(disbursementDate.toLocalDateTime());
        }
        
        Timestamp dueDate = rs.getTimestamp("due_date");
        if (dueDate != null) {
            loan.setDueDate(dueDate.toLocalDateTime());
        }
        
        loan.setStatus(rs.getString("status"));
        loan.setMonthlyPayment(rs.getDouble("monthly_payment"));
        loan.setRemainingBalance(rs.getDouble("remaining_balance"));
        loan.setApprovedBy(rs.getString("approved_by"));
        loan.setRejectionReason(rs.getString("rejection_reason"));
        
        return loan;
    }
    
    /**
     * Inner class for loan statistics
     */
    public static class LoanStatistics {
        private final long totalLoans;
        private final long pendingLoans;
        private final long approvedLoans;
        private final long activeLoans;
        private final long completedLoans;
        private final double totalLoanAmount;
        private final double totalRemainingBalance;
        
        public LoanStatistics(long totalLoans, long pendingLoans, long approvedLoans, 
                            long activeLoans, long completedLoans, double totalLoanAmount, 
                            double totalRemainingBalance) {
            this.totalLoans = totalLoans;
            this.pendingLoans = pendingLoans;
            this.approvedLoans = approvedLoans;
            this.activeLoans = activeLoans;
            this.completedLoans = completedLoans;
            this.totalLoanAmount = totalLoanAmount;
            this.totalRemainingBalance = totalRemainingBalance;
        }
        
        // Getters
        public long getTotalLoans() { return totalLoans; }
        public long getPendingLoans() { return pendingLoans; }
        public long getApprovedLoans() { return approvedLoans; }
        public long getActiveLoans() { return activeLoans; }
        public long getCompletedLoans() { return completedLoans; }
        public double getTotalLoanAmount() { return totalLoanAmount; }
        public double getTotalRemainingBalance() { return totalRemainingBalance; }
    }
} 