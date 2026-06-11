package client_side.ui_screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import client_side.database.DBConnection;

public class ModernLoginScreen extends JFrame {
    JTextField idField;
    JPasswordField pinField; 
    JButton loginBtn, signupBtn;

    public ModernLoginScreen() {
        setTitle("V-Trade: Secure Login");
        setSize(400, 350);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        mainPanel.setBackground(new Color(20, 20, 20)); 
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Title
        JLabel title = new JLabel("<html><h1 style='color:#00b852;'>V-TRADE PRO</h1></html>", SwingConstants.CENTER);
        mainPanel.add(title);

        // Inputs Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 15));
        inputPanel.setOpaque(false);
        
        JLabel idLabel = new JLabel("Trader ID:");
        idLabel.setForeground(Color.WHITE);
        idField = new JTextField();
        
        JLabel pinLabel = new JLabel("4-Digit PIN:");
        pinLabel.setForeground(Color.WHITE);
        pinField = new JPasswordField(); 

        inputPanel.add(idLabel); inputPanel.add(idField);
        inputPanel.add(pinLabel); inputPanel.add(pinField);
        mainPanel.add(inputPanel);

        // Login Button
        loginBtn = new JButton("SECURE LOGIN");
        loginBtn.setBackground(new Color(0, 184, 82)); // Groww Green
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        mainPanel.add(loginBtn);

        // Signup Button
        signupBtn = new JButton("New User? Open Account");
        signupBtn.setBackground(new Color(50, 50, 50));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFocusPainted(false);
        mainPanel.add(signupBtn);

        add(mainPanel);
        setVisible(true);

        // --- ACTIONS ---
        signupBtn.addActionListener(e -> {
            // FIX: Assigning to variable to remove 'New instance ignored' warning
            SignupScreen signup = new SignupScreen();
            signup.setVisible(true);
        });

        loginBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int pin = Integer.parseInt(new String(pinField.getPassword()));

                Connection conn = DBConnection.getConnection();
                if (conn != null) {
                    PreparedStatement pst = conn.prepareStatement("SELECT full_name FROM users WHERE user_id = ? AND pin = ?");
                    pst.setInt(1, id); pst.setInt(2, pin);
                    ResultSet rs = pst.executeQuery();
                    
                    if (rs.next()) {
                        String name = rs.getString("full_name");
                        
                        // FIX: Assigning to variable to remove 'New instance ignored' warning
                        VTradeProDesktop desktop = new VTradeProDesktop(name); 
                        desktop.setVisible(true);
                        
                        dispose(); // Login window band
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid ID or PIN!", "Access Denied", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (HeadlessException | NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        // FIX: Assigning to variable and using invokeLater for thread safety
        SwingUtilities.invokeLater(() -> {
            ModernLoginScreen app = new ModernLoginScreen();
            app.setVisible(true);
        });
    }
}