package client_side.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReceiptGenerator {

    // Ab isme segment, type aur time bhi pass ho raha hai Ledger screen se
    public static boolean generateReceipt(String userName, String ticker, String segment, String type, int qty, double price, double totalCost, String time) {
        
        // File ka naam unique banane ke liye time use kar rahe hain
        String fileName = "user_receipts/ContractNote_" + ticker.replace(" ", "") + "_" + System.currentTimeMillis() + ".txt";

        try {
            // Folder check karo, agar nahi hai toh bana do
            File directory = new File("user_receipts");
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Receipt ka format (Professional Broker Bill design)
            try (FileWriter writer = new FileWriter(fileName)) {
                // Receipt ka format (Professional Broker Bill design)
                writer.write("==================================================\n");
                writer.write("           V-TRADE : OFFICIAL CONTRACT NOTE       \n");
                writer.write("==================================================\n\n");
                writer.write("Trader Name    : " + userName + "\n");
                writer.write("Date & Time    : " + time + "\n");
                writer.write("Market Segment : " + segment + "\n");
                writer.write("Action Type    : " + type + " ORDER\n");
                writer.write("--------------------------------------------------\n");
                writer.write("Instrument     : " + ticker + "\n");
                writer.write("Quantity       : " + qty + "\n");
                writer.write("Execution Price: Rs. " + String.format("%.2f", price) + "\n");
                writer.write("--------------------------------------------------\n");
                
                // Agar kharida hai toh debit, becha hai toh credit
                if(type.equals("BUY")) {
                    writer.write("TOTAL DEBIT    : Rs. " + String.format("%.2f", totalCost) + "\n\n");
                } else {
                    writer.write("TOTAL CREDIT   : Rs. " + String.format("%.2f", totalCost) + "\n\n");
                }
                
                writer.write("==================================================\n");
                writer.write("      Thank you for trading with V-Trade Pro!     \n");
                writer.write("      *Generated automatically by System* \n");
                writer.write("==================================================\n");
            }
            System.out.println("📄 Receipt Generated Successfully: " + fileName);
            return true;

        } catch (IOException e) {
            System.out.println("❌ Receipt Generation Failed!");
            return false;
        }
    }
}