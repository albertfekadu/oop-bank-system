package com.waribank.database;

import java.sql.*;
import java.util.logging.Level;

/**
 * Database manager singleton for handling SQLite database operations
 * 
 * @author Albert Fekadu Wari
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_URL = "jdbc:sqlite:waribank.db";
    
    private DatabaseManager() {
        // Private constructor for singleton pattern
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Initialize the database and create tables if they don't exist
     */
    public void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createTables(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * Create all necessary tables
     */
    private void createTables(Connection conn) throws SQLException {
        createCustomersTable(conn);
        createAccountsTable(conn);
        createTransactionsTable(conn);
        createLoansTable(conn);
    }
    
    private void createCustomersTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "address TEXT," +
                    "national_id TEXT UNIQUE NOT NULL," +
                    "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "status TEXT DEFAULT 'ACTIVE'," +
                    "credit_score REAL DEFAULT 0.0" +
                    ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    private void createAccountsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_id INTEGER NOT NULL," +
                    "account_number TEXT UNIQUE NOT NULL," +
                    "account_type TEXT NOT NULL," +
                    "balance REAL DEFAULT 0.0," +
                    "interest_rate REAL DEFAULT 0.0," +
                    "opening_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "status TEXT DEFAULT 'ACTIVE'," +
                    "minimum_balance REAL DEFAULT 0.0," +
                    "daily_withdrawal_limit REAL DEFAULT 10000.0," +
                    "monthly_withdrawal_limit REAL DEFAULT 100000.0," +
                    "FOREIGN KEY (customer_id) REFERENCES customers (customer_id)" +
                    ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    private void createTransactionsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "account_id INTEGER NOT NULL," +
                    "transaction_type TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "description TEXT," +
                    "status TEXT DEFAULT 'PENDING'," +
                    "reference_number TEXT UNIQUE NOT NULL," +
                    "to_account_id INTEGER," +
                    "balance_after_transaction REAL," +
                    "FOREIGN KEY (account_id) REFERENCES accounts (account_id)," +
                    "FOREIGN KEY (to_account_id) REFERENCES accounts (account_id)" +
                    ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    private void createLoansTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS loans (" +
                    "loan_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_id INTEGER NOT NULL," +
                    "account_id INTEGER NOT NULL," +
                    "loan_amount REAL NOT NULL," +
                    "interest_rate REAL NOT NULL," +
                    "term_in_months INTEGER NOT NULL," +
                    "loan_type TEXT NOT NULL," +
                    "purpose TEXT," +
                    "application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "approval_date TIMESTAMP," +
                    "disbursement_date TIMESTAMP," +
                    "due_date TIMESTAMP," +
                    "status TEXT DEFAULT 'PENDING'," +
                    "monthly_payment REAL DEFAULT 0.0," +
                    "remaining_balance REAL DEFAULT 0.0," +
                    "approved_by TEXT," +
                    "rejection_reason TEXT," +
                    "FOREIGN KEY (customer_id) REFERENCES customers (customer_id)," +
                    "FOREIGN KEY (account_id) REFERENCES accounts (account_id)" +
                    ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    
    /**
     * Test database connection
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
} 