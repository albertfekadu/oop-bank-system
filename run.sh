#!/bin/bash

echo "WariBank Mini Banking System"
echo "Author: Albert Fekadu Wari"
echo ""

# Create output directory
mkdir -p build

# Download SQLite JDBC driver if not exists
if [ ! -f "sqlite-jdbc-3.42.0.0.jar" ]; then
    echo "Downloading SQLite JDBC driver..."
    curl -L -o sqlite-jdbc-3.42.0.0.jar https://github.com/xerial/sqlite-jdbc/releases/download/3.42.0.0/sqlite-jdbc-3.42.0.0.jar
fi

echo "Compiling Java files..."

# Compile main classes
javac -cp ".:sqlite-jdbc-3.42.0.0.jar" -d build src/main/java/com/waribank/*.java src/main/java/com/waribank/*/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "Starting WariBank..."
    echo ""
    
    # Run the application
    java -cp ".:build:sqlite-jdbc-3.42.0.0.jar" com.waribank.WariBankApp
else
    echo "Compilation failed!"
    exit 1
fi 