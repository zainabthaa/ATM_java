public class SavingsAccount extends Account {  //! Zainab fix
    private double interestRate; 

    // Constructor
    public SavingsAccount(long accountNumber, double balance, String accHolderName, int PIN, double interestRate) {
        super(accountNumber, balance, accHolderName, PIN);
        this.interestRate = interestRate; // Initializes the interest rate
    }

    // Getter 
    public double getInterestRate() {
        return interestRate;
    }

    // Calculates the interest based on the balance and the current interest rate.
     
    public double calculateInterest() {
        return getBalance() * (interestRate / 100);
    }

    // Apply the calculated interest to the account balance.
     
    public void applyInterest() {
        double interest = calculateInterest();
        deposit(interest); 
        System.out.println("Applied interest: " + interest);
    }
}
