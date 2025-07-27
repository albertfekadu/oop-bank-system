#!/bin/bash

# WariBank CLI Application Runner
echo "Starting WariBank Mini Banking System..."
echo "Author: Albert Fekadu Wari"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 8 or higher and try again."
    exit 1
fi

echo "Java version: $(java -version 2>&1 | head -n 1)"
echo ""

echo "Building and running WariBank..."
echo ""

# Run the application
mvn clean compile exec:java -Dexec.mainClass="com.waribank.WariBankApp"

echo ""
echo "Thank you for using WariBank!" 