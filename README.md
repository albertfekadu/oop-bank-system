# 🏦 WariBank

A simple banking simulation app built in Java CLI. Supports registering customers, managing accounts, deposits, withdrawals, loans, and transfers.

---

## 📦 Requirements

- Java JDK 17+ (tested on Java 21 or 24)
- SQLite (local file-based DB)
- Terminal or command prompt
- Optional: Homebrew (Mac), Git

---

## 🛠️ How to Set Up (From Scratch)

### For macOS:
1. Install Java:
```bash
brew install openjdk
echo 'export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

2. Install SQLite (if needed):
```bash
brew install sqlite
```

3. Clone the project:
```bash
git clone <your GitHub link>
cd wariBank
```

4. Compile and run:
```bash
chmod +x run-simple.sh
./run-simple.sh
```

### For Windows:
1. Download Java JDK from: https://adoptium.net/
2. Download SQLite from: https://www.sqlite.org/download.html
3. Extract files and run:
```bash
./run-simple.sh
```

### For Linux:
```bash
sudo apt update
sudo apt install openjdk-17-jdk sqlite3
chmod +x run-simple.sh
./run-simple.sh
```

---

## 📂 Project Structure

```
wariBank/
├── src/main/java/com/waribank/
│   ├── model/          # Bank entities (Customer, Account, etc.)
│   ├── dao/            # Database access objects
│   ├── service/        # Business logic
│   ├── ui/             # Command-line interface
│   ├── database/       # Database management
│   ├── exception/      # Custom exceptions
│   └── utils/          # Utility classes
├── src/test/java/      # Unit tests
├── config.txt          # Configuration file
└── run-simple.sh       # Run script
```

---

## 🔑 Features

* Register and manage customers
* Open savings accounts
* Deposit, withdraw, transfer funds
* Apply for loans
* View account summaries
* Export reports and logs
* Exception-safe and menu-driven

---

## 👨🏽‍💻 Author

Built by **Albert Fekadu Wari**
OOP Final Project — 2025


---

## ✅ Submission Ready

All requirements from the assignment are met:

* OOP design (21 classes, 2 interfaces, 1 abstract class)
* JDBC with SQLite (4 tables, CRUD operations)
* Java Collections (List, Map, Set)
* CLI interface (menu-driven)
* File I/O (logging and config)
* Exception handling (4 custom exceptions)

No external libraries, no GUI, no fluff.

---

## 🚀 Quick Start

1. Make sure you have Java JDK installed
2. Run: `./run-simple.sh`
3. Follow the menu prompts
4. Test features like customer registration, account opening, transactions

The app will create a local SQLite database and log files automatically.
