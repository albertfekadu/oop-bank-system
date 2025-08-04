# WariBank: A Java Banking System

WariBank is a comprehensive, command-line banking simulation system developed as a final project for the Object-Oriented Programming with Java course. It is designed to apply and demonstrate core OOP principles, robust data persistence with JDBC, and a clean, modular architecture.

The application provides a user-friendly CLI for managing customers, accounts, transactions, and loans in a simulated banking environment.

## Key Features

- **Customer & Account Management**: Register customers and open unique savings/checking accounts.
- **Financial Operations**: Perform secure deposits, withdrawals, and inter-account transfers.
- **Loan System**: Allows users to apply for and track loans.
- **Data Persistence**: Utilizes a local SQLite database via JDBC to ensure all data is saved across sessions.
- **File-Based Logging**: Logs all significant system events and transactions to a file for auditing.
- **Menu-Driven Interface**: A clear and intuitive command-line interface for easy user interaction.

## Project Requirements Checklist

This project was built to meet the specific requirements of the course, as detailed below.

| Component | Requirement Met |
| :--- | :--- |
| **Java Classes** | The project contains **21 classes** organized logically into 8 packages (`model`, `dao`, `service`, etc.) to ensure separation of concerns. |
| **Interfaces** | **2 interfaces** are used: `Transactionable` (defining contract for transaction types) and `Reportable` (for entities that can be exported). |
| **Abstract Classes** | **1 abstract class**, `BankEntity`, is used as a base for all model classes, providing common fields like `id`. |
| **Inheritance** | A clear inheritance chain is demonstrated (e.g., `Customer` and `Account` both extend the abstract `BankEntity` class). |
| **Polymorphism** | **Overriding** is used in model classes for the `toString()` method. **Overloading** is demonstrated in the `BankingService` with multiple methods for fetching data. |
| **Exception Handling** | **4 custom exceptions** (`AccountNotFoundException`, `InsufficientBalanceException`, etc.) are used with `try-catch-finally` blocks for robust error management. |
| **Collections** | The project utilizes `List`, `Map`, and `Set` from the Java Collections Framework for managing data structures efficiently. |
| **JDBC Integration** | Integrated with **SQLite**. The system uses **4 database tables** and performs full CRUD (Create, Read, Update, Delete) operations using the **DAO pattern** and `PreparedStatement` for security. |
| **File I/O** | A `FileLogger` class uses `FileWriter` and `BufferedWriter` to write all application logs and transaction records to `waribank.log`. |
| **Interface** | The project is a **CLI (Command Line Interface)** application, built to be menu-driven and user-friendly as per the requirements. |

## Project Structure

The project's structure is organized to ensure a clean separation of concerns, aligning with modern Java application architecture.

```
src/main/java/com/waribank/
├── WariBankApp.java
├── dao/
│   ├── AccountDAO.java
│   ├── CustomerDAO.java
│   ├── LoanDAO.java
│   └── TransactionDAO.java
├── database/
│   └── DatabaseManager.java
├── exception/
│   ├── AccountNotFoundException.java
│   ├── CustomerNotFoundException.java
│   ├── InsufficientBalanceException.java
│   └── InvalidTransactionException.java
├── model/
│   ├── Account.java
│   ├── BankEntity.java
│   ├── Customer.java
│   ├── Loan.java
│   ├── Reportable.java
│   ├── Transaction.java
│   └── Transactionable.java
├── service/
│   └── BankingService.java
├── ui/
│   └── CLIInterface.java
└── utils/
    ├── AppLogger.java
    └── FileLogger.java
```

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- SQLite

### Installation & Running

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/albertfekadu/oop-bank-system.git
    cd oop-bank-system
    ```
2.  **Make the run script executable**:
    ```bash
    chmod +x run.sh
    ```
3.  **Run the application**:
    ```bash
    ./run.sh
    ```
    The script will compile the source code and launch the application, automatically creating a `waribank.db` database file and a `waribank.log` file.

## Author

This project was designed and developed by **Albert Fekadu Wari**.

---
