/**
 * CLASS: Transaction
 * Purpose: Base class for all financial transactions
 */
public abstract class Transaction {
    // Core Transaction Properties
    private final String transactionId;    // Unique transaction identifier
    private final Account sourceAccount;   // Originating account
    private final double amount;           // Transaction amount
    private final Date timestamp;          // Creation timestamp
    private TransactionStatus status;      // PENDING, SUCCESS, FAILED
    private String remarks;                // Transaction description
    
    // Processing Information
    private Employee processedBy;          // Staff who processed
    private boolean needsAuthorization;    // Manager approval flag
    private Employee authorizedBy;         // Approving manager
    
    // Financial Impact
    private AccountingImpact accountingImpact;
    private List<Fee> appliedFees;
    
    // Audit Trail
    private String referenceNumber;        // Bank reference
    private String systemTraceNumber;      // System tracking

    /**
     * Template method for transaction processing
     */
    public final TransactionResult process() {
        try {
            // 1. Pre-validation
            if (!validate()) {
                return TransactionResult.failed("Validation failed");
            }
            
            // 2. Fraud check
            if (!fraudCheck()) {
                return TransactionResult.failed("Fraud detection triggered");
            }
            
            // 3. Limit check
            if (!limitCheck()) {
                return TransactionResult.failed("Limit exceeded");
            }
            
            // 4. Authorization check
            if (needsAuthorization && !isAuthorized()) {
                return TransactionResult.pendingAuthorization();
            }
            
            // 5. Accounting entries
            createAccountingEntries();
            
            // 6. Balance update
            updateBalances();
            
            // 7. Fee application
            applyFees();
            
            // 8. Notification
            sendNotifications();
            
            this.status = TransactionStatus.SUCCESS;
            return TransactionResult.success(this);
            
        } catch (Exception e) {
            this.status = TransactionStatus.FAILED;
            rollback();
            return TransactionResult.failed(e.getMessage());
        }
    }
    
    protected abstract boolean validate();
    protected abstract boolean fraudCheck();
    protected abstract boolean limitCheck();
    protected abstract void createAccountingEntries();
    protected abstract void updateBalances();
}