public class CheckingAccount extends Account { //! Zainab
    // Must include account types

    //Additional Attribute: overdraftLimit
    // variables
    private int overDraftLimit;

    // constructor
    public CheckingAccount(int accountNumber, double balance, String accHolderName, int overDraftLimit, int PIN) {
        super(accountNumber, balance, accHolderName, PIN);
        if (overDraftLimit <= 0) {
            throw new IllegalArgumentException("Limit cannot be negative");
        } else {
            this.overDraftLimit = overDraftLimit;
        }
    }
    
    // get methods
    public int getOverDraftLimit() {
        return overDraftLimit;
    }

    // Overridden Methods for handling overdraft functionality
    // method to handle overdraft protection
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        
        if (amount > getBalance() + overDraftLimit) {
            throw new IllegalArgumentException("Overdraft limit exceeded.");
        }
        super.withdraw(amount);  // withdraw method from parent 
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Overdraft Limit: " + overDraftLimit;
    }


}
