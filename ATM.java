import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ATM {
    private List<Account> accounts; // User accounts list
    private List<Transactions> transactionHistory; // Logged transactions list

    public ATM() {
        this.accounts = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
        loadAccounts();
    }

    // Load accounts from users.txt
    private void loadAccounts() {
        String filePath = System.getProperty("user.dir") + "/users.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                long accNumber = Long.parseLong(parts[0].split(":")[1]);
                int pin = Integer.parseInt(parts[1].split(":")[1]);
                double balance = Double.parseDouble(parts[2].split(":")[1]);
                String name = parts[3].split(":")[1];
                accounts.add(new Account(accNumber, balance, name, pin));
            }
            System.out.println("Accounts successfully loaded.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading user accounts from file: " + e.getMessage());
        }
    }

    // Authenticate a user's credentials.
    public Account authenticateUser(long accountNumber, int pin) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber && acc.getPIN() == pin) {
                System.out.println("User authenticated.");
                return acc; // Success
            }
        }
        System.err.println("Authentication failed.");
        return null; // Fail
    }

    // Perform a transaction for a logged-in account.
    public void performTransaction(Account account, double amount, String type) {
        if (account == null) {
            System.err.println("Invalid account provided.");
            return;
        }

        try {
            if (type.equalsIgnoreCase("deposit")) {
                account.deposit(amount);
                logTransaction(account, amount, "Deposit");
                System.out.println("Deposit successful.");
            } else if (type.equalsIgnoreCase("withdraw")) {
                account.withdraw(amount);
                logTransaction(account, amount, "Withdrawal");
                System.out.println("Withdrawal successful.");
            } else {
                System.err.println("Invalid transaction type.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Transaction error: " + e.getMessage());
        }
    }

    // Transfer funds between accounts
    public void transfer(Account sourceAccount, long destinationAccountNumber, double amount) {
        if (sourceAccount.getBalance() < amount) {
            System.err.println("Insufficient funds for transfer.");
            return;
        }

        Account destinationAccount = findAccountByNumber(destinationAccountNumber);
        if (destinationAccount == null) {
            System.err.println("Destination account not found.");
            return;
        }

        // Perform the transfer
        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);

        // Log the transactions for both accounts
        logTransaction(sourceAccount, amount, "Transfer Out");
        logTransaction(destinationAccount, amount, "Transfer In");

        System.out.println("Transfer successful. $" + amount + " transferred from Account "
                + sourceAccount.getAccountNumber() + " to Account " + destinationAccountNumber + ".");
    }

    // Find an account by its account number
    private Account findAccountByNumber(long accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc;
            }
        }
        return null;
    }

    // Adds a transaction into history and saves it using the Transactions class.
    private void logTransaction(Account account, double amount, String type) {
        String transactionID = UUID.randomUUID().toString(); // Use UUID for unique transaction ID
        Transactions t = new Transactions(transactionID, amount, type);
        transactionHistory.add(t);
        t.appendToAccountTransactions(String.valueOf(account.getAccountNumber())); // Ensure account number is passed as a string
        System.out.println("Transaction logged: " + t.toString()); // Debugging statement
    }

    // Administrative task to create a new user account.
    public void createAccount(long accNumber, int pin, String name) {
        if (findAccountByNumber(accNumber) != null) {
            System.err.println("Account number already exists.");
            return;
        }

        Account newAcc = new Account(accNumber, 0.0, name, pin);
        accounts.add(newAcc);
        saveAccountToFile(newAcc);
        System.out.println("Account created successfully.");
    }

    // Save a new account to the users.txt file
    private void saveAccountToFile(Account account) {
        String filePath = System.getProperty("user.dir") + "/users.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write("AccountNumber:" + account.getAccountNumber() + ", PIN:" +
                    account.getPIN() + ", Balance:" + account.getBalance() + "\n");
            System.out.println("Account saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving account to file: " + e.getMessage());
        }
    }
}
