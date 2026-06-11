package client_side.ui_screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;

import client_side.database.DBConnection;

public class VTradeProDesktop extends JFrame {
    String userName;
    
    // UI Colors
    Color bgColor = new Color(18, 18, 18);
    Color panelColor = new Color(30, 30, 30);
    Color textColor = new Color(220, 220, 220);
    Color buyColor = new Color(0, 184, 82);
    Color sellColor = new Color(255, 66, 78);
    Color activeTabColor = new Color(70, 130, 180); 

    // Dynamic Components
    PriceChartPanel chartPanel;
    JLabel currentPriceLabel;
    JLabel balanceLabel;
    JTextField searchBar; 
    String selectedTicker = "NIFTY 50"; 
    JTextField qtyField;
    JLabel watchTitle;
    
    Timer marketTimer; 
    double currentLivePrice = 24050.60; 

    // DATA ARRAYS
    String[] equityList = {"NIFTY 50", "SENSEX", "RELIANCE", "TCS", "HDFCBANK", "INFY", "ITC", "SBI", "ICICIBANK", "L&T"};
    String[] fnoList = {"NIFTY 24500 CE", "NIFTY 24000 PE", "BANKNIFTY 48000 CE", "BANKNIFTY 47500 PE", "RELIANCE 3000 CE", "TCS 4000 PE"};

