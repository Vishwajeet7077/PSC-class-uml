public class BranchBalanceSheet extends BalanceSheet {
    private String branchId;
    private double cashInVault;
    private double interBranchPayables;
    private double interBranchReceivables;
    
    public BranchBalanceSheet(Branch branch) {
        this.entityId = branch.getBranchId();
        initializeBranchAccounts();
    }
    
    private void initializeBranchAccounts() {
        // Asset accounts
        assets.put("CASH_IN_VAULT", 0.0);
        assets.put("BALANCES_WITH_RBI", 0.0);
        assets.put("LOANS_ADVANCES", 0.0);
        assets.put("INTER_BRANCH_RECEIVABLES", 0.0);
        assets.put("FIXED_ASSETS", 0.0);
        
        // Liability accounts
        liabilities.put("CUSTOMER_DEPOSITS", 0.0);
        liabilities.put("INTER_BRANCH_PAYABLES", 0.0);
        liabilities.put("BORROWINGS", 0.0);
        
        // Equity
        equity.put("BRANCH_EQUITY", 0.0);
    }
    
    /**
     * Calculates branch profitability
     */
    public double calculateBranchProfit() {
        double interestIncome = assets.getOrDefault("INTEREST_INCOME", 0.0);
        double feeIncome = assets.getOrDefault("FEE_INCOME", 0.0);
        double operatingExpenses = liabilities.getOrDefault("OPERATING_EXPENSES", 0.0);
        double interestExpenses = liabilities.getOrDefault("INTEREST_EXPENSES", 0.0);
        
        return (interestIncome + feeIncome) - (operatingExpenses + interestExpenses);
    }
    
    /**
     * Inter-branch settlement update
     */
    public void updateInterBranchSettlement(double amount, boolean isReceivable) {
        if (isReceivable) {
            updateBalance("INTER_BRANCH_RECEIVABLES", amount, EntryType.DEBIT);
        } else {
            updateBalance("INTER_BRANCH_PAYABLES", amount, EntryType.CREDIT);
        }
    }
}