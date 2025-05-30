import java.io.*;

public class Transactions {
    private String transactionID;
    private double amount;
    private String transactionType;

    // Constructor to initialize transaction with ID, amount, and type
    public Transactions(String transactionID, double amount, String transactionType) {
        this.transactionID = transactionID;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    // Second constructor to initialize with a double transactionID (if needed)
    public Transactions(double transactionID, double amount, String type) {
        this.transactionID = String.valueOf(transactionID); // Convert double to String
        this.amount = amount;
        this.transactionType = type;
    }

    @Override
    public String toString() {
        return "TransactionID:" + transactionID +
               ", Amount:" + amount +
               ", Type:" + transactionType;
    }

    // Method to append the transaction to the transactions file
    public void appendToAccountTransactions(String accountNumber) {
        String filePath = System.getProperty("user.dir") + "/Transactions.txt"; // Path management

        // Format the transaction entry with type of transaction (Deposit or Withdrawal)
        String transactionEntry = "AccountNumber:" + accountNumber + ", " +
                                "TransactionID:" + transactionID + ", " +
                                "Amount:" + amount + ", " +
                                "Type:" + transactionType;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(transactionEntry); // Append the transaction
            writer.newLine(); // Ensure each transaction is on a new line
            System.out.println("Transaction successfully appended: " + transactionEntry);
        } catch (IOException e) {
            System.out.println("Error writing transaction to file: " + e.getMessage());
        }
    }


    // Getters for future use if needed
    public String getTransactionID() {
        return transactionID;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }
}
