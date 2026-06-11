package client_side.core_logic;

public class BuyOrder extends Order {

    public BuyOrder(User user, Stock stock, int quantity) {
        super(user, stock, quantity); // Super keyword ka use (Unit 1)
    }

    @Override
    public boolean processTrade() {
        double totalCost = stock.getLivePrice() * quantity;
        
        if (user.getVirtualCash() >= totalCost) {
            user.updateBalance(-totalCost); // Balance kato
            System.out.println("BUY Successful: " + quantity + " shares of " + stock.getTicker());
            return true;
        } else {
            System.out.println("Failed: Paise kam hain bhai!");
            return false;
        }
    }
}