/**
 * CLASS: Admin
 * Purpose: System administration with full configuration rights
 * Extends: Employee
 */
public class Admin extends Employee {
    private SystemConfiguration systemConfig;
    private List<SystemMaintenanceTask> maintenanceTasks;
    private SecurityConfig securityConfig;
    
    // System Management
    private List<SystemBackup> backups;
    private List<SystemUpdate> appliedUpdates;
    private DisasterRecoveryPlan recoveryPlan;

    /**
     * System-wide interest rate management
     */
    public void setSystemInterestRate(InterestRateType type, double newRate, 
                                     Date effectiveFrom) {
        InterestRate rate = new InterestRate(type, newRate, effectiveFrom);
        systemConfig.updateInterestRate(rate);
        
        getAuditTrail().logEvent("INTEREST_RATE_UPDATED",
            "Updated " + type + " to " + newRate + "% effective " + effectiveFrom);
    }
    
    /**
     * End-of-day batch processing
     */
    public void runEndOfDayProcess() {
        try {
            // 1. Data backup
            SystemBackup backup = performSystemBackup();
            backups.add(backup);
            
            // 2. Interest accrual
            processDailyInterest();
            
            // 3. Account maintenance
            updateAccountStatuses();
            
            // 4. Report generation
            generateEODReports();
            
            // 5. Data archiving
            archiveOldTransactions();
            
            getAuditTrail().logEvent("EOD_PROCESS_COMPLETED",
                "End-of-day processing completed successfully");
                
        } catch (Exception e) {
            getAuditTrail().logEvent("EOD_PROCESS_FAILED",
                "EOD process failed: " + e.getMessage());
            throw new EODProcessException("EOD process failed", e);
        }
    }
    
    /**
     * Regulatory compliance reporting
     */
    public ComplianceReport generateRBIComplianceReport() {
        ComplianceReport report = new ComplianceReport();
        report.generateRBICompliance();
        
        // RBI specific checks
        report.verifyCRRCompliance();  // Cash Reserve Ratio
        report.verifySLRCompliance();  // Statutory Liquidity Ratio
        report.verifyNPAClassification(); // Non-Performing Assets
        
        getAuditTrail().logEvent("RBI_REPORT_GENERATED",
            "RBI compliance report generated");
        
        return report;
    }
}