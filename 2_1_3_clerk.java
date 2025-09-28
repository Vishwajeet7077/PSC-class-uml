/**
 * CLASS: Clerk
 * Purpose: Front-line staff for customer transactions and services
 * Extends: Employee
 */
public class Clerk extends Employee {
    private String tellerId;               // Teller station identifier
    private CashDrawer cashDrawer;         // Physical cash management
    private Queue<CustomerServiceRequest> serviceQueue;
    private List<Transaction> processedTransactions;
    
    // Operational Metrics
    private int customersServedToday;
    private double cashHandledToday;
    private ServiceEfficiencyMetrics efficiency;

    /**
     * Customer onboarding with document verification
     */
    public Customer onboardNewCustomer(CustomerRegistrationRequest request) {
        // Document validation
        if (!validateKYCDocuments(request.getDocuments())) {
            throw new InvalidDocumentException("KYC documents are invalid or expired");
        }
        
        // Customer creation
        Customer customer = createCustomerFromRequest(request);
        customer.updateKYCStatus(KYCStatus.VERIFIED, getEmployeeId());
        
        // Initial account setup
        Account account = customer.openAccount(
            request.getAccountType(),
            request.getInitialDeposit(),
            Currency.INR,
            AccountRules.getDefaultRules(request.getAccountType())
        );
        
        // Metrics update
        customersServedToday++;
        
        getAuditTrail().logEvent("CUSTOMER_ONBOARDED", 
            "New customer: " + customer.getUserId() + " with account: " + account.getAccountNumber());
        
        return customer;
    }
    
    /**
     * Cash deposit processing with denomination tracking
     */
    public CashTransaction processCashDeposit(Account account, double amount, 
                                            Denomination breakdown) {
        // Authorization check
        if (!hasPermission(Permission.PROCESS_CASH_TRANSACTIONS)) {
            throw new InsufficientPermissionsException();
        }
        
        // Limit validation
        if (amount > getProcessingLimits().getMaxCashTransaction()) {
            throw new TransactionLimitExceededException(
                "Amount exceeds clerk cash limit: " + getProcessingLimits().getMaxCashTransaction());
        }
        
        // Cash handling
        cashDrawer.addCash(breakdown);
        cashHandledToday += amount;
        
        // Transaction processing
        CashTransaction transaction = new CashTransaction(
            account, amount, TransactionType.CASH_DEPOSIT, this);
        transaction.setCashBreakdown(breakdown);
        
        processTransaction(transaction);
        processedTransactions.add(transaction);
        
        return transaction;
    }
}