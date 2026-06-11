package client_side.ui_screens;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Unit 4: Custom Graphics Drawing
public class PriceChartPanel extends JPanel {
    private final List<Double> priceHistory;
    private static final int MAX_DATA_POINTS = 30; // Screen par kitne point dikhenge

    public PriceChartPanel() {
        priceHistory = new ArrayList<>();
        setBackground(new Color(20, 20, 20)); // Dark background
    }

    public void clearChart() {
        priceHistory.clear();
        repaint();
    }

    // Naya price aane par ye method call hoga
    public void addPricePoint(double price) {
        if (priceHistory.size() >= MAX_DATA_POINTS) {
            priceHistory.remove(0); // Purana point hata do
        }
        priceHistory.add(price);
        repaint(); // Screen ko fir se draw karo
    }

    // Ye magic method hai jo line draw karta hai
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth lines

        if (priceHistory.size() < 2) return; // Kam se kam 2 point chahiye line banane ke liye

        double maxPrice = Collections.max(priceHistory);
        double minPrice = Collections.min(priceHistory);
        
        // Agar price change nahi hua toh graph center mein rakhne ke liye padding
        if (maxPrice == minPrice) { maxPrice += 10; minPrice -= 10; }

        int width = getWidth();
        int height = getHeight();
        int padding = 40;

        // Draw Grid Lines (Peeche ka jaal)
        g2d.setColor(new Color(50, 50, 50));
        for (int i = 0; i <= 5; i++) {
            int y = padding + i * (height - 2 * padding) / 5;
            g2d.drawLine(padding, y, width - padding, y);
        }

        // Draw Line Graph
        g2d.setColor(new Color(0, 255, 65)); // Hacker Green color
        g2d.setStroke(new BasicStroke(3f)); // Line ki motai

        for (int i = 0; i < priceHistory.size() - 1; i++) {
            int x1 = padding + i * (width - 2 * padding) / (MAX_DATA_POINTS - 1);
            int y1 = (int) (height - padding - ((priceHistory.get(i) - minPrice) / (maxPrice - minPrice)) * (height - 2 * padding));
            int x2 = padding + (i + 1) * (width - 2 * padding) / (MAX_DATA_POINTS - 1);
            int y2 = (int) (height - padding - ((priceHistory.get(i + 1) - minPrice) / (maxPrice - minPrice)) * (height - 2 * padding));

            g2d.drawLine(x1, y1, x2, y2);
            // Points par chote circle draw karo
            g2d.fillOval(x1 - 3, y1 - 3, 6, 6); 
        }
        
        // Current Price Label screen par likhna
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2d.drawString("Live Chart Engine Running...", padding, 20);
    }
}