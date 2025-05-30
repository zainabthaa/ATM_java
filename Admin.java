import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


public class Admin extends Account { //! Leen

    // Constructor
    public Admin(long accountNumber, int PIN, String accHolderName) {
        super(accountNumber, 0.0, accHolderName, PIN); // Calls the `Account` constructor
    }


    // Method to check for duplicate account numbers
    public boolean isAccountNumberDuplicate(String filePath, long accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Extract account number from each line
                String[] parts = line.split(", ");
                String fileAccountNumber = parts[0].split(":")[1].trim();
                if (fileAccountNumber.equals(String.valueOf(accountNumber))) {
                    return true; // Duplicate account number found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // No duplicate found
    }

    // Helper method to append data to a file
    private void appendToFile(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine(); // Add a new line after appending the data
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    // Helper method to append account number to the transactions file
    private void appendToTransactionsFile(String filePath, long accountNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("AccountNumber:" + accountNumber); // Write only the account number
            writer.newLine(); // Add a new line
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the transactions file: " + e.getMessage());
        }
    }

    // Method to reset PIN (example functionality for Admin)
    public boolean resetPIN(long accountNumber, int newPIN) {
        String filePath = "users.txt";
        StringBuilder updatedContent = new StringBuilder();
        boolean isUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");

                // Extract details from the line
                String fileAccountNumber = parts[0].split(":")[1].trim();

                if (fileAccountNumber.equals(String.valueOf(accountNumber))) {
                    // Update the PIN in the current record
                    updatedContent.append("AccountNumber:").append(fileAccountNumber)
                                  .append(", PIN:").append(newPIN) // Update the PIN
                                  .append(", Balance:").append(parts[2].split(":")[1].trim())
                                  .append(", Name:").append(parts[3].split(":")[1].trim()).append("\n");
                    isUpdated = true;
                } else {
                    // Keep the existing record unchanged
                    updatedContent.append(line).append("\n");
                }
            }

            if (!isUpdated) {
                System.out.println("Account not found. PIN could not be reset.");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Overwrite the file with updated content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("PIN successfully reset for account number: " + accountNumber);
        return true;
    }

    public void unlockAccount() {
        String filePath = "locked_accounts.txt";
        List<String> lockedAccounts = new ArrayList<>();

        // Read locked accounts from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lockedAccounts.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading locked accounts file: " + e.getMessage());
            return;
        }

        if (lockedAccounts.isEmpty()) {
            System.out.println("No locked accounts found.");
            return;
        }

        // Display locked accounts
        System.out.println("Locked Accounts:");
        for (int i = 0; i < lockedAccounts.size(); i++) {
            System.out.println((i + 1) + ". " + lockedAccounts.get(i));
        }

        // Admin selects an account to unlock
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of the account to unlock: ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > lockedAccounts.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        // Remove the selected account from the list and update the file
        lockedAccounts.remove(choice - 1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String account : lockedAccounts) {
                writer.write(account + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating locked accounts file: " + e.getMessage());
        }

        System.out.println("Account successfully unlocked.");
    }

    // Method to read and return all users as a formatted String
    public static String getAllUsers(String filePath) {
        StringBuilder allUsers = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allUsers.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allUsers.toString();
    }

}

    
    