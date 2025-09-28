public class UPITransaction extends FundTransfer {
    private String upiId;                  // Virtual Payment Address
    private String vpa;                    // Beneficiary VPA
    private String transactionNote;
    
    @Override
    protected boolean validate() {
        if (!UPIValidator.isValidVpa(getBeneficiaryVPA())) {
            throw new InvalidUPIException("Invalid VPA format");
        }
        
        if (getAmount() > 100000) { // ₹1 Lakh UPI limit
            throw new TransactionLimitExceededException("UPI limit exceeded");
        }
        
        return true;
    }
    
    @Override
    public double getTransferCharge() {
        return 0; // UPI transactions are typically free
    }
}