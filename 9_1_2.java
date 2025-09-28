public class NPAManager {
    /**
     * NPA classification as per RBI guidelines
     */
    public NPACategory classifyLoan(Loan loan) {
        int overdueDays = loan.getOverdueDays();
        
        if (overdueDays <= 90) {
            return NPACategory.STANDARD;
        } else if (overdueDays <= 365) {
            return NPACategory.SUBSTANDARD;
        } else {
            return NPACategory.DOUBTFUL;
        }
    }
    
    /**
     * Provisioning calculation
     */
    public double calculateProvisioning(Loan loan) {
        NPACategory category = classifyLoan(loan);
        double outstanding = loan.getOutstandingPrincipal();
        
        switch (category) {
            case STANDARD:
                return outstanding * 0.00; // 0% provisioning
            case SUBSTANDARD:
                return outstanding * 0.15; // 15% provisioning
            case DOUBTFUL:
                return outstanding * 0.40; // 40% provisioning (1-3 years)
            case LOSS:
                return outstanding * 1.00; // 100% provisioning
            default:
                return 0.0;
        }
    }
}