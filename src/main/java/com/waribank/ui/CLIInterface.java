package com.waribank.ui;

/**
 * Command Line Interface for WariBank application
 * 
 * @author Albert Fekadu Wari
 */
public class CLIInterface {

    private boolean running;
    
    public CLIInterface() {

        this.running = true;
    }
    
    /**
     * Start the CLI application
     */
    public void start() {
        
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                processMainMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
            }
        }
        
    }
    
    /**
     * Display main menu
     */
    private void displayMainMenu() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    WARI BANK MAIN MENU                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Customer Management                                      ║");
        System.out.println("║  2. Account Management                                       ║");
        System.out.println("║  3. Transaction Management                                   ║");
        System.out.println("║  4. Loan Management                                          ║");
        System.out.println("║  5. Reports & Analytics                                      ║");
        System.out.println("║  6. System Settings                                          ║");
        System.out.println("║  0. Exit                                                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Process main menu choice
     */
    private void processMainMenuChoice(int choice) {
   
    }
    
    /**
     * Handle customer management menu
     */
    private void handleCustomerManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                  CUSTOMER MANAGEMENT                        ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Register New Customer                                  ║");
            System.out.println("║  2. View Customer Details                                  ║");
            System.out.println("║  3. Update Customer Information                            ║");
            System.out.println("║  4. List All Customers                                     ║");
            System.out.println("║  5. Search Customer                                        ║");
            System.out.println("║  6. Update Customer Status                                 ║");
            System.out.println("║  0. Back to Main Menu                                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            
            int choice = getIntInput("Enter your choice: ");
            

        }
    }
    
    /**
     * Handle account management menu
     */
    private void handleAccountManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   ACCOUNT MANAGEMENT                       ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Open New Account                                       ║");
            System.out.println("║  2. View Account Details                                   ║");
            System.out.println("║  3. List Customer Accounts                                 ║");
            System.out.println("║  4. Update Account Status                                  ║");
            System.out.println("║  5. Close Account                                          ║");
            System.out.println("║  0. Back to Main Menu                                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            
            int choice = getIntInput("Enter your choice: ");
            

        }
    }
    
    /**
     * Handle transaction management menu
     */
    private void handleTransactionManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                TRANSACTION MANAGEMENT                      ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Deposit Money                                          ║");
            System.out.println(" ║  2. Withdraw Money                                         ║");
            System.out.println("║  3. Transfer Money                                         ║");
            System.out.println("║  4. View Transaction History                               ║");
            System.out.println("║  5. View Account Balance                                   ║");
            System.out.println("║  0. Back to Main Menu                                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            
            int choice = getIntInput("Enter your choice: ");
            
         
        }
    }
    
    /**
     * Handle loan management menu
     */
    private void handleLoanManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    LOAN MANAGEMENT                         ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Apply for Loan                                         ║");
            System.out.println("║  2. View Loan Applications                                 ║");
            System.out.println("║  3. Approve/Reject Loan                                    ║");
            System.out.println("║  4. Disburse Loan                                          ║");
            System.out.println("║  5. Make Loan Payment                                      ║");
            System.out.println("║  6. View Loan Details                                      ║");
            System.out.println("║  0. Back to Main Menu                                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            
            int choice = getIntInput("Enter your choice: ");
            
   
        }
    }
    
    /**
     * Handle reports and analytics menu
     */
    private void handleReportsAndAnalytics() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                REPORTS & ANALYTICS                         ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Customer Statistics                                    ║");
            System.out.println("║  2. Account Statistics                                     ║");
            System.out.println("║  3. Transaction Statistics                                 ║");
            System.out.println("║  4. Loan Statistics                                        ║");
            System.out.println("║  5. Generate Report                                        ║");
            System.out.println("║  0. Back to Main Menu                                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            
            
           
        }
    }
    
    /**
     * Handle system settings menu
     */
    private void handleSystemSettings() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   SYSTEM SETTINGS                          ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Database Status                                        ║");
            System.out.println("║  2. System Information                                     ║");
            System.out.println("║  3. Backup Database                                        ║");
            System.out.println("║  4. Clear Logs                                             ║");
            System.out.println("║  0. Back to Main Menu                                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            
            
           
        }
    }
    
    /**
     * Exit application
     */
    private void exitApplication() {
        System.out.println("\nThank you for using WariBank!");
        System.out.println("Goodbye!");
        running = false;
    }
    
    /**
     * Get integer input from user
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
} 