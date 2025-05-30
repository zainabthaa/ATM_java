import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class User extends Account { //! Leen
    // Constructor
    public User(long accountNumber, double balance, String accHolderName, int PIN) {
        super(accountNumber, balance, accHolderName, PIN);
    }

    // View Balance Method
    public void view_balance(String accHolderName, int inputPIN) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");

                // Split details from the file
                int filePIN = Integer.parseInt(parts[1].split(":")[1].trim());
                String balance = parts[2].split(":")[1].trim();

                // Authenticate user
                if (filePIN == inputPIN && authenticate(accountNumber, inputPIN)) {
                    System.out.println("Account Holder: " + accHolderName);
                    System.out.println("Balance: " + balance);
                    return; // Exit method after showing balance
                }
            }

            // If no match was found
            System.out.println("Account not found or incorrect credentials.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing PIN or other numeric values from file.");
        }
    }

    // View Account Information Method
    public void viewAccountInformation() {
        // Display account number and account holder's name
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder Name: " + accHolderName);
    }
}
