package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
this class was used to test connection to the database
 */
public class Connect {
    public static void connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:wholesale.db"; // creates a db in the product directory
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        connect();
    }
}
