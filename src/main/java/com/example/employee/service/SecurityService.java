package com.example.employee.service;

import com.example.employee.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service for security-related operations.
 */
@Service
public class SecurityService {

    /**
     * Check if the current authenticated user is the same as the requested user.
     * 
     * @param userId The ID of the user to check
     * @return True if the current user is the same as the requested user, false otherwise
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return false;
        }
        
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        return userDetails.getId().equals(userId);
    }
}