/**
 * CLASS: SavingsAccount
 * Purpose: Personal savings with interest earnings
 * Extends: Account
 */
public class SavingsAccount extends Account {
    private double minimumBalance;          // Quarterly average balance requirement
    private int freeTransactionLimit;       // Monthly free transactions
    private double transactionCharges;      // Charges beyond free limit
    
    @Override
    public boolean canWithdraw(double amount) {
        // Minimum balance check
        if ((getBalance() - amount) < minimumBalance) {
            throw new MinimumBalanceException(
                "Withdrawal would violate minimum balance requirement");
        }
        
        // Dormancy check
        if (isDormant()) {
            throw new DormantAccountException("Account is dormant");
        }
        
        return true;
    }
    
    @Override
    public double calculateInterest() {
        // Daily interest calculation on daily closing balance
        double dailyRate = getInterestRate() / 36500; // Convert to daily decimal
        return getBalance() * dailyRate;
    }
    
    /**
     * Links to fixed deposit for auto-investment
     */
    public FixedDepositAccount createLinkedFD(double amount, int tenureMonths) {
        if (getBalance() < amount) {
            throw new InsufficientBalanceException();
        }
        
        FixedDepositAccount fd = new FixedDepositAccount(
            getAccountHolder(), amount, tenureMonths);
        fd.setLinkedFundingAccount(this);
        
        // Auto-debit from savings
        withdraw(amount);
        
        getLinkedDeposits().add(fd);
        return fd;
    }
}