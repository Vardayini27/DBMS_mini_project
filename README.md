Inventory Management System
Overview
This is a Java-based Inventory and Sales Management System connected to a MySQL database. The application provides a console menu for managing suppliers, products, users, sales transactions, and reorders. It supports CRUD operations for all major entities and uses SQL stored procedures/functions for tasks like stock and price management.

Features
Supplier management (add, update, delete, view)
Product management (add, update, delete, view)
User management (add, update, delete, view)
Sales transactions (add, view, profit calculation)
Stock checking with automatic reorder prompts
Reorder management
Use of MySQL stored procedures and functions
Technologies Used
Java (JDBC, Console UI)
MySQL (with InnoDB tables, procedures, triggers)
Getting Started
Prerequisites
Java JDK 8 or above
MySQL Server
MySQL Connector/J (JDBC Driver)
Database Setup
Create a database named dbms_mini in MySQL.
Create the required tables. Example structures:
SQL
CREATE TABLE Suppliers (
  supplier_id INT AUTO_INCREMENT PRIMARY KEY,
  supplier_name VARCHAR(100),
  supplier_contact_phone VARCHAR(20),
  supplier_contact_email VARCHAR(100),
  supplier_address VARCHAR(255)
);

CREATE TABLE Products (
  p_id INT AUTO_INCREMENT PRIMARY KEY,
  p_name VARCHAR(100),
  p_price DOUBLE,
  p_quantity_in_stock INT,
  p_reorder_level INT,
  supplier_id INT,
  p_category VARCHAR(100),
  p_expiry_date DATE,
  FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id)
);

CREATE TABLE Users (
  u_id INT AUTO_INCREMENT PRIMARY KEY,
  u_username VARCHAR(50),
  u_password VARCHAR(50),
  u_role VARCHAR(20)
);

CREATE TABLE Transactions (
  t_id INT AUTO_INCREMENT PRIMARY KEY,
  p_id INT,
  t_quantity_sold INT,
  t_rate DOUBLE,
  t_transaction_date DATETIME,
  t_total_price DOUBLE GENERATED ALWAYS AS (t_quantity_sold * t_rate) STORED,
  u_id INT,
  FOREIGN KEY (p_id) REFERENCES Products(p_id),
  FOREIGN KEY (u_id) REFERENCES Users(u_id)
);

CREATE TABLE Reorders (
  r_id INT AUTO_INCREMENT PRIMARY KEY,
  p_id INT,
  r_quantity INT,
  r_order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  r_status VARCHAR(20) DEFAULT 'Pending',
  supplier_id INT,
  u_id INT,
  FOREIGN KEY (p_id) REFERENCES Products(p_id),
  FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id),
  FOREIGN KEY (u_id) REFERENCES Users(u_id)
);
Add stored procedures/functions as required (e.g., for updating product prices, checking stock).
Running the Application
Update the DB_URL, USER, and PASSWORD constants in DatabaseCode.java to match your MySQL credentials.

Compile the code:

bash
javac -cp .:mysql-connector-java.jar DatabaseCode.java
Run the application:

bash
java -cp .:mysql-connector-java.jar DBMS.Main
Usage
The application provides a menu for all operations.
Follow prompts to add/view/update/delete suppliers, products, users, transactions, and reorders.
Use automated prompts for reordering stock and checking profit.
