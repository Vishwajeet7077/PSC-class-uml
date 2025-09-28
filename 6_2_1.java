public class GeneralLedger {
    private String ledgerId;
    private List<LedgerEntry> entries;
    private Map<String, Double> accountBalances;
    
    /**
     * Adds balanced ledger entry
     */
    public void addEntry(LedgerEntry entry) {
        if (!entry.validateBalanced()) {
            throw new UnbalancedEntryException("Debit and credit amounts must be equal");
        }
        
        // Update account balances
        for (JournalEntry journal : entry.getJournalEntries()) {
            String accountCode = journal.getAccountCode();
            double currentBalance = accountBalances.getOrDefault(accountCode, 0.0);
            double newBalance = journal.getEntryType() == EntryType.DEBIT 
                ? currentBalance + journal.getAmount() 
                : currentBalance - journal.getAmount();
            accountBalances.put(accountCode, newBalance);
        }
        
        entries.add(entry);
    }
    
    /**
     * Validates complete ledger integrity
     */
    public boolean validateLedger() {
        double totalDebits = entries.stream()
            .flatMap(entry -> entry.getJournalEntries().stream())
            .filter(journal -> journal.getEntryType() == EntryType.DEBIT)
            .mapToDouble(JournalEntry::getAmount)
            .sum();
            
        double totalCredits = entries.stream()
            .flatMap(entry -> entry.getJournalEntries().stream())
            .filter(journal -> journal.getEntryType() == EntryType.CREDIT)
            .mapToDouble(JournalEntry::getAmount)
            .sum();
            
        return Math.abs(totalDebits - totalCredits) < 0.01;
    }
}