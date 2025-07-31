package DBMS;
import java.sql.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class Main {
	static Scanner sc = new Scanner(System.in);
	private static final String DB_URL = "jdbc:mysql://localhost:3306/dbms_mini?useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "chikya";
	Connection conn;
	public Main() throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
	}

	//SUPPLIER OPERATIONS:
	//INSERT
	public static void addSupplier(Connection conn, Scanner sc) throws SQLException {
		System.out.println("Enter Supplier Name: ");
		String name = sc.nextLine();

		System.out.println("Enter Contact Phone: ");
		String contactPhone = sc.nextLine();

		System.out.println("Enter Contact Email: ");
		String contactEmail = sc.nextLine();

		System.out.println("Enter Address: ");
		String address = sc.nextLine();

		String sql = "INSERT INTO Suppliers (supplier_name, supplier_contact_phone, supplier_contact_email, supplier_address) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, contactPhone);
			pstmt.setString(3, contactEmail);
			pstmt.setString(4, address);
			pstmt.executeUpdate();
			System.out.println("Supplier added successfully!");
		}
	}


	//DISPLAY ALL
	public static void getAllSuppliers(Connection conn, Scanner sc) throws SQLException {
		String sql = "SELECT * FROM Suppliers";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				int id = rs.getInt("supplier_id");
				String name = rs.getString("supplier_name");
				String phone = rs.getString("supplier_contact_phone");
				String email = rs.getString("supplier_contact_email");
				String address = rs.getString("supplier_address");
				System.out.printf("ID: %d, Name: %s, Phone: %s, Email: %s, Address: %s%n", id, name, phone, email, address);
			}
		}
	}

	//UPDATE SUPPLIER DATA
	public static void updateSupplier(Connection conn, Scanner sc) throws SQLException {
		// Validate column input to prevent SQL injection
		System.out.print("Enter supplier ID: ");
		int supplierId = sc.nextInt();
		sc.nextLine();  // Consume newline

		System.out.print("Enter column to update (supplier_name, supplier_contact_phone, supplier_contact_email, supplier_address): ");
		String column = sc.nextLine().trim();

		System.out.print("Enter new value: ");
		String newValue = sc.nextLine().trim();

		if (!column.matches("supplier_name|supplier_contact_phone|supplier_contact_email|supplier_address")) {
			System.out.println("Invalid column name.");
			return;
		}

		String sql = "UPDATE Suppliers SET " + column + " = ? WHERE supplier_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, newValue);
			pstmt.setInt(2, supplierId);
			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Supplier updated successfully!");
			} else {
				System.out.println("Supplier not found.");
			}
		}
	}

	//DELETE
	public void deleteSupplier(Connection conn, Scanner sc) throws SQLException {
	    System.out.print("Enter Supplier ID to delete: ");
	    int supplierId = sc.nextInt();
	    sc.nextLine(); // Consume newline left-over

	    String sql = "DELETE FROM Suppliers WHERE supplier_id = ?";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, supplierId);
	        
	        int rowsDeleted = pstmt.executeUpdate();
	        if (rowsDeleted > 0) {
	            System.out.println("Supplier deleted successfully!");
	        } else {
	            System.out.println("Supplier not found.");
	        }
	    }
	}


	/////////////////////////////////////////////////
	//PRODUCTS:

	// Method to insert a product
	private static void insertProduct(Connection conn, Scanner scanner) throws SQLException {
		System.out.print("Enter product name: ");
		String name = scanner.next();
		System.out.print("Enter price: ");
		double price = scanner.nextDouble();
		System.out.print("Enter quantity in stock: ");
		int quantity = scanner.nextInt();
		System.out.print("Enter reorder level: ");
		int reorderLevel = scanner.nextInt();
		System.out.print("Enter supplier ID: ");
		int supplierId = scanner.nextInt();
		System.out.print("Enter category: ");
		String category = scanner.next();
		System.out.print("Enter expiry date (YYYY-MM-DD): ");
		String expiryDate = scanner.next();

		String insertSQL = "INSERT INTO Products (p_name, p_price, p_quantity_in_stock, p_reorder_level, supplier_id, p_category, p_expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
			ps.setString(1, name);
			ps.setDouble(2, price);
			ps.setInt(3, quantity);
			ps.setInt(4, reorderLevel);
			ps.setInt(5, supplierId);
			ps.setString(6, category);
			ps.setDate(7, Date.valueOf(expiryDate));
			ps.executeUpdate();
			System.out.println("Product inserted successfully.");
		}
	}

	// Method to retrieve and display all products
	private static void retrieveAllProducts(Connection conn) throws SQLException {
		String selectSQL = "SELECT * FROM Products";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
			while (rs.next()) {
				System.out.println("Product ID: " + rs.getInt("p_id") +
						", Name: " + rs.getString("p_name") +
						", Price: " + rs.getDouble("p_price") +
						", Quantity in Stock: " + rs.getInt("p_quantity_in_stock") +
						", Reorder Level: " + rs.getInt("p_reorder_level") +
						", Supplier ID: " + rs.getInt("supplier_id") +
						", Category: " + rs.getString("p_category") +
						", Expiry Date: " + rs.getDate("p_expiry_date"));
			}
		}
	}

	public static void retrieveProductByName(Connection conn, Scanner sc) throws SQLException {
		sc.nextLine();
	    System.out.print("Enter Product Name: ");
	    String productName = sc.nextLine();

	    String selectSQL = "SELECT * FROM Products WHERE p_name = ?";
	    try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
	        pstmt.setString(1, productName);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                System.out.println("Product ID: " + rs.getInt("p_id") +
	                        ", Name: " + rs.getString("p_name") +
	                        ", Price: " + rs.getDouble("p_price") +
	                        ", Quantity in Stock: " + rs.getInt("p_quantity_in_stock") +
	                        ", Reorder Level: " + rs.getInt("p_reorder_level") +
	                        ", Supplier ID: " + rs.getInt("supplier_id") +
	                        ", Category: " + rs.getString("p_category") +
	                        ", Expiry Date: " + rs.getDate("p_expiry_date"));
	            } else {
	                System.out.println("Product not found.");
	            }
	        }
	    }
	}

	// Method to update a product
	private static void updateProduct(Connection conn, Scanner scanner) throws SQLException {
		System.out.print("Do you want to view all product IDs and names? (yes/no): ");
		String viewProducts = scanner.next();

		if (viewProducts.equalsIgnoreCase("yes")) {
			String selectProductsSQL = "SELECT p_id, p_name FROM Products";
			try (Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(selectProductsSQL)) {
				System.out.println("Available Products:");
				while (rs.next()) {
					System.out.println("Product ID: " + rs.getInt("p_id") + ", Name: " + rs.getString("p_name"));
				}
			}
		}

		System.out.print("Enter product ID to update: ");
		int productId = scanner.nextInt();
		System.out.print("Enter new name: ");
		String name = scanner.next();
		System.out.print("Enter new price: ");
		double price = scanner.nextDouble();
		System.out.print("Enter new quantity in stock: ");
		int quantity = scanner.nextInt();
		System.out.print("Enter new reorder level: ");
		int reorderLevel = scanner.nextInt();
		System.out.print("Enter new supplier ID: ");
		int supplierId = scanner.nextInt();
		System.out.print("Enter new category: ");
		String category = scanner.next();
		System.out.print("Enter new expiry date (YYYY-MM-DD): ");
		String expiryDate = scanner.next();

		String updateSQL = "UPDATE Products SET p_name = ?, p_price = ?, p_quantity_in_stock = ?, p_reorder_level = ?, supplier_id = ?, p_category = ?, p_expiry_date = ? WHERE p_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
			ps.setString(1, name);
			ps.setDouble(2, price);
			ps.setInt(3, quantity);
			ps.setInt(4, reorderLevel);
			ps.setInt(5, supplierId);
			ps.setString(6, category);
			ps.setDate(7, Date.valueOf(expiryDate));
			ps.setInt(8, productId);
			int rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Product updated successfully.");
			} else {
				System.out.println("Product ID not found.");
			}
		}
	}

	// Method to delete a product
	private static void deleteProduct(Connection conn, Scanner scanner) throws SQLException {
		System.out.print("Enter product ID to delete: ");
		int productId = scanner.nextInt();

		String deleteSQL = "DELETE FROM Products WHERE p_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
			ps.setInt(1, productId);
			int rowsDeleted = ps.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Product deleted successfully.");
			} else {
				System.out.println("Product ID not found.");
			}
		}
	}

	//TRANSACTIONS:
	// Method to insert a transaction
	private static int insertTransaction(Connection conn, Scanner scanner) throws SQLException {
		System.out.print("Enter product ID: ");
		int productId = scanner.nextInt();
		System.out.print("Enter quantity sold: ");
		int quantity = scanner.nextInt();
		System.out.print("Enter sale rate: ");
		double rate = scanner.nextDouble();
		System.out.print("Enter user ID: ");
		int userId = scanner.nextInt();

		String insertSQL = "INSERT INTO Transactions (p_id, t_quantity_sold, t_rate, t_transaction_date, u_id) VALUES (?, ?, ?, NOW(), ?)";
		try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
			ps.setInt(1, productId);
			ps.setInt(2, quantity);
			ps.setDouble(3, rate);
			ps.setInt(4, userId);
			ps.executeUpdate();
			System.out.println("Transaction inserted successfully.");
		}

		String selectSQL = "SELECT p_quantity_in_stock, p_reorder_level FROM Products WHERE p_id = ?";
		try (PreparedStatement psSelect = conn.prepareStatement(selectSQL)) {
			psSelect.setInt(1, productId);
			ResultSet rs = psSelect.executeQuery();

			if (rs.next()) {
				int stockLevel = rs.getInt("p_quantity_in_stock");
				int reorderLevel = rs.getInt("p_reorder_level");

				// Check if there is enough stock
				if (stockLevel >= quantity) {
					// Enough stock, proceed with the transaction
					return -1; // Transaction was successful
				} else {
					return stockLevel; // Not enough stock
				}
			} else {
				System.out.println("Product not found.");
				return -1; // Product does not exist
			}
		}
	}

	// Method to retrieve and display all transactions
	private static void retrieveAllTransactions(final Connection conn) throws SQLException {
		String selectSQL = """
				SELECT t_id, Products.p_name, t_quantity_sold, t_rate, t_transaction_date, t_total_price
				FROM Transactions
				JOIN Products ON Transactions.p_id = Products.p_id
				""";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
			while (rs.next()) {
				System.out.println("Transaction ID: " + rs.getInt("t_id") +
						", Product Name: " + rs.getString("p_name") +
						", Quantity Sold: " + rs.getInt("t_quantity_sold") +
						", Rate: " + rs.getDouble("t_rate") +
						", Transaction Date: " + rs.getTimestamp("t_transaction_date") +
						", Total Price: " + rs.getDouble("t_total_price"));
			}
		}
	}	

	// Method to retrieve and display all transactions grouped by product name
	private static void retrieveTransactionsByProduct(Connection conn) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter product name to view Sales: ");
		String productname = scanner.nextLine();

		// Correct SQL with WHERE clause to filter by product name
		String selectSQL = """
				SELECT Products.p_name, Transactions.t_id, Transactions.t_quantity_sold, 
				       Transactions.t_rate, Transactions.t_transaction_date, Transactions.t_total_price
				FROM Transactions
				JOIN Products ON Transactions.p_id = Products.p_id
				WHERE Products.p_name = ?
				ORDER BY Products.p_name, Transactions.t_transaction_date
				""";

		try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
			// Set the product name parameter from user input
			ps.setString(1, productname);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.isBeforeFirst()) {
					System.out.println("No sales found for product: " + productname);
					return;
				}

				// Loop through the result set and display transaction details
				while (rs.next()) {
					// Retrieve data for each column
					int transactionId = rs.getInt("t_id");
					int quantitySold = rs.getInt("t_quantity_sold");
					double rate = rs.getDouble("t_rate");
					Timestamp transactionDate = rs.getTimestamp("t_transaction_date");
					double totalPrice = rs.getDouble("t_total_price");

					// Display transaction details
					System.out.println("Transaction ID: " + transactionId);
					System.out.println("Quantity Sold: " + quantitySold);
					System.out.println("Rate: " + rate);
					System.out.println("Transaction Date: " + transactionDate);
					System.out.println("Total Price: " + totalPrice);
					System.out.println("====================================");
				}
			}
		}
	}


	/////////////////////////////////////////
	//USERS:
	public static void createUser(Connection conn, Scanner sc) throws SQLException {
		System.out.print("Enter Username: ");
		String username = sc.nextLine();

		System.out.print("Enter Password: ");
		String password = sc.nextLine();

		System.out.print("Enter Role: ");
		String role = sc.nextLine();

		String sql = "INSERT INTO Users (u_username, u_password, u_role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, role);

			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("User created successfully!");
			}
		}
	}


	public static void seeUser(Connection conn, Scanner sc) throws SQLException {
		System.out.print("Enter User ID: ");
		int userId = sc.nextInt();
		sc.nextLine(); // Consume newline left-over

		String sql = "SELECT * FROM Users WHERE u_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("User ID: " + rs.getInt("u_id"));
					System.out.println("Username: " + rs.getString("u_username"));
					System.out.println("Role: " + rs.getString("u_role"));
				} else {
					System.out.println("User not found.");
				}
			}
		}
	}
	
	public static void displayAllUsers(Connection conn) throws SQLException {
	    String sql = "SELECT * FROM Users";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {
	        
	        System.out.println("All Users:");
	        System.out.println("---------------------------------");
	        while (rs.next()) {
	            System.out.println("User ID: " + rs.getInt("u_id"));
	            System.out.println("Username: " + rs.getString("u_username"));
	            System.out.println("Role: " + rs.getString("u_role"));
	            System.out.println("---------------------------------");
	        }
	    }
	}

	public void updateUser(Connection conn, Scanner scanner) throws SQLException {
		System.out.print("Enter user ID to update: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		System.out.print("Enter the column you want to update (u_username, u_password, u_role): ");
		String column = scanner.nextLine().trim();

		// Validate column input to prevent SQL injection
		if (!column.matches("u_username|u_password|u_role")) {
			System.out.println("Invalid column name.");
			return;
		}

		System.out.print("Enter the new value for " + column + ": ");
		String newValue = scanner.nextLine().trim();

		String updateSQL = "UPDATE Users SET " + column + " = ? WHERE u_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
			ps.setString(1, newValue);
			ps.setInt(2, userId);

			int rowsUpdated = ps.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("User updated successfully.");
			} else {
				System.out.println("User ID not found.");
			}
		}
	}

	public static void deleteUser(Connection conn, Scanner sc) throws SQLException {
	    System.out.print("Enter User ID to delete: ");
	    int userId = sc.nextInt();
	    sc.nextLine(); // Consume newline left-over

	    String sql = "DELETE FROM Users WHERE u_id = ?";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, userId);
	        
	        int rowsDeleted = pstmt.executeUpdate();
	        if (rowsDeleted > 0) {
	            System.out.println("User deleted successfully!");
	        } else {
	            System.out.println("User ID not found.");
	        }
	    }
	}

	//////////////////////////////////////
	//REORDERS:
	public static void createReorder(Connection conn) throws SQLException {
		Scanner scanner = new Scanner(System.in);

		// Take user input for reorder details
		System.out.print("Enter product ID: ");
		int productId = scanner.nextInt();

		System.out.print("Enter reorder quantity: ");
		int quantity = scanner.nextInt();

		System.out.print("Enter supplier ID: ");
		int supplierId = scanner.nextInt();

		System.out.print("Enter user ID: ");
		int userId = scanner.nextInt();

		// SQL query to insert a reorder
		String insertSQL = "INSERT INTO Reorders (p_id, r_quantity, supplier_id, u_id) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
			// Set parameters from user input
			ps.setInt(1, productId);
			ps.setInt(2, quantity);
			ps.setInt(3, supplierId);
			ps.setInt(4, userId);

			int rowsInserted = ps.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Reorder created successfully!");
			} else {
				System.out.println("Error creating reorder.");
			}
		}
	}


	public static void viewAllReorders(Connection conn) throws SQLException {
		String selectSQL = """
				SELECT r_id, Products.p_name, r_quantity, r_order_date, r_status, Suppliers.supplier_name, Users.u_username
				FROM Reorders
				JOIN Products ON Reorders.p_id = Products.p_id
				JOIN Suppliers ON Reorders.supplier_id = Suppliers.supplier_id
				JOIN Users ON Reorders.u_id = Users.u_id
				""";
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectSQL)) {
			System.out.println("Reorders List:");
			while (rs.next()) {
				System.out.println("Reorder ID: " + rs.getInt("r_id") +
						", Product Name: " + rs.getString("p_name") +
						", Quantity: " + rs.getInt("r_quantity") +
						", Order Date: " + rs.getTimestamp("r_order_date") +
						", Status: " + rs.getString("r_status") +
						", Supplier: " + rs.getString("supplier_name") +
						", User: " + rs.getString("u_username"));
			}
		}
	}

	public static void displayReordersForProduct(Connection conn) throws SQLException {
		Scanner scanner = new Scanner(System.in);

		// Take user input for the product name
		System.out.print("Enter product name to view reorders: ");
		String productName = scanner.nextLine();

		// Query to retrieve reorder details for a specific product
		String query = "SELECT r.r_id, r.p_id, r.r_quantity, r.r_order_date, r.r_status, r.supplier_id, r.u_id " +
				"FROM Reorders r " +
				"JOIN Products p ON r.p_id = p.p_id " +
				"WHERE p.p_name = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, productName);  // Set the product name parameter from user input

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.isBeforeFirst()) {
					System.out.println("No reorders found for product: " + productName);
					return;
				}

				// Loop through the result set and display the reorder details
				while (rs.next()) {
					int reorderId = rs.getInt("r_id");
					int productId = rs.getInt("p_id");
					int quantity = rs.getInt("r_quantity");
					Timestamp orderDate = rs.getTimestamp("r_order_date");
					String status = rs.getString("r_status");
					int supplierId = rs.getInt("supplier_id");
					int userId = rs.getInt("u_id");

					// Display reorder details
					System.out.println("Reorder ID: " + reorderId);
					System.out.println("Product ID: " + productId);
					System.out.println("Quantity: " + quantity);
					System.out.println("Order Date: " + orderDate);
					System.out.println("Status: " + status);
					System.out.println("Supplier ID: " + supplierId);
					System.out.println("User ID: " + userId);
					System.out.println("====================================");
				}
			}
		}
	}

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
			while (true) {
				System.out.println("Menu:");
				System.out.println("1. Add a Sale");
				System.out.println("2. Check Stock");
				System.out.println("3. Restock");
				System.out.println("4. Update Price of Product");
				System.out.println("5. Check Profit");
				System.out.println("6. View Sales");
				System.out.println("7. View ReOrders");
				System.out.println("8. View Products");
				System.out.println("9. Add New Product");
				System.out.println("10. Add Supplier");
				System.out.println("11. Display All Suppliers");
				System.out.println("12. Modify Supplier Data");
				System.out.println("13. Add User");
				System.out.println("14. View All Users");
				System.out.println("15. Delete User");
				System.out.println("0. Exit");
				System.out.print("Enter your choice: ");
				int choice = sc.nextInt();

				switch (choice) {
				case 1 -> addSale(conn, sc);
				case 2 -> checkStock(conn, sc);
				case 3 -> restock(conn, sc);
				case 4 -> updatePrice(conn, sc);
				case 5 -> checkProfit(conn, sc);
				case 6 -> viewSales(conn);
				case 7 -> viewReOrders(conn);
				case 8 -> viewProduct(conn);
				case 9 -> insertProduct(conn, sc);
				case 10 -> addSupplier(conn, sc);
				case 11 -> getAllSuppliers(conn, sc);
				case 12 -> updateSupplier(conn, sc);
				case 13 -> createUser(conn, sc);
				case 14 -> displayUsers(conn);
				case 15 -> deleteUser(conn, sc);
				case 0 -> System.exit(0);
				default -> System.out.println("Invalid choice. Try again.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static void addSale(Connection conn, Scanner scanner) throws SQLException {
		int t=insertTransaction(conn,sc);
		if(!(t==-1)) {
			System.out.println("Stock now Below Optimal Level!\nCurrent Stock Level: "+t+"\nTime to Re-Stock!");
			System.out.print("Do you want to Re-Stock now(yes/no)? ");
			String in=sc.nextLine();
			if(in.equals("yes")) {
				restock(conn, sc);
			}
		}
	}


	private static void checkStock(Connection conn, Scanner scanner) throws SQLException {
		System.out.print("Enter product ID to check stock: ");
		int productId = scanner.nextInt();

		// Use the stored function to get stock quantity
		String checkStockQuery = "SELECT get_stock_quantity(?) AS stock_available";

		try (PreparedStatement ps = conn.prepareStatement(checkStockQuery)) {
			// Set the product ID as input parameter for the function
			ps.setInt(1, productId);

			// Execute the query and retrieve the stock quantity
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("Stock available: " + rs.getInt("stock_available"));
			} else {
				System.out.println("Product not found.");
			}
		}
	}


	private static void restock(Connection conn, Scanner scanner) throws SQLException {
		createReorder(conn);
	}

	private static void updatePrice(Connection conn, Scanner scanner) throws SQLException {
		scanner.nextLine();    
		System.out.print("Enter product name to update price: ");
		String productName = scanner.nextLine();
		System.out.print("Enter new price: ");
		double newPrice = scanner.nextDouble();
		scanner.nextLine(); // Consume the newline left-over

		// Query to fetch productId based on productName
		String getProductIdQuery = "SELECT p_id FROM Products WHERE p_name = ?";
		int productId = -1;  // Default value if product not found

		try (PreparedStatement ps = conn.prepareStatement(getProductIdQuery)) {
			ps.setString(1, productName);
			ResultSet rs = ps.executeQuery();

			// If the product is found
			if (rs.next()) {
				productId = rs.getInt("p_id");
			} else {
				System.out.println("Product not found: " + productName);
				return;  // Exit if product is not found
			}
		}

		// Define the SQL call to the stored procedure
		String updatePriceProcedureById = "{CALL update_product_price(?, ?)}";

		// Use a CallableStatement to execute the stored procedure
		try (CallableStatement cs = conn.prepareCall(updatePriceProcedureById)) {
			// Set the input parameters for the stored procedure
			cs.setInt(1, productId);  // Pass productId
			cs.setDouble(2, newPrice);  // Pass new price

			// Execute the procedure
			cs.execute();

			System.out.println("Price updated successfully using stored procedure by product ID.");
		}
	}


	private static void checkProfit(Connection conn, Scanner scanner) throws SQLException {
		// Take product name as input
		scanner.nextLine();  // Clear the newline character from the scanner buffer
		System.out.print("Enter product name for profit check: ");
		String productName = scanner.nextLine();

		// Query to get the products matching the provided name
		String productQuery = "SELECT p_id, p_name, p_price FROM Products WHERE p_name = ?";
		try (PreparedStatement ps = conn.prepareStatement(productQuery)) {
			ps.setString(1, productName);
			ResultSet rs = ps.executeQuery();

			// If no products are found with that name
			if (!rs.isBeforeFirst()) {
				System.out.println("No products found with the name: " + productName);
				return;
			}

			// Get the product price and ID
			int productId = rs.getInt("p_id");
			double productPrice = rs.getDouble("p_price");

			// Query to get all transactions for the selected product
			String transactionQuery = "SELECT t_quantity_sold, t_rate, t_transaction_date " +
					"FROM Transactions WHERE p_id = ?";

			try (PreparedStatement psTransactions = conn.prepareStatement(transactionQuery)) {
				psTransactions.setInt(1, productId);
				ResultSet transactionRs = psTransactions.executeQuery();

				double totalProfit = 0;
				boolean foundTransactions = false;

				// Display each transaction's details and calculate the total profit
				while (transactionRs.next()) {
					int quantitySold = transactionRs.getInt("t_quantity_sold");
					double saleRate = transactionRs.getDouble("t_rate");
					double transactionProfit = (saleRate - productPrice) * quantitySold;
					totalProfit += transactionProfit;

					System.out.println("Transaction Date: " + transactionRs.getDate("t_transaction_date"));
					System.out.println("Quantity Sold: " + quantitySold);
					System.out.println("Sale Rate: " + saleRate);
					System.out.println("Product Price: " + productPrice);
					System.out.println("Profit for this transaction: " + transactionProfit);
					System.out.println("-------------------------------");

					foundTransactions = true;
				}

				if (foundTransactions) {
					System.out.println("Total profit for the selected product (" + productName + "): " + totalProfit);
				} else {
					System.out.println("No transactions found for this product.");
				}
			}
		}
	}


	private static void viewSales(Connection conn) throws SQLException {
		System.out.print("1. View all Sales\n2. View sales of a product\nEnter Your choice: ");
		int choice =sc.nextInt();
		if(choice==1) {
			retrieveAllTransactions(conn);
		}
		else if(choice==2){
			retrieveTransactionsByProduct(conn);
		}
		else {
			System.out.println("Wrong Input! Try again!");
		}
	}

	private static void viewReOrders(Connection conn) throws SQLException {
		System.out.print("1. View all Re-Orders\n2. View Re-Orders of a product\nEnter Your choice: ");
		int choice =sc.nextInt();
		if(choice==1) {
			viewAllReorders(conn);
		}
		else if(choice==2){
			displayReordersForProduct(conn);
		}
		else {
			System.out.println("Wrong Input! Try again!");
		}
	}
	
	private static void displayUsers(Connection conn) throws SQLException {
		System.out.print("1. View all Re-Orders\n2. View Re-Orders of a product\nEnter Your choice: ");
		int choice =sc.nextInt();
		if(choice==1) {
			displayAllUsers(conn);
		}
		else if(choice==2){
			seeUser(conn,sc);
		}
		else {
			System.out.println("Wrong Input! Try again!");
		}
	}
	
	private static void viewProduct(Connection conn) throws SQLException {
		System.out.print("1. View all Re-Orders\n2. View Re-Orders of a product\nEnter Your choice: ");
		int choice =sc.nextInt();
		if(choice==1) {
			retrieveAllProducts(conn);
		}
		else if(choice==2){
			retrieveProductByName(conn,sc);
		}
		else {
			System.out.println("Wrong Input! Try again!");
		}
	}
}