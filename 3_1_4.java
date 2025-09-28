/**
 * CLASS: FixedDepositAccount
 * Purpose: Term deposit with fixed interest and tenure
 * Extends: DepositAccount
 */
public class FixedDepositAccount extends DepositAccount {
    private boolean autoRenewal;            // Automatic renewal at maturity
    private PayoutFrequency interestPayout; // Monthly, quarterly, maturity
    private double penaltyRate;             // Premature withdrawal penalty
    
    @Override
    public double calculateInterest() {
        // Simple interest calculation
        double principal = getDepositAmount();
        double rate = getInterestRate();
        int days = getDaysSinceOpening();
        
        return (principal * rate * days) / (100 * 365);
    }
    
    @Override
    public double getPenaltyOnPrematureWithdrawal() {
        int monthsCompleted = getMonthsSinceOpening();
        if (monthsCompleted < 3) {
            return calculateInterest() * 0.5; // 50% penalty
        } else if (monthsCompleted < 6) {
            return calculateInterest() * 0.25; // 25% penalty
        }
        return 0;
    }
    
    /**
     * Maturity processing with auto-renewal
     */
    public void processMaturity() {
        double maturityAmount = getDepositAmount() + calculateInterest();
        
        if (autoRenewal) {
            // Reinvest for same tenure
            setDepositAmount(maturityAmount);
            setOpeningDate(new Date());
        } else {
            // Payout to linked account
            Account linkedAccount = getLinkedFundingAccount();
            if (linkedAccount != null) {
                linkedAccount.deposit(maturityAmount);
            }
            close(ClosureReason.MATURITY);
        }
    }
}