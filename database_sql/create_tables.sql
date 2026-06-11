-- 1. Create and open the database
CREATE DATABASE vtrade_db;
USE vtrade_db;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    pin INT NOT NULL,  -- Login karne ke liye 4-digit PIN
    virtual_cash DOUBLE DEFAULT 100000.00  -- Signup par default 1 Lakh rupees milenge
);


CREATE TABLE market_stocks (
    ticker VARCHAR(20) PRIMARY KEY,  -- Short name jaise 'RELIANCE', 'TCS'
    company_name VARCHAR(100) NOT NULL,
    live_price DOUBLE NOT NULL
);


CREATE TABLE user_portfolio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    ticker VARCHAR(20),
    quantity INT,
    avg_buy_price DOUBLE, -- Jis average price par user ne kharida
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (ticker) REFERENCES market_stocks(ticker)
);


CREATE TABLE trade_transactions (
    trade_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    ticker VARCHAR(20),
    trade_type VARCHAR(10),  -- Yahan hum 'BUY' ya 'SELL' likhenge
    qty INT,
    price_at_trade DOUBLE,   -- Kis daam par trade hua
    trade_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- System ka time apne aap aa jayega
);



-- Insert dummy stocks into the market
INSERT INTO market_stocks (ticker, company_name, live_price) VALUES
('RELIANCE', 'Reliance Industries', 2850.50),
('TCS', 'Tata Consultancy Services', 3920.00),
('HDFC', 'HDFC Bank', 1450.75),
('ZOMATO', 'Zomato Ltd', 162.20),
('TATAMOTORS', 'Tata Motors', 980.10);


INSERT INTO users (full_name, pin, virtual_cash) VALUES ('Shubham Solanki', 4321, 100000.00);