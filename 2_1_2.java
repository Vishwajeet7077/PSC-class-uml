/**
 * CLASS: Customer
 * Purpose: Represents banking customers with financial relationships
 * Extends: User
 */
public class Customer extends User {
    // KYC Compliance Data
    private String panNumber;              // Permanent Account Number
    private String aadharNumber;           // Government ID
    private KYCDocument kycDocument;
    private KYCStatus kycStatus;           // PENDING, VERIFIED, REJECTED
    private Date kycVerifiedDate;
    private String kycVerifiedBy;          // Employee ID who verified
    
    // Financial Profile
    private EmploymentDetails employment;
    private IncomeDetails income;
    private CreditScore creditScore;       // CIBIL/CRIF score
    private RiskProfile riskProfile;       // LOW, MEDIUM, HIGH
    
    // Banking Relationships
    private List<Account> accounts;        // All customer accounts
    private List<LoanAccount> loanAccounts; // Active loans
    private List<Beneficiary> beneficiaries; // Trusted payees
    private List<Nominee> nominees;        // Account nominees
    
    // Transaction Governance
    private TransactionLimits transactionLimits;
    private Map<ChannelType, DailyLimit> dailyLimits; // Per-channel limits
    
    // Behavioral Preferences
    private NotificationPreferences notificationPrefs;
    private Language preferredLanguage;
    private CommunicationChannel preferredChannel;

    // KEY OPERATIONS:
    
    /**
     * Opens new banking account with KYC verification
     */
    public Account openAccount(AccountType type, double initialDeposit, 
                              Currency currency, AccountRules rules) {
        // Pre-validation checks
        if (!isKYCVerified()) {
            throw new KYCNotVerifiedException("Complete KYC verification first");
        }
        
        if (initialDeposit < rules.getMinOpeningBalance()) {
            throw new InsufficientDepositException(
                "Minimum deposit: " + rules.getMinOpeningBalance());
        }
        
        // Account creation
        Account account = AccountFactory.createAccount(type, this, currency, rules);
        account.deposit(initialDeposit);
        this.accounts.add(account);
        
        // Audit logging
        getAuditTrail().logEvent("ACCOUNT_OPENED", 
            String.format("Opened %s account %s with ₹%.2f", 
                         type, account.getAccountNumber(), initialDeposit));
        
        return account;
    }
    
    /**
     * Processes fund transfer with limit validation
     */
    public Transaction transferMoney(Account fromAccount, Account toAccount, 
                                   double amount, String remarks) {
        // Comprehensive validation
        validateTransferEligibility(fromAccount, toAccount, amount);
        
        // Channel-specific limit check
        ChannelType channel = determineTransferChannel(fromAccount, toAccount);
        checkDailyLimit(channel, amount);
        
        // Transaction execution
        FundTransfer transfer = TransactionFactory.createFundTransfer(
            fromAccount, toAccount, amount, remarks, this);
        
        TransactionResult result = transfer.process();
        if (result.isSuccess()) {
            updateSpendingPattern(amount, transfer.getType());
            updateDailyLimits(channel, amount);
        }
        
        return transfer;
    }
    
    /**
     * Applies for loan with eligibility assessment
     */
    public LoanApplication applyForLoan(LoanType loanType, double amount, 
                                       int tenure, PurposeOfLoan purpose) {
        LoanEligibility eligibility = checkLoanEligibility(loanType, amount, tenure);
        if (!eligibility.isEligible()) {
            throw new LoanNotEligibleException(eligibility.getRejectionReason());
        }
        
        LoanApplication application = new LoanApplication(
            this, loanType, amount, tenure, purpose);
        
        getAuditTrail().logEvent("LOAN_APPLIED", 
            String.format("Applied for %s loan: ₹%.2f for %d months", 
                         loanType, amount, tenure));
        
        return application;
    }
}