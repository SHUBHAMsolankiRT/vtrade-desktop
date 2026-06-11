package client_side.ui_screens;

import client_side.database.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
// import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupScreen extends JFrame {
    JTextField nameField;
    JPasswordField pinField, confirmPinField;
    JButton registerBtn, backBtn;

    public SignupScreen() {
        setTitle("V-Trade : Open Demat Account");
        setSize(400, 400);
        setLocationRelativeTo(null); // Center of screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Sirf ye window band hogi

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Name Input
        JPanel p1 = new JPanel(new GridLayout(2, 1));
        p1.setOpaque(false);
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(Color.WHITE);
        nameField = new JTextField();
        p1.add(nameLabel); p1.add(nameField);

        // 2. PIN Input
        JPanel p2 = new JPanel(new GridLayout(2, 1));
        p2.setOpaque(false);
        JLabel pinLabel = new JLabel("Create 4-Digit PIN:");
        pinLabel.setForeground(Color.WHITE);
        pinField = new JPasswordField();
        p2.add(pinLabel); p2.add(pinField);

        // 3. Confirm PIN Input
        JPanel p3 = new JPanel(new GridLayout(2, 1));
        p3.setOpaque(false);
        JLabel confirmLabel = new JLabel("Confirm 4-Digit PIN:");
        confirmLabel.setForeground(Color.WHITE);
        confirmPinField = new JPasswordField();
        p3.add(confirmLabel); p3.add(confirmPinField);

        // 4. Buttons
        JPanel p4 = new JPanel(new FlowLayout());
        p4.setOpaque(false);
        
        registerBtn = new JButton("CREATE ACCOUNT");
        registerBtn.setBackground(new Color(0, 153, 0));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);

        backBtn = new JButton("Back to Login");
        backBtn.setBackground(Color.GRAY);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);

        p4.add(registerBtn);
        p4.add(backBtn);

        mainPanel.add(new JLabel("<html><h2 style='color:#0099ff;'>Join V-Trade</h2></html>"));
        mainPanel.add(p1); mainPanel.add(p2); mainPanel.add(p3); mainPanel.add(p4);

        add(mainPanel);
        setVisible(true);

        // --- BUTTON ACTIONS ---
        backBtn.addActionListener(e -> dispose()); // Band kardo

        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String pin = new String(pinField.getPassword());
            String confirmPin = new String(confirmPinField.getPassword());

            if(name.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!pin.equals(confirmPin)) {
                JOptionPane.showMessageDialog(this, "PINs do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                if(conn != null) {
                    // RETURN_GENERATED_KEYS se hume naya auto-increment ID mil jayega
                    String query = "INSERT INTO users (full_name, pin, virtual_cash) VALUES (?, ?, 100000.00)";
                    PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    pst.setString(1, name);
                    pst.setInt(2, Integer.parseInt(pin));
                    
                    int affectedRows = pst.executeUpdate();
                    if(affectedRows > 0) {
                        ResultSet rs = pst.getGeneratedKeys();
                        if(rs.next()) {
                            int newId = rs.getInt(1);
                            JOptionPane.showMessageDialog(this, 
                                "Account Created Successfully!\nYour Trader ID is: " + newId + "\nKeep it safe to login.", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose(); // Signup band, wapas login par
                        }
                    }
                }
            } catch (HeadlessException | NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}