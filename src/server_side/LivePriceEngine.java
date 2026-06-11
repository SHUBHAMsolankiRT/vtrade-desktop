package server_side;
import client_side.core_logic.Stock;
import java.util.List;
import java.util.Random;

public class LivePriceEngine implements Runnable {
    private final List<Stock> marketStocks;
    private boolean isMarketOpen;

    // Constructor
    public LivePriceEngine(List<Stock> marketStocks) {
        this.marketStocks = marketStocks;
        this.isMarketOpen = true; 
    }

    // Yeh wo method hai jo Thread ke start hone par chalega
    @Override
    public void run() {
        Random random = new Random();
        System.out.println("🔔 Market is OPEN! Live Price Engine Started...");

        while (isMarketOpen) {
            try {
                // Thread ko 3 second ke liye sula do (Sleep)
                Thread.sleep(3000); 

                System.out.println("\n--- Live Market Update ---");
                for (Stock stock : marketStocks) {
                    double currentPrice = stock.getLivePrice();

                    // Price ko -1.5% se +1.5% tak randomly badlo
                    double percentageChange = (random.nextDouble() * 3.0) - 1.5; 
                    double priceChange = currentPrice * (percentageChange / 100);
                    
                    double newPrice = currentPrice + priceChange;
                    // Decimal ke baad sirf 2 digit rakhne ki ninja technique
                    newPrice = Math.round(newPrice * 100.0) / 100.0;

                    // Stock ka naya price set karo
                    stock.setLivePrice(newPrice);

                    // Console par print karo taaki hume changes dikhein
                    String trend = (priceChange >= 0) ? "📈 UP" : "📉 DOWN";
                    System.out.println(stock.getTicker() + " : ₹" + newPrice + " " + trend);
                }
            } catch (InterruptedException e) {
                System.out.println("Market Engine Stopped!");
                isMarketOpen = false;
            }
        }
    }

    // Market band karne ka switch
    public void closeMarket() {
        this.isMarketOpen = false;
    }
}