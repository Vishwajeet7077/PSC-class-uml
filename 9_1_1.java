public class ReserveCompliance {
    private static final double CRR_RATE = 0.045;  // 4.5%
    private static final double SLR_RATE = 0.180;  // 18.0%
    
    /**
     * Cash Reserve Ratio compliance check
     */
    public boolean checkCRRCompliance(Bank bank, Date asOfDate) {
        double netDemandTimeLiabilities = calculateNDTL(bank, asOfDate);
        double requiredCRR = netDemandTimeLiabilities * CRR_RATE;
        double actualBalanceWithRBI = bank.getBalanceWithRBI();
        
        return actualBalanceWithRBI >= requiredCRR;
    }
    
    /**
     * Statutory Liquidity Ratio compliance check
     */
    public boolean checkSLRCompliance(Bank bank, Date asOfDate) {
        double netDemandTimeLiabilities = calculateNDTL(bank, asOfDate);
        double requiredSLR = netDemandTimeLiabilities * SLR_RATE;
        double liquidAssets = calculateLiquidAssets(bank);
        
        return liquidAssets >= requiredSLR;
    }
}