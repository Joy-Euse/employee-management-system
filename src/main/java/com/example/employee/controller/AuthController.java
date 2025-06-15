package com.example.employee.controller;

import com.example.employee.dto.AuthRequest;
import com.example.employee.dto.AuthResponse;
import com.example.employee.dto.EmployeeDTO;
import com.example.employee.model.Employee;
import com.example.employee.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Authenticate a user and generate a JWT token.
     * 
     * @param loginRequest The login request
     * @return The authentication response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        AuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Register a new employee.
     * 
     * @param employeeDTO The employee data
     * @return The registered employee
     */
    @PostMapping("/register")
    public ResponseEntity<EmployeeDTO> registerUser(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = authService.registerEmployee(employeeDTO);
        return ResponseEntity.ok(EmployeeDTO.fromEntity(employee));
    }
}