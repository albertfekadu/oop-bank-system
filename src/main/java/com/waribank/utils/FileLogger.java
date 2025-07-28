package com.waribank.utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

/**
 * Simple file logger for the banking application
 * 
 * @author Albert Fekadu Wari
 */
public class FileLogger {
    private static final String LOG_FILE = "waribank.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Write a log message to file
     */
    public static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            String timestamp = LocalDateTime.now().format(formatter);
            out.println(timestamp + " - " + message);
            
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
    
    /**
     * Write a list of transactions to a report file
     */
    public static void writeTransactionReport(List<String> transactions, String filename) {
        try (FileWriter fw = new FileWriter(filename);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println("WariBank Transaction Report");
            out.println("Generated: " + LocalDateTime.now().format(formatter));
            out.println("==================================================");
            
            for (String transaction : transactions) {
                out.println(transaction);
            }
            
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
    
    /**
     * Read configuration from file
     */
    public static List<String> readConfig(String filename) {
        List<String> config = new ArrayList<>();
        
        try (FileReader fr = new FileReader(filename);
             BufferedReader br = new BufferedReader(fr)) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    config.add(line.trim());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
        
        return config;
    }
    
    /**
     * Check if log file exists
     */
    public static boolean logFileExists() {
        File file = new File(LOG_FILE);
        return file.exists();
    }
} 