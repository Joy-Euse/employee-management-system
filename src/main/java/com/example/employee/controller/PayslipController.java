package com.example.employee.controller;

import com.example.employee.dto.PayslipDTO;
import com.example.employee.service.PayslipService;
import com.example.employee.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for payslip endpoints.
 */
@RestController
@RequestMapping("/api/payslips")
public class PayslipController {

    @Autowired
    private PayslipService payslipService;

    @Autowired
    private SecurityService securityService;

    /**
     * Get all payslips.
     * 
     * @return List of payslip DTOs
     */
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<PayslipDTO>> getAllPayslips() {
        List<PayslipDTO> payslips = payslipService.getAllPayslips();
        return ResponseEntity.ok(payslips);
    }

    /**
     * Get payslip by ID.
     * 
     * @param id Payslip ID
     * @return Payslip DTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isCurrentUser(#id))")
    public ResponseEntity<PayslipDTO> getPayslipById(@PathVariable Long id) {
        PayslipDTO payslip = payslipService.getPayslipById(id);
        return ResponseEntity.ok(payslip);
    }

    /**
     * Get payslips by employee ID.
     * 
     * @param employeeId Employee ID
     * @return List of payslip DTOs
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isCurrentUser(#employeeId))")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByEmployeeId(@PathVariable Long employeeId) {
        List<PayslipDTO> payslips = payslipService.getPayslipsByEmployeeId(employeeId);
        return ResponseEntity.ok(payslips);
    }

    /**
     * Get payslips by month and year.
     * 
     * @param month Month
     * @param year Year
     * @return List of payslip DTOs
     */
    @GetMapping("/month/{month}/year/{year}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByMonthAndYear(
            @PathVariable Integer month, @PathVariable Integer year) {
        List<PayslipDTO> payslips = payslipService.getPayslipsByMonthAndYear(month, year);
        return ResponseEntity.ok(payslips);
    }

    /**
     * Get payslip by employee ID, month, and year.
     * 
     * @param employeeId Employee ID
     * @param month Month
     * @param year Year
     * @return Payslip DTO
     */
    @GetMapping("/employee/{employeeId}/month/{month}/year/{year}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isCurrentUser(#employeeId))")
    public ResponseEntity<PayslipDTO> getPayslipByEmployeeIdAndMonthAndYear(
            @PathVariable Long employeeId, @PathVariable Integer month, @PathVariable Integer year) {
        PayslipDTO payslip = payslipService.getPayslipByEmployeeIdAndMonthAndYear(employeeId, month, year);
        return ResponseEntity.ok(payslip);
    }

    /**
     * Generate payroll for all active employees for a specific month and year.
     * 
     * @param month Month
     * @param year Year
     * @return List of generated payslip DTOs
     */
    @PostMapping("/generate/month/{month}/year/{year}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<PayslipDTO>> generatePayroll(
            @PathVariable Integer month, @PathVariable Integer year) {
        List<PayslipDTO> payslips = payslipService.generatePayroll(month, year);
        return ResponseEntity.ok(payslips);
    }

    /**
     * Approve a payslip.
     * 
     * @param id Payslip ID
     * @return Approved payslip DTO
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PayslipDTO> approvePayslip(@PathVariable Long id) {
        PayslipDTO payslip = payslipService.approvePayslip(id);
        return ResponseEntity.ok(payslip);
    }

    /**
     * Delete a payslip.
     * 
     * @param id Payslip ID
     * @return Response with no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayslip(@PathVariable Long id) {
        payslipService.deletePayslip(id);
        return ResponseEntity.noContent().build();
    }
}