    public VTradeProDesktop(String userName) {
        this.userName = userName;

        setTitle("V-Trade Pro Max - Dual Mode Edition");
        setSize(1250, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBackground(bgColor);
        mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. TOP NAVBAR
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(panelColor);
        navbar.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftNav.setOpaque(false);
        
        JLabel logoLabel = new JLabel("V-TRADE");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logoLabel.setForeground(buyColor);
        leftNav.add(logoLabel);

        searchBar = new JTextField(20);
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchBar.setBackground(bgColor);
        searchBar.setForeground(Color.WHITE);
        searchBar.setCaretColor(Color.WHITE);
        searchBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(60,60,60)), "Search Stock / Option", 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.GRAY));
        
        JButton searchBtn = new JButton("🔍");
        searchBtn.setBackground(new Color(50, 50, 50));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchBtn.addActionListener(e -> {
            String query = searchBar.getText().toUpperCase().trim();
            if(!query.isEmpty()) {
                selectedTicker = query;
                currentPriceLabel.setText("🌐 Fetching data for " + selectedTicker + "...");
                currentPriceLabel.setForeground(Color.ORANGE);
                if(chartPanel != null) chartPanel.clearChart();
                simulateInternetFetch(selectedTicker);
            }
        });

        leftNav.add(searchBar);
        leftNav.add(searchBtn);
        navbar.add(leftNav, BorderLayout.WEST);
        
        balanceLabel = new JLabel("Virtual Cash: ₹ Loading...   |   " + userName);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceLabel.setForeground(textColor);
        fetchAndUpdateBalance();

        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightNav.setOpaque(false); 
        rightNav.add(balanceLabel);

        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setBackground(new Color(220, 20, 60)); 
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            if (marketTimer != null) marketTimer.stop(); 
            dispose();
            SwingUtilities.invokeLater(() -> {
                new ModernLoginScreen().setVisible(true); 
            });
        });
        rightNav.add(logoutBtn);

        navbar.add(rightNav, BorderLayout.EAST);
        mainContainer.add(navbar, BorderLayout.NORTH);

        // ---------------------------------------------------------
        // 2. LEFT PANEL (DUAL MODE SWAP LOGIC)
        // ---------------------------------------------------------
        JPanel watchlistPanel = new JPanel(new BorderLayout());
        watchlistPanel.setPreferredSize(new Dimension(280, 0));
        watchlistPanel.setBackground(panelColor);
        
        JPanel togglePanel = new JPanel(new GridLayout(1, 2));
        JButton btnStocks = new JButton("📈 Stocks");
        JButton btnTrade = new JButton("⚡ F&O Trading");
        
        btnStocks.setBackground(activeTabColor); 
        btnStocks.setForeground(Color.WHITE);
        btnStocks.setFocusPainted(false);
        
        btnTrade.setBackground(new Color(50, 50, 50)); 
        btnTrade.setForeground(Color.WHITE);
        btnTrade.setFocusPainted(false);

        // --- YEHI WO 2 LINES HAIN JO MAIN BHOOL GAYA THA ---
        togglePanel.add(btnStocks);
        togglePanel.add(btnTrade);
        // ----------------------------------------------------

        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setBackground(panelColor);
        headerContainer.add(togglePanel, BorderLayout.NORTH);

        watchTitle = new JLabel("  🔥 Top Market Movers", SwingConstants.LEFT);
        watchTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        watchTitle.setForeground(textColor);
        watchTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        headerContainer.add(watchTitle, BorderLayout.SOUTH);

        watchlistPanel.add(headerContainer, BorderLayout.NORTH);

        JList<String> watchList = new JList<>(equityList); 
        watchList.setBackground(panelColor);
        watchList.setForeground(textColor);
        watchList.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        watchList.setFixedCellHeight(45); 
        watchList.setSelectionBackground(new Color(60, 60, 60));
        
        btnStocks.addActionListener(e -> {
            btnStocks.setBackground(activeTabColor);
            btnTrade.setBackground(new Color(50, 50, 50));
            watchList.setListData(equityList); 
            watchTitle.setText("  🔥 Top Market Movers");
        });

        btnTrade.addActionListener(e -> {
            btnTrade.setBackground(activeTabColor);
            btnStocks.setBackground(new Color(50, 50, 50));
            watchList.setListData(fnoList); 
            watchTitle.setText("  ⚡ Live Options Chain");
        });
        
        watchList.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting() && watchList.getSelectedValue() != null) {
                selectedTicker = watchList.getSelectedValue();
                searchBar.setText(selectedTicker); 
                currentPriceLabel.setText("🌐 Processing Data for " + selectedTicker + "...");
                currentPriceLabel.setForeground(Color.ORANGE);
                if(chartPanel != null) chartPanel.clearChart();
                simulateInternetFetch(selectedTicker);
            }
        });

        watchlistPanel.add(new JScrollPane(watchList), BorderLayout.CENTER);
        mainContainer.add(watchlistPanel, BorderLayout.WEST);

        // 3. CENTER PANEL
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(panelColor);
        chartContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        currentPriceLabel = new JLabel("NIFTY 50 | Live Price: ₹ 24050.60", SwingConstants.CENTER);
        currentPriceLabel.setFont(new Font("Consolas", Font.BOLD, 24));
        currentPriceLabel.setForeground(buyColor);
        chartContainer.add(currentPriceLabel, BorderLayout.NORTH);
        
        chartPanel = new PriceChartPanel(); 
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        
        mainContainer.add(chartContainer, BorderLayout.CENTER);

        // 4. RIGHT PANEL
        JPanel orderPanel = new JPanel(new GridLayout(6, 1, 10, 15));
        orderPanel.setPreferredSize(new Dimension(250, 0));
        orderPanel.setBackground(panelColor);
        orderPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel orderTitle = new JLabel("Place Order", SwingConstants.CENTER);
        orderTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        orderTitle.setForeground(textColor);
        orderPanel.add(orderTitle);

        qtyField = new JTextField("10"); 
        qtyField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        qtyField.setBackground(bgColor);
        qtyField.setForeground(Color.WHITE);
        qtyField.setHorizontalAlignment(JTextField.CENTER);
        orderPanel.add(qtyField);

        JButton buyBtn = new JButton("BUY");
        buyBtn.setBackground(buyColor);
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        buyBtn.setFocusPainted(false);
        buyBtn.addActionListener(e -> executeTrade("BUY")); 
        orderPanel.add(buyBtn);

        JButton sellBtn = new JButton("SELL");
        sellBtn.setBackground(sellColor);
        sellBtn.setForeground(Color.WHITE);
        sellBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sellBtn.setFocusPainted(false);
        sellBtn.addActionListener(e -> executeTrade("SELL")); 
        orderPanel.add(sellBtn);

        orderPanel.add(new JLabel("")); 

        JButton historyBtn = new JButton("📜 View Ledger");
        historyBtn.setBackground(new Color(70, 130, 180)); 
        historyBtn.setForeground(Color.WHITE);
        historyBtn.setFocusPainted(false);
        historyBtn.addActionListener(e -> new LedgerScreen(userName).setVisible(true));
        orderPanel.add(historyBtn);

        mainContainer.add(orderPanel, BorderLayout.EAST);
        add(mainContainer);
        setVisible(true);

        startEngine();
    }

    private void simulateInternetFetch(String ticker) {
        Random rand = new Random();
        if(ticker.contains("CE") || ticker.contains("PE")) {
            currentLivePrice = 50 + rand.nextInt(250); 
        } 
        else if(ticker.contains("NIFTY")) currentLivePrice = 24000 + rand.nextInt(500);
        else if(ticker.contains("RELIANCE")) currentLivePrice = 2900 + rand.nextInt(100);
        else if(ticker.contains("TCS")) currentLivePrice = 3900 + rand.nextInt(150);
        else if(ticker.contains("HDFC")) currentLivePrice = 1450 + rand.nextInt(50);
        else if(ticker.contains("INFY")) currentLivePrice = 1290 + rand.nextInt(30); 
        else currentLivePrice = 100 + rand.nextInt(2000); 
    }

    private void startEngine() {
        Random fluctuation = new Random();
        
        marketTimer = new Timer(1500, e -> {
            LocalTime now = LocalTime.now();
            LocalTime marketOpen = LocalTime.of(9, 15);
            LocalTime marketClose = LocalTime.of(15, 30);

            if (now.isBefore(marketOpen) || now.isAfter(marketClose)) {
                currentPriceLabel.setText("🛑 MARKET CLOSED (Opens at 09:15 AM)");
                currentPriceLabel.setForeground(Color.GRAY);
                return; 
            }

            double change = (fluctuation.nextDouble() * 5) - 2.5; 
            currentLivePrice += change;
            String formattedPrice = String.format("%.2f", currentLivePrice);
            
            currentPriceLabel.setText(selectedTicker + " | Live Price: ₹ " + formattedPrice);
            if(change >= 0) currentPriceLabel.setForeground(buyColor); 
            else currentPriceLabel.setForeground(sellColor); 
            
            chartPanel.addPricePoint(currentLivePrice); 
        });
        
        marketTimer.start();
    }

    private void executeTrade(String tradeType) {
        String qtyStr = qtyField.getText();
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();

            double stockPrice = currentLivePrice; 
            double totalCost = stockPrice * qty;

            Connection conn = DBConnection.getConnection();
            if (conn == null) return;

            PreparedStatement pstUser = conn.prepareStatement("SELECT user_id, virtual_cash FROM users WHERE full_name = ?");
            pstUser.setString(1, userName);
            ResultSet rsUser = pstUser.executeQuery();
            
            if (rsUser.next()) {
                int userId = rsUser.getInt("user_id");
                double balance = rsUser.getDouble("virtual_cash");

                if (tradeType.equals("BUY")) {
                    if (balance >= totalCost) {
                        PreparedStatement updateBal = conn.prepareStatement("UPDATE users SET virtual_cash = virtual_cash - ? WHERE user_id = ?");
                        updateBal.setDouble(1, totalCost);
                        updateBal.setInt(2, userId);
                        updateBal.executeUpdate();

                        PreparedStatement insertHistory = conn.prepareStatement("INSERT INTO trade_transactions (user_id, ticker, trade_type, qty, price_at_trade) VALUES (?, ?, 'BUY', ?, ?)");
                        insertHistory.setInt(1, userId);
                        insertHistory.setString(2, selectedTicker);
                        insertHistory.setInt(3, qty);
                        insertHistory.setDouble(4, stockPrice);
                        insertHistory.executeUpdate();

                        JOptionPane.showMessageDialog(this, "✅ Bought " + qty + " shares of " + selectedTicker + "\nTotal: ₹ " + String.format("%.2f", totalCost));
                        fetchAndUpdateBalance(); 
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Insufficient Funds!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (tradeType.equals("SELL")) {
                    PreparedStatement updateBal = conn.prepareStatement("UPDATE users SET virtual_cash = virtual_cash + ? WHERE user_id = ?");
                    updateBal.setDouble(1, totalCost);
                    updateBal.setInt(2, userId);
                    updateBal.executeUpdate();

                    PreparedStatement insertHistory = conn.prepareStatement("INSERT INTO trade_transactions (user_id, ticker, trade_type, qty, price_at_trade) VALUES (?, ?, 'SELL', ?, ?)");
                    insertHistory.setInt(1, userId);
                    insertHistory.setString(2, selectedTicker);
                    insertHistory.setInt(3, qty);
                    insertHistory.setDouble(4, stockPrice);
                    insertHistory.executeUpdate();

                    JOptionPane.showMessageDialog(this, "✅ Sold " + qty + " shares of " + selectedTicker + "\nReceived: ₹ " + String.format("%.2f", totalCost));
                    fetchAndUpdateBalance(); 
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity sirf numbers hone chahiye!", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (HeadlessException | SQLException ex) {
            System.out.println("Database error.");
        }
    }

    private void fetchAndUpdateBalance() {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                PreparedStatement pst = conn.prepareStatement("SELECT virtual_cash FROM users WHERE full_name = ?");
                pst.setString(1, userName);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    double balance = rs.getDouble("virtual_cash");
                    balanceLabel.setText(String.format("Virtual Cash: ₹ %.2f   |   %s", balance, userName));
                }
            }
        } catch (SQLException e) {}
    }
}