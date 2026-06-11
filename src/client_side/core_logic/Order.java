package client_side.core_logic;

public abstract class Order {
    protected User user;
    protected Stock stock;
    protected int quantity;

    public Order(User user, Stock stock, int quantity) {
        this.user = user;
        this.stock = stock;
        this.quantity = quantity;
    }

    // Abstract method: Har subclass ko ise apne tareeqe se implement karna padega
    public abstract boolean processTrade(); 
}