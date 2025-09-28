public class CashManagement {
    /**
     * Daily cash reconciliation for each teller
     */
    public ReconciliationResult reconcileTellerCash(Clerk clerk, double physicalCash) {
        double systemCash = clerk.getCashDrawer().getBalance();
        double variance = physicalCash - systemCash;
        
        if (Math.abs(variance) <= ALLOWED_VARIANCE) {
            return ReconciliationResult.balanced();
        } else if (variance > 0) {
            // Excess cash - create credit entry
            createCashOverageEntry(clerk, variance);
            return ReconciliationResult.overage(variance);
        } else {
            // Shortage - create debit entry
            createCashShortageEntry(clerk, Math.abs(variance));
            return ReconciliationResult.shortage(Math.abs(variance));
        }
    }
    
    /**
     * Vault management and cash ordering
     */
    public CashOrder calculateCashOrder(Branch branch, Date forDate) {
        double projectedRequirements = calculateProjectedCashNeeds(branch, forDate);
        double currentVaultBalance = branch.getVaultBalance();
        double optimalBalance = calculateOptimalVaultBalance(branch);
        
        if (currentVaultBalance < projectedRequirements) {
            double orderAmount = Math.min(
                projectedRequirements - currentVaultBalance,
                optimalBalance - currentVaultBalance
            );
            return new CashOrder(branch, forDate, orderAmount, Denomination.getOptimalMix(orderAmount));
        }
        
        return CashOrder.none();
    }
}