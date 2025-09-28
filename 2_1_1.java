/**
 * BASE CLASS: User
 * Purpose: Foundation for all system users with common properties and behaviors
 */
public abstract class User {
    // Core Identification
    private final String userId;          // System-generated unique identifier
    private final String username;        // Login credential
    private String passwordHash;          // BCrypt encrypted password
    
    // Personal Information
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private final Date dateOfJoining;     // Immutable registration timestamp
    
    // Status Management
    private UserStatus status;            // ACTIVE, INACTIVE, SUSPENDED, LOCKED
    private int failedLoginAttempts;      // Security tracking
    private Date lastLoginDate;
    
    // Security Framework
    private List<SecurityQuestion> securityQuestions;
    private String twoFactorSecret;       // TOTP for 2FA
    private List<LoginSession> activeSessions;
    
    // Organizational Structure
    private Branch branch;                // Associated branch
    private Set<Role> roles;              // RBAC permissions
    private AuditTrail auditTrail;        // Complete activity log

    // CRITICAL METHODS:
    
    /**
     * Authenticates user with comprehensive security checks
     */
    public boolean login(String username, String password) {
        if (this.status != UserStatus.ACTIVE) {
            throw new UserInactiveException("Account is not active");
        }
        
        if (this.failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
            this.status = UserStatus.LOCKED;
            throw new AccountLockedException("Too many failed attempts");
        }
        
        boolean authenticated = PasswordUtil.verifyPassword(password, this.passwordHash);
        if (authenticated) {
            this.lastLoginDate = new Date();
            this.failedLoginAttempts = 0;
            createSession();
        } else {
            this.failedLoginAttempts++;
        }
        
        return authenticated;
    }
    
    /**
     * Password policy enforcement with security logging
     */
    public final boolean changePassword(String oldPassword, String newPassword) {
        if (!validatePassword(oldPassword)) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
        
        PasswordPolicy policy = new PasswordPolicy();
        if (!policy.validate(newPassword)) {
            throw new WeakPasswordException(policy.getRequirements());
        }
        
        this.passwordHash = PasswordUtil.hashPassword(newPassword);
        this.auditTrail.logEvent("PASSWORD_CHANGED", "Password updated successfully");
        
        return true;
    }
}