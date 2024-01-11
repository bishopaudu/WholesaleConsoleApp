package org.example;
import java.sql.*;
import java.util.Scanner;

public class WholesaleConsoleApp {
    private static final String url = "jdbc:sqlite:wholesale.db";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(url)) {
            createTable(connection);

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Add product");
                System.out.println("2. View products");
                System.out.println("3. Update product price");
                System.out.println("4. Delete product");
                System.out.println("5. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addProduct(connection, scanner);
                        break;
                    case 2:
                        viewProducts(connection);
                        break;
                    case 3:
                        updateProductPriceById(connection,scanner);
                        break;
                    case 4:
                        deleteProduct(connection, scanner);
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, SKU TEXT, description TEXT, category TEXT, price INTEGER)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        }
    }

    private static void addProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter SKU:");
        String SKU = scanner.nextLine();

        System.out.println("Enter description:");
        String description = scanner.nextLine();

        System.out.println("Enter category:");
        String category = scanner.nextLine();

        System.out.println("Enter price:");
        int price = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        ProductModel product = new ProductModel(0, SKU, description, category, price);
        insertProduct(connection, product);
        System.out.println("Product added successfully.");
    }

    private static void insertProduct(Connection connection, ProductModel product) throws SQLException {
        String insertSQL = "INSERT INTO products (SKU, description, category, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, product.getSKU());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getCategory());
            preparedStatement.setInt(4, product.getPrice());
            preparedStatement.executeUpdate();
        }
    }

    private static void viewProducts(Connection connection) throws SQLException {
        System.out.println("\nProducts:");

        String selectSQL = "SELECT id, SKU, description, category, price FROM products";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String SKU = resultSet.getString("SKU");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");
                int price = resultSet.getInt("price");
                System.out.println("ID: " + id + ", SKU: " + SKU + ", Description: " + description + ", Category: " + category + ", Price: " + price);
            }
        }
    }

    private static void updateProductPriceById(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter product ID to update:");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter new price:");
        int newPrice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String updateSQL = "UPDATE products SET price = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, newPrice);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();
            System.out.println("Product price updated successfully.");
        }
    }


    private static void deleteProduct(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter id of product to delete:");
        String id = scanner.nextLine();

        String deleteSQL = "DELETE FROM products WHERE SKU = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product with SKU '" + id + "' not found.");
            }
        }
    }



}
