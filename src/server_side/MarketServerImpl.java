package server_side;

import client_side.core_logic.Stock;
import client_side.networking.RemoteMarketInterface;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;

public class MarketServerImpl extends UnicastRemoteObject implements RemoteMarketInterface {
    
    // Server ke paas market stocks ki list honi chahiye
    private List<Stock> marketStocks;

    // Constructor
    public MarketServerImpl(List<Stock> marketStocks) throws RemoteException {
        super(); // UnicastRemoteObject ko initialize karne ke liye
        this.marketStocks = marketStocks;
    }

    // Ye wo method hai jo Client ke mangne par execute hoga
    @Override
    public String getLiveMarketData() throws RemoteException {
        StringBuilder data = new StringBuilder();
        data.append("--- LIVE MARKET DATA ---\n");
        
        for (Stock stock : marketStocks) {
            // Hum list banakar client ko bhej rahe hain
            data.append(stock.getTicker())
                .append(" : Rs. ")
                .append(stock.getLivePrice())
                .append("\n");
        }
        return data.toString();
    }
}