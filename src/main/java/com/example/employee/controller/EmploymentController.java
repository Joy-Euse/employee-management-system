package com.example.employee.controller;

import com.example.employee.dto.EmploymentDTO;
import com.example.employee.service.EmploymentService;
import com.example.employee.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for employment endpoints.
 */
@RestController
@RequestMapping("/api/employments")
public class EmploymentController {

    @Autowired
    private EmploymentService employmentService;

    @Autowired
    private SecurityService securityService;

    /**
     * Get all employments.
     * 
     * @return List of employment DTOs
     */
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<EmploymentDTO>> getAllEmployments() {
        List<EmploymentDTO> employments = employmentService.getAllEmployments();
        return ResponseEntity.ok(employments);
    }

    /**
     * Get employment by ID.
     * 
     * @param id Employment ID
     * @return Employment DTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EmploymentDTO> getEmploymentById(@PathVariable Long id) {
        EmploymentDTO employment = employmentService.getEmploymentById(id);
        return ResponseEntity.ok(employment);
    }

    /**
     * Get employment by code.
     * 
     * @param code Employment code
     * @return Employment DTO
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EmploymentDTO> getEmploymentByCode(@PathVariable String code) {
        EmploymentDTO employment = employmentService.getEmploymentByCode(code);
        return ResponseEntity.ok(employment);
    }

    /**
     * Get employments by employee ID.
     * 
     * @param employeeId Employee ID
     * @return List of employment DTOs
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isCurrentUser(#employeeId))")
    public ResponseEntity<List<EmploymentDTO>> getEmploymentsByEmployeeId(@PathVariable Long employeeId) {
        List<EmploymentDTO> employments = employmentService.getEmploymentsByEmployeeId(employeeId);
        return ResponseEntity.ok(employments);
    }

    /**
     * Get active employment by employee ID.
     * 
     * @param employeeId Employee ID
     * @return Employment DTO
     */
    @GetMapping("/employee/{employeeId}/active")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isCurrentUser(#employeeId))")
    public ResponseEntity<EmploymentDTO> getActiveEmploymentByEmployeeId(@PathVariable Long employeeId) {
        EmploymentDTO employment = employmentService.getActiveEmploymentByEmployeeId(employeeId);
        return ResponseEntity.ok(employment);
    }

    /**
     * Create a new employment.
     * 
     * @param employmentDTO Employment data
     * @return Created employment DTO
     */
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EmploymentDTO> createEmployment(@Valid @RequestBody EmploymentDTO employmentDTO) {
        EmploymentDTO createdEmployment = employmentService.createEmployment(employmentDTO);
        return ResponseEntity.ok(createdEmployment);
    }

    /**
     * Update an existing employment.
     * 
     * @param id Employment ID
     * @param employmentDTO Employment data
     * @return Updated employment DTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EmploymentDTO> updateEmployment(@PathVariable Long id, @Valid @RequestBody EmploymentDTO employmentDTO) {
        EmploymentDTO updatedEmployment = employmentService.updateEmployment(id, employmentDTO);
        return ResponseEntity.ok(updatedEmployment);
    }

    /**
     * Delete an employment.
     * 
     * @param id Employment ID
     * @return Response with no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployment(@PathVariable Long id) {
        employmentService.deleteEmployment(id);
        return ResponseEntity.noContent().build();
    }
}