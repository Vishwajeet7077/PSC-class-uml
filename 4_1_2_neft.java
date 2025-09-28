public class NEFTTransaction extends FundTransfer {
    private String utrNumber;              // Unique Transaction Reference
    private NEFTSettlementBatch batch;     // Settlement batch
    
    @Override
    protected boolean validate() {
        // NEFT specific validations
        if (getBeneficiaryAccount() == null) {
            throw new InvalidAccountException("Beneficiary account required for NEFT");
        }
        
        if (!getBeneficiaryAccount().isActive()) {
            throw new AccountInactiveException("Beneficiary account is inactive");
        }
        
        return true;
    }
    
    @Override
    public double getTransferCharge() {
        // Slab-based charges
        if (getAmount() <= 10000) return 2.50;
        if (getAmount() <= 100000) return 5.00;
        if (getAmount() <= 200000) return 15.00;
        return 25.00; // Above ₹2 Lakhs
    }
}