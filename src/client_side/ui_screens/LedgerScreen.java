package client_side.ui_screens;

import client_side.database.DBConnection;
import client_side.utils.ReceiptGenerator; // Import for the receipt

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LedgerScreen extends JFrame {
    // UI Colors (Matching Pro Desktop)
    Color bgColor = new Color(18, 18, 18);
    Color panelColor = new Color(30, 30, 30);
    Color textColor = new Color(220, 220, 220);

    public LedgerScreen(String userName) {
        setTitle("V-Trade Pro : Trade Ledger & History");
        setSize(900, 500); // Thoda bada kiya naye column ke liye
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Title
        JLabel titleLabel = new JLabel("📜 Order History & Ledger : " + userName, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 184, 82)); 
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- THE JTABLE SETUP (Naya Segment Column add kiya) ---
        String[] columns = {"Date & Time", "Stock Ticker", "Segment", "Trade Type", "Quantity", "Price (₹)", "Total Value (₹)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable historyTable = new JTable(tableModel);

        // Table Styling
        historyTable.setBackground(panelColor);
        historyTable.setForeground(textColor);
        historyTable.setGridColor(new Color(60, 60, 60));
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        historyTable.setRowHeight(30);
        historyTable.setSelectionBackground(new Color(70, 130, 180)); // Select karne par Blue
        historyTable.setSelectionForeground(Color.WHITE);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Ek baar mein ek hi select ho

        // Header Styling
        JTableHeader header = historyTable.getTableHeader();
        header.setBackground(new Color(45, 45, 45));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(100, 35));

        // Center align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(bgColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- NAYA FEATURE: DOWNLOAD BUTTON ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(bgColor);
        
        JButton downloadBtn = new JButton("📥 Download Selected Receipt");
        downloadBtn.setBackground(new Color(70, 130, 180));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        downloadBtn.setFocusPainted(false);
        downloadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        downloadBtn.addActionListener(e -> {
            int selectedRow = historyTable.getSelectedRow();
            if (selectedRow != -1) {
                // Table se data nikalo
                String time = tableModel.getValueAt(selectedRow, 0).toString();
                String ticker = tableModel.getValueAt(selectedRow, 1).toString();
                String segment = tableModel.getValueAt(selectedRow, 2).toString();
                String type = tableModel.getValueAt(selectedRow, 3).toString();
                int qty = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
                double price = Double.parseDouble(tableModel.getValueAt(selectedRow, 5).toString());
                double total = Double.parseDouble(tableModel.getValueAt(selectedRow, 6).toString());

                // Receipt Generator ko call karo
                boolean success = ReceiptGenerator.generateReceipt(userName, ticker, segment, type, qty, price, total, time);
                
                if(success) {
                    JOptionPane.showMessageDialog(this, "✅ Contract Note generated in 'user_receipts' folder!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to generate receipt.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Please click on a trade in the table first!", "Select Trade", JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomPanel.add(downloadBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Fetch Data from Database
        loadHistoryData(userName, tableModel);

        add(mainPanel);
        setVisible(true);
    }

    // --- SQL MEIN NAYA CASE LOGIC: Segment pata lagane ke liye ---
    @SuppressWarnings("UseSpecificCatch")
    private void loadHistoryData(String userName, DefaultTableModel tableModel) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                // CE ya PE dekha toh 'F&O', warna 'Equity'
                String query = "SELECT t.trade_time, t.ticker, t.trade_type, t.qty, t.price_at_trade, " +
                               "CASE WHEN t.ticker LIKE '%CE' OR t.ticker LIKE '%PE' THEN 'F&O Trading' ELSE 'Equity Stock' END as segment " +
                               "FROM trade_transactions t JOIN users u ON t.user_id = u.user_id " +
                               "WHERE u.full_name = ? ORDER BY t.trade_time DESC";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, userName);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String time = rs.getString("trade_time");
                    String ticker = rs.getString("ticker");
                    String segment = rs.getString("segment"); // Naya fetched column
                    String type = rs.getString("trade_type");
                    int qty = rs.getInt("qty");
                    double price = rs.getDouble("price_at_trade");
                    double total = qty * price;

                    Object[] rowData = {time, ticker, segment, type, qty, price, total};
                    tableModel.addRow(rowData);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load history: " + e.getMessage());
        }
    }
}