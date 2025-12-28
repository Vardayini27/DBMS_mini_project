# Inventory and Sales Management System

## Overview

This project is a Java-based Inventory and Sales Management System integrated with a MySQL database. It is designed to manage suppliers, products, users, sales transactions, and reorders through a structured, menu-driven console interface.

The system supports complete CRUD operations for all major entities and uses MySQL stored procedures and functions to handle core business logic such as stock validation, price updates, and reorder management. JDBC is used for reliable communication between the application and the database.

---

## Features

### Supplier Management

* Add, update, delete, and view supplier records
* Maintain supplier contact and address details

### Product Management

* Add, update, delete, and view products
* Track stock levels, reorder thresholds, expiry dates, and categories
* Associate products with suppliers

### User Management

* Manage system users with role information
* Support for add, update, delete, and view operations

### Sales and Transactions

* Record sales transactions
* Automatic total price calculation
* View transaction history
* Profit calculation support

### Stock Monitoring and Reorders

* Continuous stock level checking
* Automated reorder prompts when stock falls below defined thresholds
* Reorder tracking with status management

### Database-Level Logic

* Use of MySQL stored procedures and functions
* Generated columns for transaction totals
* Enforced referential integrity using foreign keys

---

## Technologies Used

* Java (JDBC, console-based interface)
* MySQL (InnoDB tables, stored procedures, functions, triggers)

---

## Getting Started

### Prerequisites

* Java JDK 8 or higher
* MySQL Server
* MySQL Connector/J (JDBC Driver)

---

## Database Setup

Create a MySQL database named `inventory_mgmt` and configure the required tables, stored procedures, and functions as per the project schema. The database design follows normalization principles and enforces referential integrity between entities such as suppliers, products, users, transactions, and reorders.

---

## Running the Application

1. Update the database credentials in `DatabaseCode.java`:

   * Database URL
   * MySQL username
   * MySQL password

2. Compile the application:

```bash
javac -cp .:mysql-connector-java.jar DatabaseCode.java
```

3. Run the application:

```bash
java -cp .:mysql-connector-java.jar DBMS.Main
```

---

## Usage

The application provides a menu-driven interface for all operations. Users can add, view, update, and delete suppliers, products, users, transactions, and reorders. Stock levels are continuously monitored, and the system prompts for reordering when inventory reaches critical levels. Transaction data can be reviewed for sales tracking and profit analysis.

---

## Project Highlights

* Clear separation between application logic and database logic
* Practical use of relational database concepts
* Automated inventory control mechanisms
* Suitable for academic projects and small-scale business applications
