package com.waribank.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Customer model
 * 
 * @author Albert Fekadu Wari
 */
public class CustomerTest {
    
    private Customer customer;
    
    @BeforeEach
    void setUp() {
        customer = new Customer("John", "Doe", "john.doe@email.com", 
                              "+251912345678", "Addis Ababa, Ethiopia", "ET123456789");
    }
    
    @Test
    void testCustomerCreation() {
        assertNotNull(customer);
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@email.com", customer.getEmail());
        assertEquals("+251912345678", customer.getPhoneNumber());
        assertEquals("Addis Ababa, Ethiopia", customer.getAddress());
        assertEquals("ET123456789", customer.getNationalId());
    }
    
    @Test
    void testGetFullName() {
        assertEquals("John Doe", customer.getFullName());
    }
    
    @Test
    void testIsActive() {
        assertTrue(customer.isActive());
        assertEquals("ACTIVE", customer.getStatus());
    }
    
    @Test
    void testActivate() {
        customer.suspend();
        assertFalse(customer.isActive());
        
        customer.activate();
        assertTrue(customer.isActive());
        assertEquals("ACTIVE", customer.getStatus());
    }
    
    @Test
    void testSuspend() {
        customer.suspend();
        assertFalse(customer.isActive());
        assertEquals("SUSPENDED", customer.getStatus());
    }
    
    @Test
    void testDeactivate() {
        customer.deactivate();
        assertFalse(customer.isActive());
        assertEquals("INACTIVE", customer.getStatus());
    }
    
    @Test
    void testUpdateCreditScore() {
        customer.updateCreditScore(750.0);
        assertEquals(750.0, customer.getCreditScore());
        
        // Test upper bound
        customer.updateCreditScore(1200.0);
        assertEquals(1000.0, customer.getCreditScore());
        
        // Test lower bound
        customer.updateCreditScore(-100.0);
        assertEquals(0.0, customer.getCreditScore());
    }
    
    @Test
    void testEqualsAndHashCode() {
        Customer customer1 = new Customer("John", "Doe", "john.doe@email.com", 
                                        "+251912345678", "Addis Ababa, Ethiopia", "ET123456789");
        Customer customer2 = new Customer("John", "Doe", "john.doe@email.com", 
                                        "+251912345678", "Addis Ababa, Ethiopia", "ET123456789");
        
        // Set same IDs for equality test
        customer1.setCustomerId(1);
        customer2.setCustomerId(1);
        
        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }
    
    @Test
    void testToString() {
        customer.setCustomerId(1);
        customer.updateCreditScore(750.0);
        
        String toString = customer.toString();
        assertTrue(toString.contains("Customer{id=1"));
        assertTrue(toString.contains("name='John Doe'"));
        assertTrue(toString.contains("email='john.doe@email.com'"));
        assertTrue(toString.contains("status='ACTIVE'"));
        assertTrue(toString.contains("creditScore=750.00"));
    }
} 