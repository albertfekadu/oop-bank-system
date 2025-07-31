package com.waribank.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Account model
 * 
 * @author Albert Fekadu Wari
 */
public class AccountTest {
    
    private Account account;
    
    @BeforeEach
    void setUp() {
        account = new Account(1, "SAVINGS", 1000.0);
    }
    
    @Test
    void testAccountCreation() {
        assertNotNull(account);
        assertEquals(1, account.getCustomerId());
        assertEquals("SAVINGS", account.getAccountType());
        assertEquals(1000.0, account.getBalance());
    }
    
    @Test
    void testGetDisplayName() {
        assertTrue(account.getDisplayName().contains("Account"));
    }
    
    @Test
    void testIsValid() {
        assertTrue(account.isValid());
    }
    
    @Test
    void testCanPerformTransaction() {
        assertTrue(account.canPerformTransaction());
    }
    
    @Test
    void testUpdateBalance() {
        double originalBalance = account.getBalance();
        account.updateBalance(500.0);
        assertEquals(originalBalance + 500.0, account.getBalance());
    }
    
    @Test
    void testGenerateSummary() {
        String summary = account.generateSummary();
        assertTrue(summary.contains("Account"));
        assertTrue(summary.contains("SAVINGS"));
    }
    
    @Test
    void testGetDetails() {
        assertFalse(account.getDetails().isEmpty());
        assertTrue(account.getDetails().size() > 0);
    }
    
    @Test
    void testIsReportAvailable() {
        assertTrue(account.isReportAvailable());
    }
} 