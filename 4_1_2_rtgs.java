public class RTGSTransaction extends FundTransfer {
    @Override
    protected boolean validate() {
        // RTGS minimum amount check
        if (getAmount() < 200000) {
            throw new InvalidAmountException("RTGS minimum amount is ₹2,00,000");
        }
        
        return true;
    }
    
    @Override
    public double getTransferCharge() {
        // Fixed charges for RTGS
        return getAmount() <= 500000 ? 25.00 : 50.00;
    }
}