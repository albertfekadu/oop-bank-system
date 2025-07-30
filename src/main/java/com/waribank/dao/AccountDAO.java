package com.waribank.dao;

import com.waribank.database.DatabaseManager;
import com.waribank.model.Account;
import com.waribank.exception.AccountNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Data Access Object for Account entity
 * 
 * @author Albert Fekadu Wari
 */
public class AccountDAO {
    private static final Logger LOGGER = Logger.getLogger(AccountDAO.class.getName());
    private final DatabaseManager dbManager;
    
    public AccountDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Create a new account
     */
    public Account createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (customer_id, account_number, account_type, balance, interest_rate, " +
                    "opening_date, last_transaction_date, status, minimum_balance, " +
                    "daily_withdrawal_limit, monthly_withdrawal_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, account.getCustomerId());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setDouble(5, account.getInterestRate());
            pstmt.setTimestamp(6, Timestamp.valueOf(account.getOpeningDate()));
            pstmt.setTimestamp(7, Timestamp.valueOf(account.getLastTransactionDate()));
            pstmt.setString(8, account.getStatus());
            pstmt.setDouble(9, account.getMinimumBalance());
            pstmt.setDouble(10, account.getDailyWithdrawalLimit());
            pstmt.setDouble(11, account.getMonthlyWithdrawalLimit());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccountId(generatedKeys.getInt(1));
                    LOGGER.info("Account created with ID: " + account.getAccountId());
                    return account;
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Find account by ID
     */
    public Account findById(int accountId) throws SQLException, AccountNotFoundException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                } else {
                    throw new AccountNotFoundException("Account not found", accountId);
                }
            }
        }
    }
    
    /**
     * Find account by account number
     */
    public Account findByAccountNumber(String accountNumber) throws SQLException, AccountNotFoundException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                } else {
                    throw new AccountNotFoundException("Account not found", accountNumber);
                }
            }
        }
    }
    
    /**
     * Find accounts by customer ID
     */
    public List<Account> findByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY account_id";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        
        return accounts;
    }
    
    /**
     * Get all accounts
     */
    public List<Account> findAll() throws SQLException {
        String sql = "SELECT * FROM accounts ORDER BY account_id";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        
        return accounts;
    }
    
    /**
     * Get active accounts
     */
    public List<Account> findActiveAccounts() throws SQLException {
        String sql = "SELECT * FROM accounts WHERE status = 'ACTIVE' ORDER BY account_id";
        List<Account> accounts = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        
        return accounts;
    }
    
    /**
     * Update account
     */
    public boolean updateAccount(Account account) throws SQLException {
        String sql = "UPDATE accounts SET customer_id = ?, account_number = ?, account_type = ?, " +
                    "balance = ?, interest_rate = ?, opening_date = ?, " +
                    "last_transaction_date = ?, status = ?, minimum_balance = ?, " +
                    "daily_withdrawal_limit = ?, monthly_withdrawal_limit = ? WHERE account_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, account.getCustomerId());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setDouble(5, account.getInterestRate());
            pstmt.setTimestamp(6, Timestamp.valueOf(account.getOpeningDate()));
            pstmt.setTimestamp(7, Timestamp.valueOf(account.getLastTransactionDate()));
            pstmt.setString(8, account.getStatus());
            pstmt.setDouble(9, account.getMinimumBalance());
            pstmt.setDouble(10, account.getDailyWithdrawalLimit());
            pstmt.setDouble(11, account.getMonthlyWithdrawalLimit());
            pstmt.setInt(12, account.getAccountId());
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Account updated. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Update account balance
     */
    public boolean updateBalance(int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ?, last_transaction_date = ? WHERE account_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, accountId);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Account balance updated to " + newBalance + ". Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Update account status
     */
    public boolean updateAccountStatus(String accountNumber, String status) throws SQLException {
        String sql = "UPDATE accounts SET status = ? WHERE account_number = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, accountNumber);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Account status updated to " + status + ". Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Close account
     */
    public boolean closeAccount(String accountNumber) throws SQLException {
        String sql = "UPDATE accounts SET status = 'CLOSED' WHERE account_number = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Account closed. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete account
     */
    public boolean deleteAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Account deleted. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Map ResultSet to Account object
     */
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setCustomerId(rs.getInt("customer_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setAccountType(rs.getString("account_type"));
        account.setBalance(rs.getDouble("balance"));
        account.setInterestRate(rs.getDouble("interest_rate"));
        account.setOpeningDate(rs.getTimestamp("opening_date").toLocalDateTime());
        account.setLastTransactionDate(rs.getTimestamp("last_transaction_date").toLocalDateTime());
        account.setStatus(rs.getString("status"));
        account.setMinimumBalance(rs.getDouble("minimum_balance"));
        account.setDailyWithdrawalLimit(rs.getDouble("daily_withdrawal_limit"));
        account.setMonthlyWithdrawalLimit(rs.getDouble("monthly_withdrawal_limit"));
        return account;
    }
} 