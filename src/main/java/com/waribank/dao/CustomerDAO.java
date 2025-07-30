package com.waribank.dao;

import com.waribank.database.DatabaseManager;
import com.waribank.model.Customer;
import com.waribank.exception.CustomerNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Data Access Object for Customer entity
 * 
 * @author Albert Fekadu Wari
 */
public class CustomerDAO {
    private static final Logger LOGGER = Logger.getLogger(CustomerDAO.class.getName());
    private final DatabaseManager dbManager;
    
    public CustomerDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Create a new customer
     */
    public Customer createCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone_number, address, national_id, " +
                    "registration_date, status, credit_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getNationalId());
            pstmt.setTimestamp(7, Timestamp.valueOf(customer.getRegistrationDate()));
            pstmt.setString(8, customer.getStatus());
            pstmt.setDouble(9, customer.getCreditScore());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));
                    LOGGER.info("Customer created with ID: " + customer.getCustomerId());
                    return customer;
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Find customer by ID
     */
    public Customer findById(int customerId) throws SQLException, CustomerNotFoundException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                } else {
                    throw new CustomerNotFoundException("Customer not found", customerId);
                }
            }
        }
    }
    
    /**
     * Find customer by email
     */
    public Customer findByEmail(String email) throws SQLException, CustomerNotFoundException {
        String sql = "SELECT * FROM customers WHERE email = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                } else {
                    throw new CustomerNotFoundException("Customer not found", email, true);
                }
            }
        }
    }
    
    /**
     * Find customer by national ID
     */
    public Customer findByNationalId(String nationalId) throws SQLException, CustomerNotFoundException {
        String sql = "SELECT * FROM customers WHERE national_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nationalId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                } else {
                    throw new CustomerNotFoundException("Customer not found", nationalId);
                }
            }
        }
    }
    
    /**
     * Get all customers
     */
    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT * FROM customers ORDER BY customer_id";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        
        return customers;
    }
    
    /**
     * Get active customers
     */
    public List<Customer> findActiveCustomers() throws SQLException {
        String sql = "SELECT * FROM customers WHERE status = 'ACTIVE' ORDER BY customer_id";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        
        return customers;
    }
    
    /**
     * Update customer
     */
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone_number = ?, " +
                    "address = ?, national_id = ?, status = ?, credit_score = ? WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getNationalId());
            pstmt.setString(7, customer.getStatus());
            pstmt.setDouble(8, customer.getCreditScore());
            pstmt.setInt(9, customer.getCustomerId());
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Customer updated. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Delete customer
     */
    public boolean deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Customer deleted. Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Update customer status
     */
    public boolean updateCustomerStatus(int customerId, String status) throws SQLException {
        String sql = "UPDATE customers SET status = ? WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, customerId);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Customer status updated to " + status + ". Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Update customer credit score
     */
    public boolean updateCreditScore(int customerId, double creditScore) throws SQLException {
        String sql = "UPDATE customers SET credit_score = ? WHERE customer_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, creditScore);
            pstmt.setInt(2, customerId);
            
            int affectedRows = pstmt.executeUpdate();
            LOGGER.info("Customer credit score updated to " + creditScore + ". Rows affected: " + affectedRows);
            return affectedRows > 0;
        }
    }
    
    /**
     * Map ResultSet to Customer object
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setAddress(rs.getString("address"));
        customer.setNationalId(rs.getString("national_id"));
        customer.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        customer.setStatus(rs.getString("status"));
        customer.setCreditScore(rs.getDouble("credit_score"));
        return customer;
    }
} 