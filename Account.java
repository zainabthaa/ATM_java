import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.JOptionPane;

public class Account { //! Zainab and Leen
    // Attributes
    protected long accountNumber;
    protected double balance;
    protected String accHolderName;
    protected int PIN;
    protected String name;
    private ArrayList<Transactions> transactionHistory;

    // File paths
    private static final String USERS_FILE = System.getProperty("user.dir") + "/users.txt";
    private static final String LOCKED_ACCOUNTS_FILE = System.getProperty("user.dir") + "/locked_accounts.txt";

    // Main Constructor
    public Account(long accountNumber, double balance, String accHolderName, int PIN) {
        this.transactionHistory = new ArrayList<>();

        // Validate account number length
        if (String.valueOf(accountNumber).length() != 10) {
            throw new IllegalArgumentException("Invalid Account Number. Must be 10 digits.");
        }
        this.accountNumber = accountNumber;

        // Validate PIN length
        if (String.valueOf(PIN).length() != 4) {
            throw new IllegalArgumentException("Invalid PIN. Must be 4 digits.");
        }
        this.PIN = PIN;

        this.balance = balance;
        this.accHolderName = accHolderName;
    }

    // Overloaded Constructor
    public Account(long accountNumber, String accHolderName, int PIN, String name) {
        this(accountNumber, 0.0, accHolderName, PIN);
        this.name = name;
    }

    // Empty Constructor
    public Account() {
        this.transactionHistory = new ArrayList<>();
    }

    // Getter Methods
    public long getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccHolderName() {
        return accHolderName;
    }

    public int getPIN() {
        return PIN;
    }

    // Setter for PIN
    public void setPIN(int newPIN) {
        if (String.valueOf(newPIN).length() != 4) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
        this.PIN = newPIN;
        updateBalanceInFile();
    }

    // Deposit Method
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        addTransaction("Deposit", amount);
        updateBalanceInFile();
    }

    // Withdraw Method
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        balance -= amount;
        addTransaction("Withdrawal", amount);
        updateBalanceInFile();
    }

    // Add Transaction
    public void addTransaction(String type, double amount) {
        double transactionID = UUID.randomUUID().hashCode(); // Generate a unique ID
        Transactions transaction = new Transactions(transactionID, amount, type);
        transactionHistory.add(transaction);
        transaction.appendToAccountTransactions(String.valueOf(accountNumber)); // Write to file
    }

    // Update Balance in File
    private void updateBalanceInFile() {
        StringBuilder updatedContent = new StringBuilder();
        boolean accountFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String fileAccountNumber = parts[0].split(":")[1].trim();

                if (fileAccountNumber.equals(String.valueOf(accountNumber))) {
                    updatedContent.append("AccountNumber:").append(fileAccountNumber)
                                  .append(", PIN:").append(parts[1].split(":")[1].trim())
                                  .append(", Balance:").append(balance)
                                  .append(", Name:").append(parts[3].split(":")[1].trim())
                                  .append("\n");
                    accountFound = true;
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        if (accountFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
                writer.write(updatedContent.toString());
            } catch (IOException e) {
                System.out.println("Error writing to the file: " + e.getMessage());
            }
        } else {
            System.out.println("Account not found in the file.");
        }
    }

    // Authenticate Method
    public boolean authenticate(long accountNumber, int PIN) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(", ");
                String fileAccountNumber = null;
                String filePIN = null;

                for (String detail : details) {
                    String[] keyValue = detail.split(":");
                    if (keyValue[0].equalsIgnoreCase("AccountNumber")) {
                        fileAccountNumber = keyValue[1].trim();
                    } else if (keyValue[0].equalsIgnoreCase("PIN")) {
                        filePIN = keyValue[1].trim();
                    }
                }

                if (fileAccountNumber != null && filePIN != null &&
                        fileAccountNumber.equals(String.valueOf(accountNumber)) &&
                        filePIN.equals(String.valueOf(PIN))) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error authenticating: " + e.getMessage());
        }
        return false;
    }

    // Lock Account Method
    public void lockAccount(long accountNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOCKED_ACCOUNTS_FILE, true))) {
            writer.write("AccountNumber:" + accountNumber + "\n");
            JOptionPane.showMessageDialog(null, "Account locked: " + accountNumber,
                    "Account Locked", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error locking account: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Authenticate with Lock
    public boolean authenticateWithLock(long accountNumber, int PIN) {
        int failedAttempts = 0;

        while (failedAttempts < 3) {
            
            if (authenticate(accountNumber, PIN)) {
                return true;
            } else {
                failedAttempts++;
                JOptionPane.showMessageDialog(null, "Invalid credentials. Attempt " + failedAttempts + " of 3.",
                        "Authentication Failed", JOptionPane.WARNING_MESSAGE);
            }

            String newPINInput = JOptionPane.showInputDialog("Re-enter your PIN:");
            if (newPINInput == null) {
                JOptionPane.showMessageDialog(null, "Login cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            try {
                PIN = Integer.parseInt(newPINInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid PIN format. Please enter 4 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(null, "Account temporarily locked due to incorrect login attempts.",
                "Account Locked", JOptionPane.ERROR_MESSAGE);
        lockAccount(accountNumber);
        return false;
    }
}
