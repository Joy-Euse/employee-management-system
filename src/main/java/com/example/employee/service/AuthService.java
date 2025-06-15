package com.example.employee.service;

import com.example.employee.dto.AuthRequest;
import com.example.employee.dto.AuthResponse;
import com.example.employee.dto.EmployeeDTO;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.security.JwtTokenProvider;
import com.example.employee.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for authentication and registration.
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Authenticate a user and generate a JWT token.
     * 
     * @param loginRequest The login request
     * @return The authentication response with JWT token
     */
    public AuthResponse authenticateUser(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthResponse(jwt, userDetails.getId(), userDetails.getCode(), userDetails.getEmail(), roles);
    }

    /**
     * Register a new employee.
     * 
     * @param employeeDTO The employee data
     * @return The registered employee
     */
    @Transactional
    public Employee registerEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        if (employeeRepository.existsByCode(employeeDTO.getCode())) {
            throw new RuntimeException("Employee code is already in use!");
        }

        // Create new employee
        Employee employee = employeeDTO.toEntity();
        
        // Encode password
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        
        // Set default role if not provided
        if (employee.getRoles() == null || employee.getRoles().isEmpty()) {
            Set<Employee.Role> roles = new HashSet<>();
            roles.add(Employee.Role.ROLE_EMPLOYEE);
            employee.setRoles(roles);
        }

        return employeeRepository.save(employee);
    }
}