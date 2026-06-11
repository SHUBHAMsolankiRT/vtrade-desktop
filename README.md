# V-Trade Pro Desktop Application

This is a dual-mode virtual stock trading desktop application that I developed using Java and Swing. It simulates real-world market mechanics and provides a paper-trading experience for both Equity and Futures & Options (F&O) segments.

## The Live Price Engine Logic
Initially, I integrated a third-party live market API to fetch real-time stock and options prices. However, free APIs have strict rate limits and get exhausted very quickly during testing and trading sessions. To solve this dependency, I designed a custom asynchronous Live Price Simulator Engine. It runs in the background using Java Timers, generating realistic market fluctuations while strictly respecting Indian stock market timings (09:15 AM to 03:30 PM).

## Core Features
Dual-Mode Interface: Switch smoothly between Equity (RELIANCE, TCS) and F&O Options Chain.
Automated Contract Notes: The system generates actual downloadable trade receipts and saves them locally upon trade execution on user_receipts.
Smart Ledger: Interactive order history tracking with segment classification and real-time P&L updates.
Secure Authentication: Isolated user portfolios and virtual cash management using MySQL.

## 🛠️ Tech Stack

Frontend: Java Swing, AWT (Dark Mode UI)
Backend: Java (JDK 21)
Database: MySQL (JDBC)
Build Tool: Maven

## How to Run This Project
1. Database Setup: Start your local MySQL server (using XAMPP or MySQL Workbench). Import the database schema from the project folder.
2. Configure Credentials: Open the file located at src/client_side/database/DBConnection.java and update the database username and password to match your local setup.
3. Execution: Navigate to src/client_side/ui_screens/ModernLoginScreen.java and run this file. This is the main entry point of the application.

## Screenshots
1. Secure Login & Demat Account Creation
![Account Creation]<img width="487" height="517" alt="signup" src="https://github.com/user-attachments/assets/18537e02-dc4f-4b4b-a680-c9700a72e74e" />
)
![Trader Id] <img width="517" height="497" alt="signup2" src="https://github.com/user-attachments/assets/61f9b39f-1a1a-447c-8acd-c7f902fec9b3" />

![Secure Login] <img width="497" height="423" alt="login" src="https://github.com/user-attachments/assets/23425baa-2269-4990-80e0-227bfabd6c37" />


### 2. Main Trading Dashboard & Live Chart
![Dashboard] <img width="1541" height="978" alt="dashboard" src="https://github.com/user-attachments/assets/6e157ea2-02c3-4433-9d02-5a0c1bbd846e" />

![Live Trading] <img width="1497" height="962" alt="chart" src="https://github.com/user-attachments/assets/25159540-d4f8-4550-85de-e26c559c4b97" />


### 3. Buy & Sell
![Buy] <img width="1487" height="960" alt="buy" src="https://github.com/user-attachments/assets/e70b2a7f-5f2e-4ee6-b6aa-d453fb5a141e" />

![Sell] <img width="1478" height="955" alt="sell" src="https://github.com/user-attachments/assets/b33b3224-fc8a-499f-adc5-5709be6f1a98" />


### 3. Smart Ledger & Order History
![Ledger] <img width="1082" height="605" alt="ledger" src="https://github.com/user-attachments/assets/5ceb054f-d405-47ad-910a-91d4bdc4dc00" />


### 4. Generated Contract Note
![Receipt] <img width="737" height="751" alt="receipt" src="https://github.com/user-attachments/assets/a1c714b5-d833-47b9-8a9a-0f507fd3a743" />

---
Developed by Shubham Solanki
