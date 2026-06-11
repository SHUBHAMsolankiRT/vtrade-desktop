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
![Account Creation](screenshots/<img width="487" height="517" alt="signup" src="https://github.com/user-attachments/assets/18537e02-dc4f-4b4b-a680-c9700a72e74e" />
)
![Trader Id](screenshots/signup2.png)
![Secure Login](screenshots/login.png)

### 2. Main Trading Dashboard & Live Chart
![Dashboard](screenshots/dashboard.png)
![Live Trading](screenshots/chart.png)

### 3. Buy & Sell
![Buy](screenshots/buy.png)
![Sell](screenshots/sell.png)

### 3. Smart Ledger & Order History
![Ledger](screenshots/ledger.png)

### 4. Generated Contract Note
![Receipt](screenshots/receipt.png)
---
Developed by Shubham Solanki
