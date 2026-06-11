package client_side.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Database credentials - Dhyan se dekhna!
    private static final String URL = "jdbc:mysql://localhost:3306/vtrade_db";
    private static final String USER = "root";
    
    // QT, YAHAN APNA ASLI MYSQL PASSWORD LIKHNA JO TUMNE INSTALL KARTE WAQT BANAYA THA
    private static final String PASSWORD = "@Fojjiboss123"; 

    // Ye method jab bhi call hoga, ek naya connection banayega
    public static Connection getConnection() {
        try {
            // Step 1: Register the JDBC Driver (Viva ke liye important line)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Step 2: Open a connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: MySQL JDBC Driver nahi mila! Jar file check karo.");
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Error: Database connect nahi hua. Password ya DB name check karo.");
            return null;
        }
    }

    // Is file ko test karne ke liye ek chota sa main method
    public static void main(String[] args) {
        System.out.println("Connecting to Database...");
        Connection conn = getConnection();
        
        if (conn != null) {
            System.out.println("✅ CONGRATULATIONS! Java aur MySQL ki dosti ho gayi!");
        }
    }
}