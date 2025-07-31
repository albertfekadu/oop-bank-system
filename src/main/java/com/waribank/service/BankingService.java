package com.waribank.service;

import com.waribank.dao.CustomerDAO;
import com.waribank.dao.AccountDAO;
import com.waribank.dao.TransactionDAO;
import com.waribank.dao.LoanDAO;
import com.waribank.model.*;
import com.waribank.exception.*;
import com.waribank.utils.AppLogger;
import com.waribank.database.DatabaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service class containing all banking business logic
 * 
 * @author Albert Fekadu Wari
 */
public class BankingService {
    private final CustomerDAO customerDAO;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private final LoanDAO loanDAO;
    private final DatabaseManager dbManager;
    
    public BankingService() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.loanDAO = new LoanDAO();
        this.dbManager = DatabaseManager.getInstance();
    }
    
    // ==================== CUSTOMER MANAGEMENT ====================
    
    /**
     * Register a new customer
     */
    public void registerCustomer(Scanner scanner) {
        try {
            System.out.println("\n=== CUSTOMER REGISTRATION ===");
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine().trim();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine().trim();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Enter Phone Number: ");
            String phoneNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Address: ");
            String address = scanner.nextLine().trim();
            
            System.out.print("Enter National ID: ");
            String nationalId = scanner.nextLine().trim();
            
            // Validate input
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || nationalId.isEmpty()) {
                System.out.println("Error: Required fields cannot be empty.");
                return;
            }
            
            Customer customer = new Customer(firstName, lastName, email, phoneNumber, address, nationalId);
            Customer savedCustomer = customerDAO.createCustomer(customer);
            
            AppLogger.success("Customer registered successfully with ID: " + savedCustomer.getCustomerId());
            System.out.println("Customer registered successfully!");
            System.out.println("Customer ID: " + savedCustomer.getCustomerId());
            
        } catch (SQLException e) {
            AppLogger.error("Failed to register customer: " + e.getMessage());
            System.out.println("Error: Failed to register customer. " + e.getMessage());
        }
    }
    
    /**
     * View customer details
     */
    public void viewCustomerDetails(Scanner scanner) {
        try {
            System.out.println("\n=== VIEW CUSTOMER DETAILS ===");
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            Customer customer = customerDAO.findById(customerId);
            displayCustomerDetails(customer);
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid customer ID.");
        } catch (SQLException | CustomerNotFoundException e) {
            AppLogger.error("Failed to view customer details: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Update customer information
     */
    public void updateCustomerInformation(Scanner scanner) {
        try {
            System.out.println("\n=== UPDATE CUSTOMER INFORMATION ===");
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            Customer customer = customerDAO.findById(customerId);
            
            System.out.println("Current customer information:");
            displayCustomerDetails(customer);
            
            System.out.print("Enter new First Name (or press Enter to keep current): ");
            String firstName = scanner.nextLine().trim();
            if (!firstName.isEmpty()) {
                customer.setFirstName(firstName);
            }
            
            System.out.print("Enter new Last Name (or press Enter to keep current): ");
            String lastName = scanner.nextLine().trim();
            if (!lastName.isEmpty()) {
                customer.setLastName(lastName);
            }
            
            System.out.print("Enter new Phone Number (or press Enter to keep current): ");
            String phoneNumber = scanner.nextLine().trim();
            if (!phoneNumber.isEmpty()) {
                customer.setPhoneNumber(phoneNumber);
            }
            
            System.out.print("Enter new Address (or press Enter to keep current): ");
            String address = scanner.nextLine().trim();
            if (!address.isEmpty()) {
                customer.setAddress(address);
            }
            
            boolean updated = customerDAO.updateCustomer(customer);
            if (updated) {
                AppLogger.success("Customer information updated successfully");
                System.out.println("Customer information updated successfully!");
            } else {
                System.out.println("Failed to update customer information.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid customer ID.");
        } catch (SQLException | CustomerNotFoundException e) {
            AppLogger.error("Failed to update customer information: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * List all customers
     */
    public void listAllCustomers() {
        try {
            System.out.println("\n=== ALL CUSTOMERS ===");
            List<Customer> customers = customerDAO.findAll();
            
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }
            
            System.out.printf("%-5s %-20s %-20s %-30s %-15s %-10s%n", 
                           "ID", "Name", "Email", "Phone", "Status", "Credit Score");
            System.out.println("=".repeat(100));
            
            for (Customer customer : customers) {
                System.out.printf("%-5d %-20s %-20s %-30s %-15s %-10.2f%n",
                               customer.getCustomerId(),
                               customer.getFullName(),
                               customer.getEmail(),
                               customer.getPhoneNumber(),
                               customer.getStatus(),
                               customer.getCreditScore());
            }
            
        } catch (SQLException e) {
            AppLogger.error("Failed to list customers: " + e.getMessage());
            System.out.println("Error: Failed to retrieve customers. " + e.getMessage());
        }
    }
    
    /**
     * Search customer
     */
    public void searchCustomer(Scanner scanner) {
        try {
            System.out.println("\n=== SEARCH CUSTOMER ===");
            System.out.println("1. Search by Email");
            System.out.println("2. Search by National ID");
            System.out.print("Enter your choice: ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            Customer customer = null;
            
            switch (choice) {
                case 1:
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine().trim();
                    customer = customerDAO.findByEmail(email);
                    break;
                case 2:
                    System.out.print("Enter National ID: ");
                    String nationalId = scanner.nextLine().trim();
                    customer = customerDAO.findByNationalId(nationalId);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            
            if (customer != null) {
                displayCustomerDetails(customer);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid choice.");
        } catch (SQLException | CustomerNotFoundException e) {
            AppLogger.error("Failed to search customer: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Update customer status
     */
    public void updateCustomerStatus(Scanner scanner) {
        try {
            System.out.println("\n=== UPDATE CUSTOMER STATUS ===");
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            Customer customer = customerDAO.findById(customerId);
            System.out.println("Current status: " + customer.getStatus());
            
            System.out.println("Available statuses: ACTIVE, SUSPENDED, INACTIVE");
            System.out.print("Enter new status: ");
            String newStatus = scanner.nextLine().trim().toUpperCase();
            
            boolean updated = customerDAO.updateCustomerStatus(customerId, newStatus);
            if (updated) {
                AppLogger.success("Customer status updated successfully");
                System.out.println("Customer status updated successfully!");
            } else {
                System.out.println("Failed to update customer status.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid customer ID.");
        } catch (SQLException | CustomerNotFoundException e) {
            AppLogger.error("Failed to update customer status: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // ==================== ACCOUNT MANAGEMENT ====================
    
    /**
     * Open new account
     */
    public void openNewAccount(Scanner scanner) {
        try {
            System.out.println("\n=== OPEN NEW ACCOUNT ===");
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            // Verify customer exists
            Customer customer = customerDAO.findById(customerId);
            
            System.out.println("Available account types: SAVINGS, CHECKING, FIXED_DEPOSIT");
            System.out.print("Enter Account Type: ");
            String accountType = scanner.nextLine().trim().toUpperCase();
            
            System.out.print("Enter Initial Balance: ");
            double initialBalance = Double.parseDouble(scanner.nextLine().trim());
            
            Account account = new Account(customerId, accountType, initialBalance);
            Account savedAccount = accountDAO.createAccount(account);
            
            AppLogger.success("Account opened successfully with number: " + savedAccount.getAccountNumber());
            System.out.println("Account opened successfully!");
            System.out.println("Account Number: " + savedAccount.getAccountNumber());
            System.out.println("Account Type: " + savedAccount.getAccountType());
            System.out.println("Initial Balance: " + savedAccount.getBalance());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers.");
        } catch (SQLException | CustomerNotFoundException e) {
            AppLogger.error("Failed to open account: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View account details
     */
    public void viewAccountDetails(Scanner scanner) {
        try {
            System.out.println("\n=== VIEW ACCOUNT DETAILS ===");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            Account account = accountDAO.findByAccountNumber(accountNumber);
            displayAccountDetails(account);
            
        } catch (SQLException | AccountNotFoundException e) {
            AppLogger.error("Failed to view account details: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * List customer accounts
     */
    public void listCustomerAccounts(Scanner scanner) {
        try {
            System.out.println("\n=== CUSTOMER ACCOUNTS ===");
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            List<Account> accounts = accountDAO.findByCustomerId(customerId);
            
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this customer.");
                return;
            }
            
            System.out.printf("%-15s %-15s %-15s %-15s %-10s%n", 
                           "Account Number", "Type", "Balance", "Status", "Interest Rate");
            System.out.println("=".repeat(75));
            
            for (Account account : accounts) {
                System.out.printf("%-15s %-15s %-15.2f %-15s %-10.2f%%%n",
                               account.getAccountNumber(),
                               account.getAccountType(),
                               account.getBalance(),
                               account.getStatus(),
                               account.getInterestRate());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid customer ID.");
        } catch (SQLException e) {
            AppLogger.error("Failed to list customer accounts: " + e.getMessage());
            System.out.println("Error: Failed to retrieve accounts. " + e.getMessage());
        }
    }
    
    /**
     * Update account status
     */
    public void updateAccountStatus(Scanner scanner) {
        try {
            System.out.println("\n=== UPDATE ACCOUNT STATUS ===");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            Account account = accountDAO.findByAccountNumber(accountNumber);
            System.out.println("Current status: " + account.getStatus());
            
            System.out.println("Available statuses: ACTIVE, FROZEN, CLOSED");
            System.out.print("Enter new status: ");
            String newStatus = scanner.nextLine().trim().toUpperCase();
            
            boolean updated = accountDAO.updateAccountStatus(accountNumber, newStatus);
            if (updated) {
                AppLogger.success("Account status updated successfully");
                System.out.println("Account status updated successfully!");
            } else {
                System.out.println("Failed to update account status.");
            }
            
        } catch (SQLException | AccountNotFoundException e) {
            AppLogger.error("Failed to update account status: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Close account
     */
    public void closeAccount(Scanner scanner) {
        try {
            System.out.println("\n=== CLOSE ACCOUNT ===");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            Account account = accountDAO.findByAccountNumber(accountNumber);
            
            if (account.getBalance() > 0) {
                System.out.println("Warning: Account has remaining balance of " + account.getBalance());
                System.out.print("Are you sure you want to close this account? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (!confirm.equals("y")) {
                    System.out.println("Account closure cancelled.");
                    return;
                }
            }
            
            boolean closed = accountDAO.closeAccount(accountNumber);
            if (closed) {
                AppLogger.success("Account closed successfully");
                System.out.println("Account closed successfully!");
            } else {
                System.out.println("Failed to close account.");
            }
            
        } catch (SQLException | AccountNotFoundException e) {
            AppLogger.error("Failed to close account: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // ==================== TRANSACTION MANAGEMENT ====================
    
    /**
     * Deposit money
     */
    public void depositMoney(Scanner scanner) {
        try {
            System.out.println("\n=== DEPOSIT MONEY ===");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            if (amount <= 0) {
                System.out.println("Error: Amount must be greater than zero.");
                return;
            }
            
            System.out.print("Enter Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "Cash deposit";
            }
            
            Transaction transaction = new Transaction(0, "DEPOSIT", amount, description);
            Transaction savedTransaction = transactionDAO.createTransaction(accountNumber, transaction);
            
            AppLogger.success("Deposit successful. Transaction ID: " + savedTransaction.getTransactionId());
            System.out.println("Deposit successful!");
            System.out.println("Transaction ID: " + savedTransaction.getTransactionId());
            System.out.println("Reference Number: " + savedTransaction.getReferenceNumber());
            System.out.println("New Balance: " + savedTransaction.getBalanceAfterTransaction());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid amount.");
        } catch (SQLException | AccountNotFoundException | InsufficientBalanceException | InvalidTransactionException e) {
            AppLogger.error("Failed to deposit money: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Withdraw money
     */
    public void withdrawMoney(Scanner scanner) {
        try {
            System.out.println("\n=== WITHDRAW MONEY ===");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            if (amount <= 0) {
                System.out.println("Error: Amount must be greater than zero.");
                return;
            }
            
            System.out.print("Enter Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "Cash withdrawal";
            }
            
            Transaction transaction = new Transaction(0, "WITHDRAWAL", amount, description);
            Transaction savedTransaction = transactionDAO.createTransaction(accountNumber, transaction);
            
            AppLogger.success("Withdrawal successful. Transaction ID: " + savedTransaction.getTransactionId());
            System.out.println("Withdrawal successful!");
            System.out.println("Transaction ID: " + savedTransaction.getTransactionId());
            System.out.println("Reference Number: " + savedTransaction.getReferenceNumber());
            System.out.println("New Balance: " + savedTransaction.getBalanceAfterTransaction());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid amount.");
        } catch (SQLException | AccountNotFoundException | InsufficientBalanceException | InvalidTransactionException e) {
            AppLogger.error("Failed to withdraw money: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Transfer money
     */
    public void transferMoney(Scanner scanner) {
        try {
            System.out.println("\n=== TRANSFER MONEY ===");
            System.out.print("Enter From Account Number: ");
            String fromAccountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter To Account Number: ");
            String toAccountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            if (amount <= 0) {
                System.out.println("Error: Amount must be greater than zero.");
                return;
            }
            
            System.out.print("Enter Description (optional): ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "Money transfer";
            }
            
            Transaction transaction = new Transaction(0, "TRANSFER", amount, description);
            Transaction savedTransaction = transactionDAO.createTransfer(fromAccountNumber, toAccountNumber, transaction);
            
            AppLogger.success("Transfer successful. Transaction ID: " + savedTransaction.getTransactionId());
            System.out.println("Transfer successful!");
            System.out.println("Transaction ID: " + savedTransaction.getTransactionId());
            System.out.println("Reference Number: " + savedTransaction.getReferenceNumber());
            System.out.println("New Balance: " + savedTransaction.getBalanceAfterTransaction());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid amount.");
        } catch (SQLException | AccountNotFoundException | InsufficientBalanceException | InvalidTransactionException e) {
            AppLogger.error("Failed to transfer money: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View transaction history
     */
    public void viewTransactionHistory(Scanner scanner) {
        try {
            System.out.println("\n=== TRANSACTION HISTORY ===");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            List<Transaction> transactions = transactionDAO.findByAccountNumber(accountNumber);
            
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
                return;
            }
            
            System.out.printf("%-15s %-15s %-15s %-20s %-15s%n", 
                           "Date", "Type", "Amount", "Description", "Status");
            System.out.println("=".repeat(85));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            for (Transaction transaction : transactions) {
                System.out.printf("%-15s %-15s %-15s %-20s %-15s%n",
                               transaction.getTransactionDate().format(formatter),
                               transaction.getTransactionType(),
                               transaction.getFormattedAmount(),
                               transaction.getDescription(),
                               transaction.getStatus());
            }
            
        } catch (SQLException | AccountNotFoundException e) {
            AppLogger.error("Failed to view transaction history: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View account balance
     */
    public void viewAccountBalance(Scanner scanner) {
        try {
            System.out.println("\n=== ACCOUNT BALANCE ===");
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            Account account = accountDAO.findByAccountNumber(accountNumber);
            
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Current Balance: " + account.getBalance());
            System.out.println("Status: " + account.getStatus());
            System.out.println("Interest Rate: " + account.getInterestRate() + "%");
            
        } catch (SQLException | AccountNotFoundException e) {
            AppLogger.error("Failed to view account balance: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // ==================== LOAN MANAGEMENT ====================
    
    /**
     * Apply for loan
     */
    public void applyForLoan(Scanner scanner) {
        try {
            System.out.println("\n=== APPLY FOR LOAN ===");
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            // Verify customer exists
            Customer customer = customerDAO.findById(customerId);
            
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            // Verify account exists
            Account account = accountDAO.findByAccountNumber(accountNumber);
            
            System.out.println("Available loan types: PERSONAL, BUSINESS, EDUCATION, AGRICULTURE");
            System.out.print("Enter Loan Type: ");
            String loanType = scanner.nextLine().trim().toUpperCase();
            
            System.out.print("Enter Loan Amount: ");
            double loanAmount = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Enter Term (in months): ");
            int termInMonths = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Enter Purpose: ");
            String purpose = scanner.nextLine().trim();
            
            Loan loan = new Loan(customerId, account.getAccountId(), loanAmount, termInMonths, loanType, purpose);
            Loan savedLoan = loanDAO.createLoan(loan);
            
            AppLogger.success("Loan application submitted successfully. Loan ID: " + savedLoan.getLoanId());
            System.out.println("Loan application submitted successfully!");
            System.out.println("Loan ID: " + savedLoan.getLoanId());
            System.out.println("Loan Type: " + savedLoan.getLoanType());
            System.out.println("Loan Amount: " + savedLoan.getLoanAmount());
            System.out.println("Interest Rate: " + savedLoan.getInterestRate() + "%");
            System.out.println("Monthly Payment: " + savedLoan.getMonthlyPayment());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers.");
        } catch (SQLException | CustomerNotFoundException | AccountNotFoundException e) {
            AppLogger.error("Failed to apply for loan: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View loan applications
     */
    public void viewLoanApplications(Scanner scanner) {
        try {
            System.out.println("\n=== LOAN APPLICATIONS ===");
            List<Loan> loans = loanDAO.findAll();
            
            if (loans.isEmpty()) {
                System.out.println("No loan applications found.");
                return;
            }
            
            System.out.printf("%-8s %-15s %-15s %-15s %-15s %-10s%n", 
                           "Loan ID", "Customer ID", "Loan Type", "Amount", "Status", "Term");
            System.out.println("=".repeat(80));
            
            for (Loan loan : loans) {
                System.out.printf("%-8d %-15d %-15s %-15.2f %-15s %-10d%n",
                               loan.getLoanId(),
                               loan.getCustomerId(),
                               loan.getLoanType(),
                               loan.getLoanAmount(),
                               loan.getStatus(),
                               loan.getTermInMonths());
            }
            
        } catch (SQLException e) {
            AppLogger.error("Failed to view loan applications: " + e.getMessage());
            System.out.println("Error: Failed to retrieve loan applications. " + e.getMessage());
        }
    }
    
    /**
     * Approve or reject loan
     */
    public void approveRejectLoan(Scanner scanner) {
        try {
            System.out.println("\n=== APPROVE/REJECT LOAN ===");
            System.out.print("Enter Loan ID: ");
            int loanId = Integer.parseInt(scanner.nextLine().trim());
            
            Loan loan = loanDAO.findById(loanId);
            
            if (!loan.isPending()) {
                System.out.println("This loan is not pending for approval.");
                return;
            }
            
            System.out.println("Loan Details:");
            displayLoanDetails(loan);
            
            System.out.println("1. Approve");
            System.out.println("2. Reject");
            System.out.print("Enter your choice: ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            switch (choice) {
                case 1:
                    System.out.print("Enter approver name: ");
                    String approver = scanner.nextLine().trim();
                    loan.approve(approver);
                    loanDAO.updateLoan(loan);
                    AppLogger.success("Loan approved successfully");
                    System.out.println("Loan approved successfully!");
                    break;
                case 2:
                    System.out.print("Enter rejection reason: ");
                    String reason = scanner.nextLine().trim();
                    loan.reject(reason);
                    loanDAO.updateLoan(loan);
                    AppLogger.success("Loan rejected successfully");
                    System.out.println("Loan rejected successfully!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers.");
        } catch (SQLException e) {
            AppLogger.error("Failed to approve/reject loan: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            AppLogger.error("Failed to approve/reject loan: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Disburse loan
     */
    public void disburseLoan(Scanner scanner) {
        try {
            System.out.println("\n=== DISBURSE LOAN ===");
            System.out.print("Enter Loan ID: ");
            int loanId = Integer.parseInt(scanner.nextLine().trim());
            
            Loan loan = loanDAO.findById(loanId);
            
            if (!loan.isApproved()) {
                System.out.println("This loan is not approved for disbursement.");
                return;
            }
            
            loan.disburse();
            loanDAO.updateLoan(loan);
            
            // Create disbursement transaction
            Transaction transaction = new Transaction(loan.getAccountId(), "LOAN_DISBURSEMENT", 
                                                   loan.getLoanAmount(), "Loan disbursement");
            // Get account number for the transaction
            Account account = accountDAO.findById(loan.getAccountId());
            transactionDAO.createTransaction(account.getAccountNumber(), transaction);
            
            AppLogger.success("Loan disbursed successfully");
            System.out.println("Loan disbursed successfully!");
            System.out.println("Amount disbursed: " + loan.getLoanAmount());
            System.out.println("Due date: " + loan.getDueDate());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid loan ID.");
        } catch (SQLException e) {
            AppLogger.error("Failed to disburse loan: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            AppLogger.error("Failed to disburse loan: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Make loan payment
     */
    public void makeLoanPayment(Scanner scanner) {
        try {
            System.out.println("\n=== MAKE LOAN PAYMENT ===");
            System.out.print("Enter Loan ID: ");
            int loanId = Integer.parseInt(scanner.nextLine().trim());
            
            Loan loan = loanDAO.findById(loanId);
            
            if (!loan.isActive()) {
                System.out.println("This loan is not active for payments.");
                return;
            }
            
            System.out.println("Remaining balance: " + loan.getRemainingBalance());
            System.out.println("Monthly payment: " + loan.getMonthlyPayment());
            
            System.out.print("Enter payment amount: ");
            double paymentAmount = Double.parseDouble(scanner.nextLine().trim());
            
            if (paymentAmount <= 0) {
                System.out.println("Error: Payment amount must be greater than zero.");
                return;
            }
            
            loan.makePayment(paymentAmount);
            loanDAO.updateLoan(loan);
            
            AppLogger.success("Loan payment made successfully");
            System.out.println("Loan payment made successfully!");
            System.out.println("Payment amount: " + paymentAmount);
            System.out.println("Remaining balance: " + loan.getRemainingBalance());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers.");
        } catch (SQLException e) {
            AppLogger.error("Failed to make loan payment: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            AppLogger.error("Failed to make loan payment: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View loan details
     */
    public void viewLoanDetails(Scanner scanner) {
        try {
            System.out.println("\n=== VIEW LOAN DETAILS ===");
            System.out.print("Enter Loan ID: ");
            int loanId = Integer.parseInt(scanner.nextLine().trim());
            
            Loan loan = loanDAO.findById(loanId);
            displayLoanDetails(loan);
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid loan ID.");
        } catch (SQLException e) {
            AppLogger.error("Failed to view loan details: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            AppLogger.error("Failed to view loan details: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // ==================== REPORTS & ANALYTICS ====================
    
    /**
     * Show customer statistics
     */
    public void showCustomerStatistics() {
        try {
            System.out.println("\n=== CUSTOMER STATISTICS ===");
            List<Customer> customers = customerDAO.findAll();
            
            long totalCustomers = customers.size();
            long activeCustomers = customers.stream().filter(Customer::isActive).count();
            long suspendedCustomers = customers.stream().filter(c -> "SUSPENDED".equals(c.getStatus())).count();
            long inactiveCustomers = customers.stream().filter(c -> "INACTIVE".equals(c.getStatus())).count();
            
            double avgCreditScore = customers.stream()
                .mapToDouble(Customer::getCreditScore)
                .average()
                .orElse(0.0);
            
            System.out.println("Total Customers: " + totalCustomers);
            System.out.println("Active Customers: " + activeCustomers);
            System.out.println("Suspended Customers: " + suspendedCustomers);
            System.out.println("Inactive Customers: " + inactiveCustomers);
            System.out.println("Average Credit Score: " + String.format("%.2f", avgCreditScore));
            
        } catch (SQLException e) {
            AppLogger.error("Failed to show customer statistics: " + e.getMessage());
            System.out.println("Error: Failed to retrieve customer statistics. " + e.getMessage());
        }
    }
    
    /**
     * Show account statistics
     */
    public void showAccountStatistics() {
        try {
            System.out.println("\n=== ACCOUNT STATISTICS ===");
            List<Account> accounts = accountDAO.findAll();
            
            long totalAccounts = accounts.size();
            long activeAccounts = accounts.stream().filter(Account::isActive).count();
            long savingsAccounts = accounts.stream().filter(a -> "SAVINGS".equals(a.getAccountType())).count();
            long checkingAccounts = accounts.stream().filter(a -> "CHECKING".equals(a.getAccountType())).count();
            long fixedDepositAccounts = accounts.stream().filter(a -> "FIXED_DEPOSIT".equals(a.getAccountType())).count();
            
            double totalBalance = accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
            
            System.out.println("Total Accounts: " + totalAccounts);
            System.out.println("Active Accounts: " + activeAccounts);
            System.out.println("Savings Accounts: " + savingsAccounts);
            System.out.println("Checking Accounts: " + checkingAccounts);
            System.out.println("Fixed Deposit Accounts: " + fixedDepositAccounts);
            System.out.println("Total Balance: " + String.format("%.2f", totalBalance));
            
        } catch (SQLException e) {
            AppLogger.error("Failed to show account statistics: " + e.getMessage());
            System.out.println("Error: Failed to retrieve account statistics. " + e.getMessage());
        }
    }
    
    /**
     * Show transaction statistics
     */
    public void showTransactionStatistics() {
        try {
            System.out.println("\n=== TRANSACTION STATISTICS ===");
            List<Transaction> transactions = transactionDAO.findAll();
            
            long totalTransactions = transactions.size();
            long completedTransactions = transactions.stream().filter(Transaction::isCompleted).count();
            long pendingTransactions = transactions.stream().filter(Transaction::isPending).count();
            long failedTransactions = transactions.stream().filter(Transaction::isFailed).count();
            
            double totalAmount = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            System.out.println("Total Transactions: " + totalTransactions);
            System.out.println("Completed Transactions: " + completedTransactions);
            System.out.println("Pending Transactions: " + pendingTransactions);
            System.out.println("Failed Transactions: " + failedTransactions);
            System.out.println("Total Amount: " + String.format("%.2f", totalAmount));
            
        } catch (SQLException e) {
            AppLogger.error("Failed to show transaction statistics: " + e.getMessage());
            System.out.println("Error: Failed to retrieve transaction statistics. " + e.getMessage());
        }
    }
    
    /**
     * Show loan statistics
     */
    public void showLoanStatistics() {
        try {
            System.out.println("\n=== LOAN STATISTICS ===");
            List<Loan> loans = loanDAO.findAll();
            
            long totalLoans = loans.size();
            long pendingLoans = loans.stream().filter(Loan::isPending).count();
            long approvedLoans = loans.stream().filter(Loan::isApproved).count();
            long activeLoans = loans.stream().filter(Loan::isActive).count();
            long completedLoans = loans.stream().filter(Loan::isCompleted).count();
            
            double totalLoanAmount = loans.stream()
                .mapToDouble(Loan::getLoanAmount)
                .sum();
            
            double totalRemainingBalance = loans.stream()
                .mapToDouble(Loan::getRemainingBalance)
                .sum();
            
            System.out.println("Total Loans: " + totalLoans);
            System.out.println("Pending Loans: " + pendingLoans);
            System.out.println("Approved Loans: " + approvedLoans);
            System.out.println("Active Loans: " + activeLoans);
            System.out.println("Completed Loans: " + completedLoans);
            System.out.println("Total Loan Amount: " + String.format("%.2f", totalLoanAmount));
            System.out.println("Total Remaining Balance: " + String.format("%.2f", totalRemainingBalance));
            
        } catch (SQLException e) {
            AppLogger.error("Failed to show loan statistics: " + e.getMessage());
            System.out.println("Error: Failed to retrieve loan statistics. " + e.getMessage());
        }
    }
    
    /**
     * Generate report
     */
    public void generateReport(Scanner scanner) {
        System.out.println("\n=== GENERATE REPORT ===");
        System.out.println("Report generation feature will be implemented in future versions.");
        System.out.println("For now, you can use the statistics features to view data.");
    }
    
    // ==================== SYSTEM SETTINGS ====================
    
    /**
     * Show database status
     */
    public void showDatabaseStatus() {
        System.out.println("\n=== DATABASE STATUS ===");
        boolean isConnected = dbManager.testConnection();
        System.out.println("Database Connection: " + (isConnected ? "Connected" : "Disconnected"));
        System.out.println("Database File: waribank.db");
    }
    
    /**
     * Show system information
     */
    public void showSystemInformation() {
        System.out.println("\n=== SYSTEM INFORMATION ===");
        System.out.println("Application: WariBank CLI");
        System.out.println("Version: 1.0.0");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Operating System: " + System.getProperty("os.name"));
        System.out.println("Current Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /**
     * Backup database
     */
    public void backupDatabase() {
        System.out.println("\n=== BACKUP DATABASE ===");
        System.out.println("Database backup feature will be implemented in future versions.");
        System.out.println("For now, you can manually copy the waribank.db file.");
    }
    
    /**
     * Clear logs
     */
    public void clearLogs() {
        System.out.println("\n=== CLEAR LOGS ===");
        System.out.println("Log clearing feature will be implemented in future versions.");
        System.out.println("Logs are currently displayed in the console.");
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Display customer details
     */
    private void displayCustomerDetails(Customer customer) {
        System.out.println("\n=== CUSTOMER DETAILS ===");
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Name: " + customer.getFullName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhoneNumber());
        System.out.println("Address: " + customer.getAddress());
        System.out.println("National ID: " + customer.getNationalId());
        System.out.println("Registration Date: " + customer.getRegistrationDate());
        System.out.println("Status: " + customer.getStatus());
        System.out.println("Credit Score: " + customer.getCreditScore());
    }
    
    /**
     * Display account details
     */
    private void displayAccountDetails(Account account) {
        System.out.println("\n=== ACCOUNT DETAILS ===");
        System.out.println("Account ID: " + account.getAccountId());
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Interest Rate: " + account.getInterestRate() + "%");
        System.out.println("Opening Date: " + account.getOpeningDate());
        System.out.println("Last Transaction: " + account.getLastTransactionDate());
        System.out.println("Status: " + account.getStatus());
        System.out.println("Minimum Balance: " + account.getMinimumBalance());
    }
    
    /**
     * Display loan details
     */
    private void displayLoanDetails(Loan loan) {
        System.out.println("\n=== LOAN DETAILS ===");
        System.out.println("Loan ID: " + loan.getLoanId());
        System.out.println("Customer ID: " + loan.getCustomerId());
        System.out.println("Account ID: " + loan.getAccountId());
        System.out.println("Loan Type: " + loan.getLoanType());
        System.out.println("Loan Amount: " + loan.getLoanAmount());
        System.out.println("Interest Rate: " + loan.getInterestRate() + "%");
        System.out.println("Term (months): " + loan.getTermInMonths());
        System.out.println("Purpose: " + loan.getPurpose());
        System.out.println("Application Date: " + loan.getApplicationDate());
        System.out.println("Status: " + loan.getStatus());
        System.out.println("Monthly Payment: " + loan.getMonthlyPayment());
        System.out.println("Remaining Balance: " + loan.getRemainingBalance());
        
        if (loan.getApprovalDate() != null) {
            System.out.println("Approval Date: " + loan.getApprovalDate());
            System.out.println("Approved By: " + loan.getApprovedBy());
        }
        
        if (loan.getDisbursementDate() != null) {
            System.out.println("Disbursement Date: " + loan.getDisbursementDate());
            System.out.println("Due Date: " + loan.getDueDate());
        }
        
        if (loan.getRejectionReason() != null) {
            System.out.println("Rejection Reason: " + loan.getRejectionReason());
        }
    }
} 