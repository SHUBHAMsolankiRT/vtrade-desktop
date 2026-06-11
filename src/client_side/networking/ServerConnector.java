package client_side.networking;

import java.rmi.Naming;

public class ServerConnector {
    
    // Yeh method client app ko Server ka object return karega
    public static RemoteMarketInterface getMarketServer() {
        try {
            // RMI registry mein us port (1099) par jakar engine ko dhundo
            return (RemoteMarketInterface) Naming.lookup("rmi://localhost:1099/VTradeServer");
        } catch (Exception e) {
            System.out.println("❌ Server Connection Failed! Pehle StartServer ko run karo.");
            return null;
        }
    }
}