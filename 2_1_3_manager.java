/**
 * CLASS: Manager
 * Purpose: Branch management with approval authority and oversight
 * Extends: Employee
 */
public class Manager extends Employee {
    private double loanApprovalLimit;      // ₹25 Lakhs default
    private double transactionOverrideLimit; // ₹5 Lakhs default
    private List<Employee> teamMembers;
    private List<ApprovalRequest> pendingApprovals;
    
    // Branch Performance
    private BranchPerformanceMetrics branchPerformance;
    private List<BranchReport> generatedReports;

    /**
     * Loan approval with comprehensive verification
     */
    public LoanApplication approveLoan(LoanApplication application) {
        // Authority check
        if (!hasPermission(Permission.APPROVE_LOANS)) {
            throw new InsufficientPermissionsException();
        }
        
        // Limit validation
        if (application.getLoanAmount() > loanApprovalLimit) {
            throw new ApprovalLimitExceededException(
                "Loan amount ₹" + application.getLoanAmount() + 
                " exceeds approval limit ₹" + loanApprovalLimit);
        }
        
        // Risk assessment
        if (!verifyLoanApplication(application)) {
            application.reject("Application verification failed");
            return application;
        }
        
        // Approval processing
        application.approve(this);
        updateBranchPerformance(application);
        
        getAuditTrail().logEvent("LOAN_APPROVED",
            "Approved loan " + application.getApplicationId() + 
            " for ₹" + application.getLoanAmount());
        
        return application;
    }
    
    /**
     * Transaction limit override for exceptional cases
     */
    public boolean overrideTransactionLimit(Transaction transaction) {
        if (transaction.getAmount() > transactionOverrideLimit) {
            throw new OverrideLimitExceededException(
                "Override limit: ₹" + transactionOverrideLimit);
        }
        
        transaction.setNeedsOverride(false);
        transaction.setOverrideApprovedBy(this);
        
        getAuditTrail().logEvent("TRANSACTION_OVERRIDE_APPROVED",
            "Override approved for: " + transaction.getTransactionId());
        
        return true;
    }
}