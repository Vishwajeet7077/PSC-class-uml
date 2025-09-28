/**
 * CLASS: Employee
 * Purpose: Base class for all bank staff with branch associations
 * Extends: User
 */
public abstract class Employee extends User {
    private final String employeeId;       // Bank employee ID
    private String designation;            // Job title
    private Department department;         // Operations, IT, Management, etc.
    private EmployeeGrade grade;           // A1, A2, B1, etc.
    private double salary;                 // Monthly compensation
    private Date dateOfJoining;
    private Date dateOfRetirement;
    
    // Work Management
    private Branch assignedBranch;
    private WorkShift currentShift;
    private boolean isOnDuty;
    private List<WorkSchedule> workSchedules;
    
    // Performance Tracking
    private PerformanceMetrics performance;
    private List<Achievement> achievements;
    private List<Training> completedTrainings;
    
    // Operational Authority
    private ApprovalLimits approvalLimits;
    private TransactionLimits processingLimits;
    
    // Organizational Hierarchy
    private Employee reportingManager;
    private List<Employee> teamMembers;

    /**
     * Duty management with validation
     */
    public void startDuty(WorkShift shift) {
        if (!isScheduledForShift(shift)) {
            throw new UnauthorizedShiftException("Not scheduled for this shift");
        }
        
        this.currentShift = shift;
        this.isOnDuty = true;
        
        getAuditTrail().logEvent("DUTY_STARTED", 
            "Started " + shift + " shift at branch: " + assignedBranch.getBranchCode());
    }
    
    /**
     * End-of-day reconciliation hook
     */
    protected abstract void performEndOfDayReconciliation();
}