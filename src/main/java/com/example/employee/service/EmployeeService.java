package com.example.employee.service;

import com.example.employee.dto.EmployeeDTO;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing employees.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get all employees.
     * 
     * @return List of employee DTOs
     */
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get employee by ID.
     * 
     * @param id Employee ID
     * @return Employee DTO
     */
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return EmployeeDTO.fromEntity(employee);
    }

    /**
     * Get employee by email.
     * 
     * @param email Employee email
     * @return Employee DTO
     */
    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        return EmployeeDTO.fromEntity(employee);
    }

    /**
     * Get employee by code.
     * 
     * @param code Employee code
     * @return Employee DTO
     */
    public EmployeeDTO getEmployeeByCode(String code) {
        Employee employee = employeeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + code));
        return EmployeeDTO.fromEntity(employee);
    }

    /**
     * Create a new employee.
     * 
     * @param employeeDTO Employee data
     * @return Created employee DTO
     */
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        if (employeeRepository.existsByCode(employeeDTO.getCode())) {
            throw new RuntimeException("Employee code is already in use!");
        }

        Employee employee = employeeDTO.toEntity();
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeDTO.fromEntity(savedEmployee);
    }

    /**
     * Update an existing employee.
     * 
     * @param id Employee ID
     * @param employeeDTO Employee data
     * @return Updated employee DTO
     */
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Check if email is being changed and if it's already in use
        if (!employee.getEmail().equals(employeeDTO.getEmail()) && 
                employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Check if code is being changed and if it's already in use
        if (!employee.getCode().equals(employeeDTO.getCode()) && 
                employeeRepository.existsByCode(employeeDTO.getCode())) {
            throw new RuntimeException("Employee code is already in use!");
        }

        // Update employee fields
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setCode(employeeDTO.getCode());
        employee.setMobile(employeeDTO.getMobile());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setStatus(employeeDTO.getStatus());
        employee.setRoles(employeeDTO.getRoles());

        // Update password if provided
        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return EmployeeDTO.fromEntity(updatedEmployee);
    }

    /**
     * Delete an employee.
     * 
     * @param id Employee ID
     */
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}