package client_side.networking; // Ye add karo
import java.rmi.Remote;
import java.rmi.RemoteException;

// RMI rules ke hisaab se interface ko 'Remote' extend karna zaroori hai
public interface RemoteMarketInterface extends Remote {
    
    // Client ye method call karega server se prices mangne ke liye
    // throws RemoteException lagana mandatory hai RMI mein
    String getLiveMarketData() throws RemoteException;
    
}