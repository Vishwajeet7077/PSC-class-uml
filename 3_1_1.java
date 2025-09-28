/**
 * CLASS: Account
 * Purpose: Base class for all account types with common operations
 */
public abstract class Account {
    // Core Account Properties
    private final String accountNumber;    // Unique account identifier
    private final Customer accountHolder;  // Account owner
    private double balance;                // Current available balance
    private final Date openingDate;        // Account creation date
    private AccountStatus status;          // ACTIVE, DORMANT, BLOCKED, CLOSED
    
    // Financial Properties
    private double interestRate;           // Annual interest rate
    private Currency currency;             // Account currency
    private AccountRules rules;            // Configurable business rules
    
    // Transaction History
    private List<Transaction> transactions;
    private double totalDeposits;
    private double totalWithdrawals;
    
    // Linked Relationships
    private List<DepositAccount> linkedDeposits;
    private List<Nominee> nominees;

    /**
     * Deposit operation with validation
     */
    public final void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        
        if (this.status != AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Account is not active for deposits");
        }
        
        this.balance += amount;
        this.totalDeposits += amount;
        
        // Create transaction record
        Transaction depositTx = new DepositTransaction(this, amount);
        this.transactions.add(depositTx);
        
        // Update linked deposits if any
        updateLinkedDeposits();
    }
    
    /**
     * Withdrawal operation with balance check
     */
    public final boolean withdraw(double amount) {
        if (!canWithdraw(amount)) {
            return false;
        }
        
        this.balance -= amount;
        this.totalWithdrawals += amount;
        
        Transaction withdrawalTx = new WithdrawalTransaction(this, amount);
        this.transactions.add(withdrawalTx);
        
        return true;
    }
    
    /**
     * Abstract method for account-specific withdrawal rules
     */
    public abstract boolean canWithdraw(double amount);
    
    /**
     * Interest calculation based on account type
     */
    public abstract double calculateInterest();
}