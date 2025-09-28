public class HomeLoan extends Loan {
    private String propertyAddress;
    private double propertyValue;
    private double ltvRatio;               // Loan-to-Value ratio
    private PropertyType propertyType;     // RESIDENTIAL, COMMERCIAL
    private ConstructionStage stage;       // UNDER_CONSTRUCTION, READY_TO_MOVE
    
    @Override
    public boolean isEligible(Customer customer) {
        // LTV compliance (RBI guidelines)
        if (getLtvRatio() > 0.75) { // Max 75% LTV for loans above ₹30L
            return false;
        }
        
        // Income criteria
        double emi = calculateEMI();
        if (emi > customer.getIncome().getMonthlyIncome() * 0.5) {
            return false; // EMI should not exceed 50% of monthly income
        }
        
        // Credit score requirement
        if (customer.getCreditScore().getScore() < 750) {
            return false;
        }
        
        return true;
    }
    
    public double getTaxBenefit() {
        // Section 24: ₹2,00,000 interest deduction
        // Section 80C: ₹1,50,000 principal deduction
        double annualInterest = calculateEMI() * 12 - getPrincipalAmount() / getTenureMonths();
        return Math.min(annualInterest, 200000) + Math.min(getPrincipalAmount() / getTenureMonths(), 150000);
    }
}