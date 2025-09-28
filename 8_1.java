public class AccessControl {
    /**
     * Role-based permission validation
     */
    public boolean checkPermission(User user, Permission permission) {
        return user.getRoles().stream()
            .anyMatch(role -> role.hasPermission(permission));
    }
    
    /**
     Transaction authorization based on amount and user role
     */
    public boolean authorizeTransaction(Transaction transaction, User user) {
        double amount = transaction.getAmount();
        
        if (user instanceof Clerk) {
            return amount <= ((Clerk) user).getProcessingLimits().getMaxTransactionAmount();
        } else if (user instanceof Manager) {
            return amount <= ((Manager) user).getTransactionOverrideLimit();
        } else if (user instanceof Admin) {
            return true; // Admin has unlimited authority
        }
        
        return false;
    }
}