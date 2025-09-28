/**
 * Loan application processing pipeline
 */
public class LoanProcessor {
    public LoanApplication processApplication(LoanApplication application) {
        try {
            // 1. Document verification
            DocumentVerificationResult docResult = verifyDocuments(application);
            if (!docResult.isValid()) {
                return application.reject("Document verification failed: " + docResult.getRemarks());
            }
            
            // 2. Credit appraisal
            CreditAppraisal appraisal = assessCreditworthiness(application);
            if (!appraisal.isApproved()) {
                return application.reject("Credit appraisal failed: " + appraisal.getRemarks());
            }
            
            // 3. Eligibility check
            LoanEligibility eligibility = checkEligibility(application);
            if (!eligibility.isEligible()) {
                return application.reject("Eligibility criteria not met: " + eligibility.getRemarks());
            }
            
            // 4. Risk assessment
            RiskAssessment risk = assessRisk(application);
            if (risk.getRiskLevel() == RiskLevel.HIGH) {
                return application.reject("High risk application");
            }
            
            // 5. Approval routing based on amount
            double amount = application.getLoanAmount();
            if (amount <= 500000) {
                application.setStatus(LoanStatus.APPROVED);
            } else if (amount <= 2500000) {
                application.setStatus(LoanStatus.PENDING_MANAGER_APPROVAL);
            } else {
                application.setStatus(LoanStatus.PENDING_COMMITTEE_APPROVAL);
            }
            
            return application;
            
        } catch (Exception e) {
            return application.reject("Processing error: " + e.getMessage());
        }
    }
}