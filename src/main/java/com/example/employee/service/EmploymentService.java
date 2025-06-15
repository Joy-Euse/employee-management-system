package com.example.employee.service;

import com.example.employee.dto.EmploymentDTO;
import com.example.employee.model.Employee;
import com.example.employee.model.Employment;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.repository.EmploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing employments.
 */
@Service
public class EmploymentService {

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Get all employments.
     * 
     * @return List of employment DTOs
     */
    public List<EmploymentDTO> getAllEmployments() {
        return employmentRepository.findAll().stream()
                .map(EmploymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get employment by ID.
     * 
     * @param id Employment ID
     * @return Employment DTO
     */
    public EmploymentDTO getEmploymentById(Long id) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found with id: " + id));
        return EmploymentDTO.fromEntity(employment);
    }

    /**
     * Get employment by code.
     * 
     * @param code Employment code
     * @return Employment DTO
     */
    public EmploymentDTO getEmploymentByCode(String code) {
        Employment employment = employmentRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Employment not found with code: " + code));
        return EmploymentDTO.fromEntity(employment);
    }

    /**
     * Get employments by employee ID.
     * 
     * @param employeeId Employee ID
     * @return List of employment DTOs
     */
    public List<EmploymentDTO> getEmploymentsByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        return employmentRepository.findByEmployee(employee).stream()
                .map(EmploymentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get active employment by employee ID.
     * 
     * @param employeeId Employee ID
     * @return Employment DTO
     */
    public EmploymentDTO getActiveEmploymentByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        List<Employment> activeEmployments = employmentRepository.findByEmployeeAndStatus(
                employee, Employment.EmploymentStatus.ACTIVE);
        
        if (activeEmployments.isEmpty()) {
            throw new RuntimeException("No active employment found for employee with id: " + employeeId);
        }
        
        return EmploymentDTO.fromEntity(activeEmployments.get(0));
    }

    /**
     * Create a new employment.
     * 
     * @param employmentDTO Employment data
     * @return Created employment DTO
     */
    @Transactional
    public EmploymentDTO createEmployment(EmploymentDTO employmentDTO) {
        if (employmentRepository.existsByCode(employmentDTO.getCode())) {
            throw new RuntimeException("Employment code is already in use!");
        }

        Employee employee = employeeRepository.findById(employmentDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employmentDTO.getEmployeeId()));

        Employment employment = employmentDTO.toEntity(employee);
        Employment savedEmployment = employmentRepository.save(employment);
        
        return EmploymentDTO.fromEntity(savedEmployment);
    }

    /**
     * Update an existing employment.
     * 
     * @param id Employment ID
     * @param employmentDTO Employment data
     * @return Updated employment DTO
     */
    @Transactional
    public EmploymentDTO updateEmployment(Long id, EmploymentDTO employmentDTO) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found with id: " + id));

        // Check if code is being changed and if it's already in use
        if (!employment.getCode().equals(employmentDTO.getCode()) && 
                employmentRepository.existsByCode(employmentDTO.getCode())) {
            throw new RuntimeException("Employment code is already in use!");
        }

        // Check if employee is being changed
        if (!employment.getEmployee().getId().equals(employmentDTO.getEmployeeId())) {
            Employee newEmployee = employeeRepository.findById(employmentDTO.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employmentDTO.getEmployeeId()));
            employment.setEmployee(newEmployee);
        }

        // Update employment fields
        employment.setCode(employmentDTO.getCode());
        employment.setDepartment(employmentDTO.getDepartment());
        employment.setPosition(employmentDTO.getPosition());
        employment.setBaseSalary(employmentDTO.getBaseSalary());
        employment.setStatus(employmentDTO.getStatus());
        employment.setJoiningDate(employmentDTO.getJoiningDate());

        Employment updatedEmployment = employmentRepository.save(employment);
        return EmploymentDTO.fromEntity(updatedEmployment);
    }

    /**
     * Delete an employment.
     * 
     * @param id Employment ID
     */
    @Transactional
    public void deleteEmployment(Long id) {
        if (!employmentRepository.existsById(id)) {
            throw new RuntimeException("Employment not found with id: " + id);
        }
        employmentRepository.deleteById(id);
    }
}