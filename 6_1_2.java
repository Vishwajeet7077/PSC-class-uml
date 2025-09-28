public class UserBalanceSheet extends BalanceSheet {
    private String userId;
    
    public UserBalanceSheet(User user) {
        this.entityId = user.getUserId();
        initializeAccounts();
    }
    
    private void initializeAccounts() {
        // Asset accounts
        assets.put("SAVINGS_ACCOUNTS", 0.0);
        assets.put("CURRENT_ACCOUNTS", 0.0);
        assets.put("FIXED_DEPOSITS", 0.0);
        assets.put("INVESTMENTS", 0.0);
        
        // Liability accounts
        liabilities.put("HOME_LOAN", 0.0);
        liabilities.put("PERSONAL_LOAN", 0.0);
        liabilities.put("CREDIT_CARD_OUTSTANDING", 0.0);
        liabilities.put("OVERDRAFT", 0.0);
        
        // Equity (Net Worth)
        equity.put("PERSONAL_EQUITY", 0.0);
    }
    
    @Override
    public double getNetWorth() {
        return getTotalAssets() - getTotalLiabilities();
    }
    
    /**
     * Updates from transaction impact
     */
    public void updateFromTransaction(Transaction transaction) {
        AccountingImpact impact = transaction.getAccountingImpact();
        
        for (Map.Entry<String, Double> entry : impact.getUserImpact().entrySet()) {
            String account = entry.getKey();
            double amount = entry.getValue();
            
            if (account.startsWith("ASSET_")) {
                updateBalance(account, amount, EntryType.DEBIT);
            } else if (account.startsWith("LIABILITY_")) {
                updateBalance(account, amount, EntryType.CREDIT);
            }
        }
    }
}