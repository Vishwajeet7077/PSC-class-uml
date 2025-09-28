/**
 * CLASS: Loan
 * Purpose: Base class for all loan products
 */
public abstract class Loan {
    // Loan Identification
    private final String loanAccountNumber;
    private final Customer borrower;
    private final double principalAmount;
    private final double interestRate;
    private final Date disbursalDate;
    private final Date maturityDate;
    
    // Loan Status
    private LoanStatus status;             // APPLIED, APPROVED, DISBURSED, CLOSED, NPA
    private int overdueDays;
    private double outstandingPrincipal;
    private double totalInterestAccrued;
    
    // Repayment Structure
    private List<EMI> emiSchedule;
    private int tenureMonths;
    private RepaymentFrequency frequency;  // MONTHLY, QUARTERLY
    
    // Security & Collateral
    private Collateral collateral;
    private List<Guarantor> guarantors;
    
    // Financial Tracking
    private double totalRepaid;
    private double totalInterestPaid;
    private double prepaymentCharges;

    /**
     * EMI calculation using reducing balance method
     */
    public double calculateEMI() {
        double monthlyRate = getInterestRate() / 1200; // Monthly interest rate
        int months = getTenureMonths();
        
        double emi = (getPrincipalAmount() * monthlyRate * 
                     Math.pow(1 + monthlyRate, months)) / 
                    (Math.pow(1 + monthlyRate, months) - 1);
        
        return Math.round(emi * 100) / 100.0; // Round to 2 decimal places
    }
    
    /**
     * Loan eligibility assessment
     */
    public abstract boolean isEligible(Customer customer);
    
    /**
     * NPA classification as per RBI guidelines
     */
    public boolean isNPA() {
        return getOverdueDays() > 90; // Standard asset becomes NPA after 90 days overdue
    }
}