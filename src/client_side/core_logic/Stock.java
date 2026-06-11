package client_side.core_logic;

public class Stock {
    private final String ticker;
    private final String companyName;
    private double livePrice;

    public Stock(String ticker, String companyName, double livePrice) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.livePrice = livePrice;
    }

    public String getTicker() { return ticker; }
    public String getCompanyName() { return companyName; }
    public double getLivePrice() { return livePrice; }

    // Thread isko use karega price change karne ke liye
    public void setLivePrice(double newPrice) {
        this.livePrice = newPrice;
    }
}