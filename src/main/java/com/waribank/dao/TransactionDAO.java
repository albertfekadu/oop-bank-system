package com.waribank.dao;

import com.waribank.database.DatabaseManager;
import com.waribank.model.Transaction;
import com.waribank.model.Account;
import com.waribank.exception.AccountNotFoundException;
import com.waribank.exception.InsufficientBalanceException;
import com.waribank.exception.InvalidTransactionException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TransactionDAO {
    private static final Logger LOGGER = Logger.getLogger(TransactionDAO.class.getName());
    private final DatabaseManager dbManager;
    private final AccountDAO accountDAO;
    
    public TransactionDAO() {
        this.dbManager = DatabaseManager.getInstance();
        this.accountDAO = new AccountDAO();
    }
    
    public Transaction createTransaction(String accountNumber, Transaction transaction) throws SQLException, AccountNotFoundException, InsufficientBalanceException, InvalidTransactionException {
        Account account = accountDAO.findByAccountNumber(accountNumber);
        transaction.setAccountId(account.getAccountId());
        
        // Validate transaction based on type
        validateTransaction(account, transaction);
        
        // Update account balance
        updateAccountBalance(account, transaction);
        
        // Save transaction
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, description, " +
                    "transaction_date, status, reference_number, to_account_id, balance_after_transaction) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, transaction.getAccountId());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setTimestamp(5, Timestamp.valueOf(transaction.getTransactionDate()));
            pstmt.setString(6, transaction.getStatus());
            pstmt.setString(7, transaction.getReferenceNumber());
            pstmt.setObject(8, transaction.getToAccountId());
            pstmt.setDouble(9, transaction.getBalanceAfterTransaction());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                    transaction.markCompleted();
                    LOGGER.info("Transaction created with ID: " + transaction.getTransactionId());
                    return transaction;
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }
    }
    
    public Transaction createTransfer(String fromAccountNumber, String toAccountNumber, Transaction transaction) 
            throws SQLException, AccountNotFoundException, InsufficientBalanceException, InvalidTransactionException {
        
        Account fromAccount = accountDAO.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountDAO.findByAccountNumber(toAccountNumber);
        
        transaction.setAccountId(fromAccount.getAccountId());
        transaction.setToAccountId(toAccount.getAccountId());
        
        // Validate transfer
        if (!fromAccount.isActive() || !toAccount.isActive()) {
            throw new InvalidTransactionException("One or both accounts are not active");
        }
        
        if (!fromAccount.hasSufficientBalance(transaction.getAmount())) {
            throw new InsufficientBalanceException("Insufficient balance for transfer", 
                                                 transaction.getAmount(), fromAccount.getBalance());
        }
        
        // Update both account balances
        fromAccount.withdraw(transaction.getAmount());
        toAccount.deposit(transaction.getAmount());
        
        accountDAO.updateAccount(fromAccount);
        accountDAO.updateAccount(toAccount);
        
        // Save transaction
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, description, " +
                    "transaction_date, status, reference_number, to_account_id, balance_after_transaction) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, transaction.getAccountId());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setTimestamp(5, Timestamp.valueOf(transaction.getTransactionDate()));
            pstmt.setString(6, transaction.getStatus());
            pstmt.setString(7, transaction.getReferenceNumber());
            pstmt.setInt(8, transaction.getToAccountId());
            pstmt.setDouble(9, fromAccount.getBalance());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating transfer transaction failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                    transaction.setBalanceAfterTransaction(fromAccount.getBalance());
                    transaction.markCompleted();
                    LOGGER.info("Transfer transaction created with ID: " + transaction.getTransactionId());
                    return transaction;
                } else {
                    throw new SQLException("Creating transfer transaction failed, no ID obtained.");
                }
            }
        }
    }
    
    public Transaction findById(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                } else {
                    return null;
                }
            }
        }
    }
    
    public List<Transaction> findByAccountNumber(String accountNumber) throws SQLException, AccountNotFoundException {
        Account account = accountDAO.findByAccountNumber(accountNumber);
        return findByAccountId(account.getAccountId());
    }
    
    public List<Transaction> findByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        
        return transactions;
    }
    
    public List<Transaction> findAll() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        
        return transactions;
    }
    
    public List<Transaction> findCompletedTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions WHERE status = 'COMPLETED' ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        
        return transactions;
    }
    
    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET account_id = ?, transaction_type = ?, amount = ?, " +
                    "description = ?, transaction_date = ?, status = ?, " +
                    "reference_number = ?, to_account_id = ?, balance_after_transaction = ? " +
                    "WHERE transaction_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transaction.getAccountId());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setTimestamp(5, Timestamp.valueOf(transaction.getTransactionDate()));
            pstmt.setString(6, transaction.getStatus());
            pstmt.setString(7, transaction.getReferenceNumber());
            pstmt.setObject(8, transaction.getToAccountId());
            pstmt.setDouble(9, transaction.getBalanceAfterTransaction());
            pstmt.setInt(10, transaction.getTransactionId());
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Transaction updated. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    public boolean deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Transaction deleted. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    private void validateTransaction(Account account, Transaction transaction) throws InsufficientBalanceException, InvalidTransactionException {
        if (!account.isActive()) {
            throw new InvalidTransactionException("Account is not active", transaction.getTransactionType(), transaction.getAmount());
        }
        
        if ("WITHDRAWAL".equals(transaction.getTransactionType()) || "TRANSFER".equals(transaction.getTransactionType())) {
            if (!account.hasSufficientBalance(transaction.getAmount())) {
                throw new InsufficientBalanceException("Insufficient balance", transaction.getAmount(), account.getBalance());
            }
        }
        
        if (transaction.getAmount() <= 0) {
            throw new InvalidTransactionException("Transaction amount must be greater than zero", 
                                               transaction.getTransactionType(), transaction.getAmount());
        }
    }
    
    private void updateAccountBalance(Account account, Transaction transaction) throws SQLException, InvalidTransactionException {
        double newBalance = account.getBalance();
        
        switch (transaction.getTransactionType()) {
            case "DEPOSIT":
                newBalance += transaction.getAmount();
                break;
            case "WITHDRAWAL":
                newBalance -= transaction.getAmount();
                break;
            case "LOAN_DISBURSEMENT":
                newBalance += transaction.getAmount();
                break;
            case "LOAN_REPAYMENT":
                newBalance -= transaction.getAmount();
                break;
            default:
                throw new InvalidTransactionException("Invalid transaction type: " + transaction.getTransactionType());
        }
        
        account.setBalance(newBalance);
        account.setLastTransactionDate(LocalDateTime.now());
        accountDAO.updateAccount(account);
        
        transaction.setBalanceAfterTransaction(newBalance);
    }
    
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setAccountId(rs.getInt("account_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setDescription(rs.getString("description"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        transaction.setStatus(rs.getString("status"));
        transaction.setReferenceNumber(rs.getString("reference_number"));
        
        int toAccountId = rs.getInt("to_account_id");
        if (rs.wasNull()) {
            transaction.setToAccountId(null);
        } else {
            transaction.setToAccountId(toAccountId);
        }
        
        transaction.setBalanceAfterTransaction(rs.getDouble("balance_after_transaction"));
        return transaction;
    }
} 