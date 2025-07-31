package com.waribank;

import com.waribank.ui.CLIInterface;
import com.waribank.database.DatabaseManager;
import com.waribank.utils.AppLogger;

/**
 * Main application class for WariBank - Microfinance Simulation Platform
 * 
 * @author Albert Fekadu Wari
 */
public class WariBankApp {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    WARI BANK CLI                            ║");
        System.out.println("║              Microfinance Simulation Platform               ║");
        System.out.println("║                    Version 1.0.0                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        try {
            // Initialize database
            DatabaseManager.getInstance().initializeDatabase();
            AppLogger.info("Database initialized successfully");
            
            // Start CLI interface
            CLIInterface cli = new CLIInterface();
            cli.start();
            
        } catch (Exception e) {
            AppLogger.error("Failed to start WariBank application: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
} 