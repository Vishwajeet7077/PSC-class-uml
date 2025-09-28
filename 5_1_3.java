public class PersonalLoan extends Loan {
    private String purpose;
    private boolean insuranceCovered;
    
    @Override
    public boolean isEligible(Customer customer) {
        // Minimum income requirement
        if (customer.getIncome().getAnnualIncome() < 300000) {
            return false; // ₹3 Lakhs minimum annual income
        }
        
        // Employment stability
        if (customer.getEmployment().getEmploymentTenure() < 2) {
            return false; // Minimum 2 years employment
        }
        
        // Credit score threshold
        if (customer.getCreditScore().getScore() < 650) {
            return false;
        }
        
        // Debt-to-income ratio
        double existingEmis = customer.getLoanAccounts().stream()
            .mapToDouble(LoanAccount::getEMI)
            .sum();
        double totalMonthlyObligation = existingEmis + calculateEMI();
        
        if (totalMonthlyObligation > customer.getIncome().getMonthlyIncome() * 0.6) {
            return false; // Total obligations should not exceed 60% of income
        }
        
        return true;
    }
}