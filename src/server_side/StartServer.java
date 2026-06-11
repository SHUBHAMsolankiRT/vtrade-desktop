package server_side;

import java.net.MalformedURLException;

import client_side.core_logic.Stock;
// import client_side.networking.RemoteMarketInterface;

import java.util.ArrayList;
import java.util.List;
import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class StartServer {
    // ... baaki poora code same rahega
    public static void main(String[] args) {
        try {
            // 1. Setup Stocks
            // 1. Setup Stocks & Indices (Naya Code)
            List<Stock> myStocks = new ArrayList<>();
            // Indices (Simulated as stocks for easy engine updates)
            myStocks.add(new Stock("NIFTY 50", "Market Index", 22500.00));
            myStocks.add(new Stock("BANK NIFTY", "Bank Index", 48200.00));
            
            // Top Companies
            myStocks.add(new Stock("RELIANCE", "Reliance Ind.", 2850.50));
            myStocks.add(new Stock("TCS", "Tata Consultancy", 3920.00));
            myStocks.add(new Stock("HDFCBANK", "HDFC Bank", 1450.25));
            myStocks.add(new Stock("INFY", "Infosys", 1480.00));
            myStocks.add(new Stock("ITC", "ITC Ltd", 420.50));
            myStocks.add(new Stock("SBI", "State Bank of India", 760.00));
            myStocks.add(new Stock("ZOMATO", "Zomato Ltd", 162.20));
            myStocks.add(new Stock("TATASTEEL", "Tata Steel", 155.80));

            // 2. Start Live Engine (Thread)
            LivePriceEngine engine = new LivePriceEngine(myStocks);
            Thread engineThread = new Thread(engine);
            engineThread.start(); 
            System.out.println("✅ Background Price Engine Started!");

            // 3. START RMI SERVER (Naya Code)
            // Port 1099 RMI ka default port hota hai
            LocateRegistry.createRegistry(1099); 
            
            // Server Implementation ka object bnao
            MarketServerImpl rmiServer = new MarketServerImpl(myStocks);
            
            // Server ko ek naam (VTradeServer) dekar registry mein bind kar do
            Naming.rebind("rmi://localhost:1099/VTradeServer", rmiServer);
            
            System.out.println("✅ RMI Server is running on port 1099...");
            System.out.println("✅ Waiting for Clients to connect...");

        } catch (MalformedURLException | RemoteException e) {
            System.out.println("❌ Server Start Error: " + e.getMessage());
        }
    }
}