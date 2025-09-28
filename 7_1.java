public class EndOfDayProcessor {
    public void executeEOD() {
        // 1. Transaction cutoff
        cutoffAllTransactions();
        
        // 2. Interest accrual
        accrueDailyInterest();
        
        // 3. Fee processing
        processAccountMaintenanceFees();
        
        // 4. GL updating
        updateGeneralLedger();
        
        // 5. Balance sheet generation
        generateDailyBalanceSheets();
        
        // 6. Reconciliation
        performDailyReconciliation();
        
        // 7. Report generation
        generateEODReports();
        
        // 8. Data archiving
        archiveDailyData();
    }
    
    private void accrueDailyInterest() {
        List<Account> accounts = AccountRepository.getAllInterestBearingAccounts();
        for (Account account : accounts) {
            if (account instanceof InterestBearingAccount) {
                InterestBearingAccount interestAccount = (InterestBearingAccount) account;
                interestAccount.accrueDailyInterest();
            }
        }
    }
}