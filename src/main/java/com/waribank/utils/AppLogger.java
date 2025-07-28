package com.waribank.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for application logging
 * 
 * @author Albert Fekadu Wari
 */
public class AppLogger {
    private static final Logger LOGGER = Logger.getLogger("WariBank");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    static {
        LOGGER.setLevel(Level.ALL);
    }
    
    /**
     * Log info message
     */
    public static void info(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[INFO] [%s] %s", timestamp, message);
        LOGGER.info(logMessage);
        System.out.println(logMessage);
    }
    
    /**
     * Log warning message
     */
    public static void warning(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[WARNING] [%s] %s", timestamp, message);
        LOGGER.warning(logMessage);
        System.out.println("\u001B[33m" + logMessage + "\u001B[0m"); // Yellow color
    }
    
    /**
     * Log error message
     */
    public static void error(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[ERROR] [%s] %s", timestamp, message);
        LOGGER.severe(logMessage);
        System.err.println("\u001B[31m" + logMessage + "\u001B[0m"); // Red color
    }
    
    /**
     * Log debug message
     */
    public static void debug(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[DEBUG] [%s] %s", timestamp, message);
        LOGGER.fine(logMessage);
        System.out.println("\u001B[36m" + logMessage + "\u001B[0m"); // Cyan color
    }
    
    /**
     * Log exception
     */
    public static void error(String message, Throwable throwable) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[ERROR] [%s] %s", timestamp, message);
        LOGGER.log(Level.SEVERE, logMessage, throwable);
        System.err.println("\u001B[31m" + logMessage + "\u001B[0m");
        System.err.println("\u001B[31mException: " + throwable.getMessage() + "\u001B[0m");
    }
    
    /**
     * Log success message
     */
    public static void success(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[SUCCESS] [%s] %s", timestamp, message);
        LOGGER.info(logMessage);
        System.out.println("\u001B[32m" + logMessage + "\u001B[0m"); // Green color
    }
} 