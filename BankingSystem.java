import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BankingSystem implements ActionListener { //! Leen and Zainab
    private JTextField userText;
    private JPasswordField passwordText;
    private JLabel success;
    private Account loggedInAccount;

    public static void main(String[] args) {
        new BankingSystem();
    }

    public BankingSystem() {
        showInitialSelection();
    }

    // select if admin or user:
    private void showInitialSelection() {
        JFrame selectionFrame = new JFrame("Banking System - Select User Type");
        selectionFrame.setSize(300, 200);
        selectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectionFrame.setLayout(null);
        selectionFrame.setLocationRelativeTo(null);

        JButton adminButton = new JButton("Admin Login");
        adminButton.setBounds(50, 50, 200, 30);
        adminButton.addActionListener(e -> {
            selectionFrame.dispose();
            openAdminLogin();
        });

        JButton userButton = new JButton("User Login");
        userButton.setBounds(50, 100, 200, 30);
        userButton.addActionListener(e -> {
            selectionFrame.dispose();
            openUserLogin();
        });

        selectionFrame.add(adminButton);
        selectionFrame.add(userButton);

        selectionFrame.setVisible(true);
    }

    // screen of admin login
    private void openAdminLogin() {
        JFrame adminFrame = new JFrame("Admin Login");
        adminFrame.setSize(400, 300);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel pinLabel = new JLabel("Admin PIN:");
        pinLabel.setBounds(50, 80, 120, 25);
        panel.add(pinLabel);

        JPasswordField pinField = new JPasswordField();
        pinField.setBounds(180, 80, 165, 25);
        panel.add(pinField);

        JButton loginButton = new JButton("Log in");
        loginButton.setBounds(150, 120, 100, 25);
        loginButton.addActionListener(e -> {
            String enteredPin = new String(pinField.getPassword());
            if (enteredPin.equals("CS2132")) { // hardcoded admin PIN
                adminFrame.dispose();
                openAdminPanel();
            } else {
                JOptionPane.showMessageDialog(adminFrame, "Invalid PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loginButton);
        adminFrame.add(panel);
        adminFrame.setVisible(true);
    }

    // options for admin panel
    private void openAdminPanel() {
        JFrame adminPanel = new JFrame("Admin Panel");
        adminPanel.setSize(400, 320);
        adminPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminPanel.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.setBounds(100, 40, 200, 30);
        generateReportButton.addActionListener(e -> generateReport());

        JButton resetPinButton = new JButton("Reset PIN");
        resetPinButton.setBounds(100, 80, 200, 30);
        resetPinButton.addActionListener(e -> resetPIN());

        JButton unlockAccountButton = new JButton("Unlock Account");
        unlockAccountButton.setBounds(100, 120, 200, 30);
        unlockAccountButton.addActionListener(e -> unlockAccount());

        JButton createUserButton = new JButton("Create User");
        createUserButton.setBounds(100, 160, 200, 30);
        createUserButton.addActionListener(e -> openCreateUserWindow(adminPanel));

        JButton viewUsersButton = new JButton("View All Users");
        viewUsersButton.setBounds(100, 200, 200, 30);
        viewUsersButton.addActionListener(e -> showAllUsersWindow());

        JButton backButton = new JButton("Back");
        backButton.setBounds(150, 240, 100, 25);
        backButton.addActionListener(e -> {
            adminPanel.dispose();
            showInitialSelection();
        });

        panel.add(generateReportButton);
        panel.add(resetPinButton);
        panel.add(unlockAccountButton);
        panel.add(createUserButton);
        panel.add(viewUsersButton);
        panel.add(backButton);

        adminPanel.add(panel);
        adminPanel.setVisible(true);
    }

    // Add this method to show all users in a new window
    private void showAllUsersWindow() {
        String filePath = "users.txt";
        List<String[]> userData = new ArrayList<>();
        String[] columns = {"Account Number", "PIN", "Balance", "Name"};

        // Read users file and parse data into rows
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Each line format: AccountNumber:1234567890, PIN:1234, Balance:1000.0, Name:John Doe
                String[] parts = line.split(", ");
                String[] row = new String[4];
                for (int i = 0; i < parts.length; i++) {
                    String[] keyValue = parts[i].split(":");
                    if (keyValue.length == 2) {
                        row[i] = keyValue[1].trim();
                    } else {
                        row[i] = "";
                    }
                }
                userData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading users file.");
            return;
        }

        // Convert List<String[]> to Object[][]
        Object[][] data = new Object[userData.size()][4];
        for (int i = 0; i < userData.size(); i++) {
            data[i] = userData.get(i);
        }

        // Create JTable with data and column names
        JTable table = new JTable(data, columns);
        table.setEnabled(false); // Make table non-editable

        // Setup JFrame
        JFrame usersFrame = new JFrame("All Users");
        usersFrame.setSize(600, 400);
        usersFrame.setLocationRelativeTo(null);
        usersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add JTable to JScrollPane for scrolling
        JScrollPane scrollPane = new JScrollPane(table);
        usersFrame.add(scrollPane);

        usersFrame.setVisible(true);
    }


    private void generateReport() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Transactions.txt"))) {
            int deposits = 0, withdrawals = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Type:Deposit")) deposits++;
                if (line.contains("Type:Withdrawal")) withdrawals++;
            }
            JOptionPane.showMessageDialog(null, "Deposits: " + deposits + "\nWithdrawals: " + withdrawals,
                    "Report", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading transactions: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPIN() {
        String accountNumber = JOptionPane.showInputDialog("Enter Account Number:");
        String newPIN = JOptionPane.showInputDialog("Enter New PIN:");
        // Validate input
        if (newPIN == null || newPIN.length() != 4 || !newPIN.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "PIN must be exactly 4 digits and numeric.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (accountNumber != null && newPIN != null) {
            try {
                StringBuilder updatedContent = new StringBuilder();
                boolean isUpdated = false;

                try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("AccountNumber:" + accountNumber)) {
                            String[] parts = line.split(", ");
                            updatedContent.append("AccountNumber:").append(accountNumber)
                                    .append(", PIN:").append(newPIN)
                                    .append(", Balance:").append(parts[2].split(":")[1].trim())
                                    .append(", Name:").append(parts[3].split(":")[1].trim()).append("\n");
                            isUpdated = true;
                        } else {
                            updatedContent.append(line).append("\n");
                        }
                    }
                }

                if (isUpdated) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
                        writer.write(updatedContent.toString());
                    }
                    JOptionPane.showMessageDialog(null, "PIN reset successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Account not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error resetting PIN: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void unlockAccount() {
        try {
            List<String> lockedAccounts = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("locked_accounts.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lockedAccounts.add(line);
                }
            }

            if (lockedAccounts.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No locked accounts found", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] accountsArray = lockedAccounts.toArray(new String[0]);
            String selectedAccount = (String) JOptionPane.showInputDialog(null, "Select account to unlock",
                    "Unlock Account", JOptionPane.QUESTION_MESSAGE, null, accountsArray, accountsArray[0]);

            if (selectedAccount != null) {
                lockedAccounts.remove(selectedAccount);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("locked_accounts.txt"))) {
                    for (String account : lockedAccounts) {
                        writer.write(account + "\n");
                    }
                }
                JOptionPane.showMessageDialog(null, "Account unlocked successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error unlocking account: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // creating a new user
    private void openCreateUserWindow(JFrame adminPanel) {
        adminPanel.setVisible(false); // hide admin panel

        JFrame createUserFrame = new JFrame("Create New User");
        createUserFrame.setSize(400, 300);
        createUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createUserFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setBounds(50, 40, 120, 25);
        panel.add(accountNumberLabel);

        JTextField accountNumberField = new JTextField();
        accountNumberField.setBounds(180, 40, 165, 25);
        panel.add(accountNumberField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(50, 80, 120, 25);
        panel.add(pinLabel);

        JPasswordField pinField = new JPasswordField();
        pinField.setBounds(180, 80, 165, 25);
        panel.add(pinField);

        JLabel balanceLabel = new JLabel("Balance:");
        balanceLabel.setBounds(50, 120, 120, 25);
        panel.add(balanceLabel);

        JTextField balanceField = new JTextField();
        balanceField.setBounds(180, 120, 165, 25);
        panel.add(balanceField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 160, 120, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(180, 160, 165, 25);
        panel.add(nameField);

        JButton createButton = new JButton("Create User");
        createButton.setBounds(150, 200, 100, 25);
        createButton.addActionListener(e -> {
            createUser(accountNumberField, pinField, balanceField, nameField);
            createUserFrame.dispose();      // close create user window
            adminPanel.setVisible(true);    // show admin panel again
        });
        panel.add(createButton);

        createUserFrame.add(panel);
        createUserFrame.setVisible(true);
    }


    private void createUser(JTextField accountNumberField, JPasswordField pinField, JTextField balanceField, JTextField nameField) {
        String accountNumber = accountNumberField.getText();
        String pin = new String(pinField.getPassword());
        String balance = balanceField.getText();
        String name = nameField.getText();
    
        // acc num 10 digits
        if (accountNumber.length() != 10 || !accountNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Account number must be exactly 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // pin 4 digits
        if (pin.length() != 4 || !pin.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "PIN must be exactly 4 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double accountBalance = 0;
    
        // emplty name
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // create if all checks pass
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write("AccountNumber:" + accountNumber + ", PIN:" + pin + ", Balance:" + accountBalance + ", Name:" + name + "\n");
            JOptionPane.showMessageDialog(null, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error creating account: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    // user panel
    private void openUserLogin() {
        JFrame userFrame = new JFrame("User Login");
        userFrame.setSize(400, 300);
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Account Number:");
        userLabel.setBounds(50, 40, 120, 25);
        panel.add(userLabel);

        userText = new JTextField();
        userText.setBounds(180, 40, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("PIN:");
        passwordLabel.setBounds(50, 80, 120, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(180, 80, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Log in");
        loginButton.setBounds(150, 120, 100, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        success = new JLabel("");
        success.setBounds(50, 160, 300, 25);
        panel.add(success);

        userFrame.add(panel);
        userFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String enteredAccountNumber = userText.getText();
        String enteredPIN = new String(passwordText.getPassword());

        if (validateCredentials(enteredAccountNumber, enteredPIN)) {
            success.setText("Login successful!");
            openBankingOperationsWindow();
        } else {
            success.setText("Invalid credentials.");
        }
    }


    private boolean validateCredentials(String accountNumber, String pin) {
        try {
            long accNumber = Long.parseLong(accountNumber);
            int accPIN = Integer.parseInt(pin);
            if (pin.length() != 4 || !pin.matches("\\d+")) {
                success.setText("PIN must be exactly 4 numeric digits.");
                JOptionPane.showMessageDialog(null, "PIN must be exactly 4 numeric digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // check if the account is locked
            if (isAccountLocked(accNumber)) {
                String lockMessage = "This account is locked. Contact admin.";
                success.setText(lockMessage);
                JOptionPane.showMessageDialog(null, lockMessage, "Account Locked", JOptionPane.ERROR_MESSAGE);
                return false;
            }
    
            // create a temporary account for authentication
            Account tempAccount = new Account(accNumber, 0, "Temporary Name", accPIN);
            if (tempAccount.authenticateWithLock(accNumber, accPIN)) {
                loggedInAccount = getAccountFromFile(accNumber, accPIN);
                return loggedInAccount != null;
            } else {
                success.setText("Invalid credentials.");
                return false;
            }
        } catch (NumberFormatException ex) {
            success.setText("Invalid input format for account number or PIN.");
            return false;
        } catch (Exception ex) {
            success.setText("Error during login: " + ex.getMessage());
            return false;
        }
    }
    
    private boolean isAccountLocked(long accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader("locked_accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("AccountNumber:" + accountNumber)) {
                    return true; // Account is locked
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading locked accounts: " + e.getMessage());
        }
        return false; // Account is not locked
    }

    private Account getAccountFromFile(long accountNumber, int pin) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Checking line: " + line);
                String[] details = line.split(", ");
                long fileAccountNumber = Long.parseLong(details[0].split(":")[1].trim());
                int filePIN = Integer.parseInt(details[1].split(":")[1].trim());
                double fileBalance = Double.parseDouble(details[2].split(":")[1].trim());
                String fileName = details[3].split(":")[1].trim();
    
                if (fileAccountNumber == accountNumber && filePIN == pin) {
                    System.out.println("Match found for AccountNumber: " + accountNumber);
                    return new Account(fileAccountNumber, fileBalance, fileName, filePIN);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users.txt: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing account details: " + e.getMessage());
        }
        return null;
    }
    

    private void openBankingOperationsWindow() {
        JFrame bankingFrame = new JFrame("Banking Operations");
        bankingFrame.setSize(400, 400);
        bankingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bankingFrame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel balanceLabel = new JLabel("Current Balance: " + loggedInAccount.getBalance());
        balanceLabel.setBounds(50, 20, 300, 25);
        panel.add(balanceLabel);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 60, 100, 25);
        panel.add(amountLabel);

        JTextField amountField = new JTextField();
        amountField.setBounds(120, 60, 150, 25);
        panel.add(amountField);

        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(50, 100, 100, 25);
        panel.add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(170, 100, 100, 25);
        panel.add(withdrawButton);

        JButton historyButton = new JButton("Transaction History");
        historyButton.setBounds(60, 140, 200, 25);
        panel.add(historyButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(150, 180, 100, 25);
        exitButton.addActionListener(e -> {
            System.exit(0);  // completely exits the program
        });
        panel.add(exitButton);

        JLabel statusLabel = new JLabel("");
        statusLabel.setBounds(50, 220, 300, 25);
        panel.add(statusLabel);

        depositButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                loggedInAccount.deposit(amount); // Update balance and persist to file
                balanceLabel.setText("Current Balance: " + loggedInAccount.getBalance());
                statusLabel.setText("Deposit successful!");
                // add type of transaction to history
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                loggedInAccount.withdraw(amount); // Update balance and persist to file
                balanceLabel.setText("Current Balance: " + loggedInAccount.getBalance());
                statusLabel.setText("Withdrawal successful!");
                // add type of transaction to history
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        historyButton.addActionListener(e -> showTransactionHistory());

        bankingFrame.add(panel);
        bankingFrame.setVisible(true);
    }

    private void showTransactionHistory() {
        JFrame historyFrame = new JFrame("Transaction History");
        historyFrame.setSize(500, 400);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(null);
    
        JTextArea historyArea = new JTextArea();
        historyArea.setBounds(20, 20, 450, 300);
        historyArea.setEditable(false);
    
        StringBuilder history = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("Transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Debugging: Print to confirm lines are read
                System.out.println("Read line: " + line);
    
                // Filter transactions for the current account
                if (line.startsWith("AccountNumber:" + loggedInAccount.getAccountNumber())) {
                    history.append(line).append("\n"); // Append to display history
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading transaction history: " + ex.getMessage());
            history.append("Error reading transaction history: ").append(ex.getMessage());
        }
    
        if (history.length() == 0) {
            historyArea.setText("No transactions found for this account."); // Show if no transactions
        } else {
            historyArea.setText(history.toString()); // Display transactions
        }
    
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBounds(20, 20, 450, 300);
        panel.add(scrollPane);
    
        historyFrame.add(panel);
        historyFrame.setVisible(true);
    }
}
