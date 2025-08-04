package com.waribank.ui;

import com.waribank.service.BankingService;
import com.waribank.utils.AppLogger;
import java.util.Scanner;


public class CLIInterface {
    private final BankingService bankingService;
    private final Scanner scanner;
    private boolean running;
    
    public CLIInterface() {
        this.bankingService = new BankingService();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    

    public void start() {
        AppLogger.info("Starting WariBank CLI interface");
        
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                processMainMenuChoice(choice);
            } catch (Exception e) {
                AppLogger.error("Error processing menu choice: " + e.getMessage());
                System.out.println("An error occurred. Please try again.");
            }
        }
        
        AppLogger.info("WariBank CLI interface stopped");
        scanner.close();
    }
    

    private void displayMainMenu() {
        System.out.println("\nWARI BANK MAIN MENU");
        System.out.println("1. Customer Management");
        System.out.println("2. Account Management");
        System.out.println("3. Transaction Management");
        System.out.println("4. Loan Management");
        System.out.println("5. Reports & Analytics");
        System.out.println("6. System Settings");
        System.out.println("0. Exit");
    }
    

    private void processMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                handleCustomerManagement();
                break;
            case 2:
                handleAccountManagement();
                break;
            case 3:
                handleTransactionManagement();
                break;
            case 4:
                handleLoanManagement();
                break;
            case 5:
                handleReportsAndAnalytics();
                break;
            case 6:
                handleSystemSettings();
                break;
            case 0:
                exitApplication();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    

    private void handleCustomerManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\nCUSTOMER MANAGEMENT");
            System.out.println("1. Register New Customer");
            System.out.println("2. View Customer Details");
            System.out.println("3. Update Customer Information");
            System.out.println("4. List All Customers");
            System.out.println("5. Search Customer");
            System.out.println("6. Update Customer Status");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bankingService.registerCustomer(scanner);
                    break;
                case 2:
                    bankingService.viewCustomerDetails(scanner);
                    break;
                case 3:
                    bankingService.updateCustomerInformation(scanner);
                    break;
                case 4:
                    bankingService.listAllCustomers();
                    break;
                case 5:
                    bankingService.searchCustomer(scanner);
                    break;
                case 6:
                    bankingService.updateCustomerStatus(scanner);
                    break;
                case 0:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void handleAccountManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\nACCOUNT MANAGEMENT");
            System.out.println("1. Open New Account");
            System.out.println("2. View Account Details");
            System.out.println("3. List Customer Accounts");
            System.out.println("4. Update Account Status");
            System.out.println("5. Close Account");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bankingService.openNewAccount(scanner);
                    break;
                case 2:
                    bankingService.viewAccountDetails(scanner);
                    break;
                case 3:
                    bankingService.listCustomerAccounts(scanner);
                    break;
                case 4:
                    bankingService.updateAccountStatus(scanner);
                    break;
                case 5:
                    bankingService.closeAccount(scanner);
                    break;
                case 0:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void handleTransactionManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\nTRANSACTION MANAGEMENT");
            System.out.println("1. Deposit Money");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Transfer Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. View Account Balance");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bankingService.depositMoney(scanner);
                    break;
                case 2:
                    bankingService.withdrawMoney(scanner);
                    break;
                case 3:
                    bankingService.transferMoney(scanner);
                    break;
                case 4:
                    bankingService.viewTransactionHistory(scanner);
                    break;
                case 5:
                    bankingService.viewAccountBalance(scanner);
                    break;
                case 0:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void handleLoanManagement() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\nLOAN MANAGEMENT");
            System.out.println("1. Apply for Loan");
            System.out.println("2. View Loan Applications");
            System.out.println("3. Approve/Reject Loan");
            System.out.println("4. Disburse Loan");
            System.out.println("5. Make Loan Payment");
            System.out.println("6. View Loan Details");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bankingService.applyForLoan(scanner);
                    break;
                case 2:
                    bankingService.viewLoanApplications(scanner);
                    break;
                case 3:
                    bankingService.approveRejectLoan(scanner);
                    break;
                case 4:
                    bankingService.disburseLoan(scanner);
                    break;
                case 5:
                    bankingService.makeLoanPayment(scanner);
                    break;
                case 6:
                    bankingService.viewLoanDetails(scanner);
                    break;
                case 0:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void handleReportsAndAnalytics() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\nREPORTS & ANALYTICS");
            System.out.println("1. Customer Statistics");
            System.out.println("2. Account Statistics");
            System.out.println("3. Transaction Statistics");
            System.out.println("4. Loan Statistics");
            System.out.println("5. Generate Report");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bankingService.showCustomerStatistics();
                    break;
                case 2:
                    bankingService.showAccountStatistics();
                    break;
                case 3:
                    bankingService.showTransactionStatistics();
                    break;
                case 4:
                    bankingService.showLoanStatistics();
                    break;
                case 5:
                    bankingService.generateReport(scanner);
                    break;
                case 0:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void handleSystemSettings() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\nSYSTEM SETTINGS");
            System.out.println("1. Database Status");
            System.out.println("2. System Information");
            System.out.println("3. Backup Database");
            System.out.println("4. Clear Logs");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bankingService.showDatabaseStatus();
                    break;
                case 2:
                    bankingService.showSystemInformation();
                    break;
                case 3:
                    bankingService.backupDatabase();
                    break;
                case 4:
                    bankingService.clearLogs();
                    break;
                case 0:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void exitApplication() {
        System.out.println("\nThank you for using WariBank!");
        System.out.println("Goodbye!");
        running = false;
    }
    

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
} 