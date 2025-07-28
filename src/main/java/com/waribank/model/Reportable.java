package com.waribank.model;

import java.util.List;

/**
 * Interface for entities that can generate reports
 * 
 * @author Albert Fekadu Wari
 */
public interface Reportable {
    
    /**
     * Generate a summary report
     */
    String generateSummary();
    
    /**
     * Get detailed information
     */
    List<String> getDetails();
    
    /**
     * Check if report is available
     */
    boolean isReportAvailable();
} 