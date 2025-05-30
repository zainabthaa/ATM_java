🏦 Java Banking System

A simple Java-based banking system with a Swing GUI for admin and user management. The admin panel supports creating users, resetting PINs, unlocking accounts, generating reports, and viewing all users in a neat table — all with file-based data storage.
📌 Features

🔹 Admin Panel
Create new users with 10-digit account numbers
Reset user PINs easily
Unlock locked user accounts
Generate reports (extendable)
View all users in a tabular GUI
🔹 User Management
Validate account numbers (must be exactly 10 digits)
Persistent user data stored in users.txt and locked accounts in locked_accounts.txt
Smooth GUI interactions with Swing
🖼️ GUI Highlights

Built with Java Swing
Centered buttons and forms for better UX
User info displayed in JTable for clarity
Multiple dialogs for admin operations
🗃️ Data Storage

users.txt: Stores user details in text format
locked_accounts.txt: Keeps track of locked accounts
Simple text-based storage, easy to extend or migrate to a database
⚙️ Installation & Running

🔧 Requirements
JDK 8 or higher
Any Java IDE (Eclipse, IntelliJ, NetBeans) or command line
🏃 How to run
Compile the Java files:
javac *.java
Run the main application class (e.g., BankingSystem):
java BankingSystem
🧑‍💻 Author

Zainab Ali Taha
Effat University
Java GUI Banking System Project
