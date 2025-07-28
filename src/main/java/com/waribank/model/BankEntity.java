package com.waribank.model;

import java.time.LocalDateTime;

/**
 * Abstract base class for all bank entities
 * 
 * @author Albert Fekadu Wari
 */
public abstract class BankEntity {
    protected int id;
    protected LocalDateTime createdAt;
    protected String status;
    
    public BankEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }
    
    // Abstract methods that subclasses must implement
    public abstract String getDisplayName();
    public abstract boolean isValid();
    
    // Common methods for all entities
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
    
    // Method overloading example
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
    
    public void updateStatus(String newStatus, String reason) {
        this.status = newStatus;
        // In a real app, you might log the reason
    }
} 