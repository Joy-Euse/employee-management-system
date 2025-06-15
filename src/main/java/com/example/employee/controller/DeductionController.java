package com.example.employee.controller;

import com.example.employee.dto.DeductionDTO;
import com.example.employee.service.DeductionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for deduction endpoints.
 */
@RestController
@RequestMapping("/api/deductions")
public class DeductionController {

    @Autowired
    private DeductionService deductionService;

    /**
     * Get all deductions.
     * 
     * @return List of deduction DTOs
     */
    @GetMapping
    public ResponseEntity<List<DeductionDTO>> getAllDeductions() {
        List<DeductionDTO> deductions = deductionService.getAllDeductions();
        return ResponseEntity.ok(deductions);
    }

    /**
     * Get deduction by ID.
     * 
     * @param id Deduction ID
     * @return Deduction DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeductionDTO> getDeductionById(@PathVariable Long id) {
        DeductionDTO deduction = deductionService.getDeductionById(id);
        return ResponseEntity.ok(deduction);
    }

    /**
     * Get deduction by code.
     * 
     * @param code Deduction code
     * @return Deduction DTO
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<DeductionDTO> getDeductionByCode(@PathVariable String code) {
        DeductionDTO deduction = deductionService.getDeductionByCode(code);
        return ResponseEntity.ok(deduction);
    }

    /**
     * Get deduction by name.
     * 
     * @param name Deduction name
     * @return Deduction DTO
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<DeductionDTO> getDeductionByName(@PathVariable String name) {
        DeductionDTO deduction = deductionService.getDeductionByName(name);
        return ResponseEntity.ok(deduction);
    }

    /**
     * Create a new deduction.
     * 
     * @param deductionDTO Deduction data
     * @return Created deduction DTO
     */
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DeductionDTO> createDeduction(@Valid @RequestBody DeductionDTO deductionDTO) {
        DeductionDTO createdDeduction = deductionService.createDeduction(deductionDTO);
        return ResponseEntity.ok(createdDeduction);
    }

    /**
     * Update an existing deduction.
     * 
     * @param id Deduction ID
     * @param deductionDTO Deduction data
     * @return Updated deduction DTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<DeductionDTO> updateDeduction(@PathVariable Long id, @Valid @RequestBody DeductionDTO deductionDTO) {
        DeductionDTO updatedDeduction = deductionService.updateDeduction(id, deductionDTO);
        return ResponseEntity.ok(updatedDeduction);
    }

    /**
     * Delete a deduction.
     * 
     * @param id Deduction ID
     * @return Response with no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeduction(@PathVariable Long id) {
        deductionService.deleteDeduction(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Initialize standard deductions.
     * 
     * @return Response with no content
     */
    @PostMapping("/initialize")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> initializeStandardDeductions() {
        deductionService.initializeStandardDeductions();
        return ResponseEntity.noContent().build();
    }
}