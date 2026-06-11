package client_side.core_logic;

public class User {
    private final int userId;
    private final String fullName;
    private final int pin;
    private double virtualCash;

    // Constructor
    public User(int userId, String fullName, int pin, double virtualCash) {
        this.userId = userId;
        this.fullName = fullName;
        this.pin = pin;
        this.virtualCash = virtualCash;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public int getPin() { return pin; }
    public double getVirtualCash() { return virtualCash; }

    // Method to update balance after trade
    public void updateBalance(double amount) {
        this.virtualCash += amount;
    }
}