/**
 * CLASS: BalanceSheet
 * Purpose: Base class for all balance sheet implementations
 */
public abstract class BalanceSheet {
    protected String entityId;
    protected Date asOfDate;
    protected Map<String, Double> assets;
    protected Map<String, Double> liabilities;
    protected Map<String, Double> equity;
    
    /**
     * Double-entry accounting principle enforcement
     */
    public void updateBalance(String accountCode, double amount, EntryType type) {
        if (type == EntryType.DEBIT) {
            debitAccount(accountCode, amount);
        } else {
            creditAccount(accountCode, amount);
        }
        
        if (!validateBalanced()) {
            throw new UnbalancedEntryException("Balance sheet is not balanced");
        }
    }
    
    /**
     * Fundamental accounting equation: Assets = Liabilities + Equity
     */
    public boolean validateBalanced() {
        double totalAssets = getTotalAssets();
        double totalLiabilitiesEquity = getTotalLiabilities() + getTotalEquity();
        return Math.abs(totalAssets - totalLiabilitiesEquity) < 0.01;
    }
    
    public abstract double getNetWorth();
}