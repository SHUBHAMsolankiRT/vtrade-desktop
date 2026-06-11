package client_side.core_logic;

public class SellOrder extends Order {

    public SellOrder(User user, Stock stock, int quantity) {
        super(user, stock, quantity);
    }

    @Override
    public boolean processTrade() {
        double totalRevenue = stock.getLivePrice() * quantity;
        
        // Asli code mein yahan pehle check karenge ki user ke paas ye share hai bhi ya nahi
        user.updateBalance(totalRevenue); // Balance badhao
        System.out.println("SELL Successful: " + quantity + " shares of " + stock.getTicker());
        return true;
    }
}