package com.example.employee.service;

import com.example.employee.dto.PayslipDTO;
import com.example.employee.model.Deduction;
import com.example.employee.model.Employee;
import com.example.employee.model.Employment;
import com.example.employee.model.Payslip;
import com.example.employee.repository.DeductionRepository;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.repository.EmploymentRepository;
import com.example.employee.repository.PayslipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for managing payslips and payroll generation.
 */
@Service
public class PayslipService {

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    /**
     * Get all payslips.
     * 
     * @return List of payslip DTOs
     */
    public List<PayslipDTO> getAllPayslips() {
        return payslipRepository.findAll().stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get payslip by ID.
     * 
     * @param id Payslip ID
     * @return Payslip DTO
     */
    public PayslipDTO getPayslipById(Long id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + id));
        return PayslipDTO.fromEntity(payslip);
    }

    /**
     * Get payslips by employee ID.
     * 
     * @param employeeId Employee ID
     * @return List of payslip DTOs
     */
    public List<PayslipDTO> getPayslipsByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        return payslipRepository.findByEmployee(employee).stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get payslips by month and year.
     * 
     * @param month Month
     * @param year Year
     * @return List of payslip DTOs
     */
    public List<PayslipDTO> getPayslipsByMonthAndYear(Integer month, Integer year) {
        return payslipRepository.findByMonthAndYear(month, year).stream()
                .map(PayslipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get payslip by employee ID, month, and year.
     * 
     * @param employeeId Employee ID
     * @param month Month
     * @param year Year
     * @return Payslip DTO
     */
    public PayslipDTO getPayslipByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        Payslip payslip = payslipRepository.findFirstByEmployeeAndMonthAndYear(employee, month, year)
                .orElseThrow(() -> new RuntimeException("Payslip not found for employee id: " + employeeId + 
                        ", month: " + month + ", year: " + year));
        
        return PayslipDTO.fromEntity(payslip);
    }

    /**
     * Generate payroll for all active employees for a specific month and year.
     * 
     * @param month Month
     * @param year Year
     * @return List of generated payslip DTOs
     */
    @Transactional
    public List<PayslipDTO> generatePayroll(Integer month, Integer year) {
        // Get all active employees
        List<Employee> activeEmployees = employeeRepository.findAll().stream()
                .filter(e -> e.getStatus() == Employee.EmployeeStatus.ACTIVE)
                .collect(Collectors.toList());
        
        // Get all deductions
        Map<String, Deduction> deductionsMap = deductionRepository.findAll().stream()
                .collect(Collectors.toMap(Deduction::getCode, Function.identity()));
        
        // Generate payslips for each active employee
        return activeEmployees.stream()
                .map(employee -> {
                    // Check if payslip already exists for this employee, month, and year
                    if (payslipRepository.existsByEmployeeAndMonthAndYear(employee, month, year)) {
                        throw new RuntimeException("Payslip already exists for employee id: " + employee.getId() + 
                                ", month: " + month + ", year: " + year);
                    }
                    
                    // Get active employment for the employee
                    List<Employment> activeEmployments = employmentRepository.findByEmployeeAndStatus(
                            employee, Employment.EmploymentStatus.ACTIVE);
                    
                    if (activeEmployments.isEmpty()) {
                        throw new RuntimeException("No active employment found for employee id: " + employee.getId());
                    }
                    
                    Employment activeEmployment = activeEmployments.get(0);
                    
                    // Calculate payslip
                    return calculatePayslip(employee, activeEmployment, deductionsMap, month, year);
                })
                .collect(Collectors.toList());
    }

    /**
     * Calculate payslip for an employee.
     * 
     * @param employee Employee
     * @param employment Employment
     * @param deductionsMap Map of deductions
     * @param month Month
     * @param year Year
     * @return Payslip DTO
     */
    private PayslipDTO calculatePayslip(Employee employee, Employment employment, 
                                       Map<String, Deduction> deductionsMap, Integer month, Integer year) {
        BigDecimal baseSalary = employment.getBaseSalary();
        
        // Calculate housing amount (14% of base salary)
        BigDecimal housingPercentage = deductionsMap.get("005").getPercentage().divide(new BigDecimal("100"));
        BigDecimal houseAmount = baseSalary.multiply(housingPercentage).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate transport amount (14% of base salary)
        BigDecimal transportPercentage = deductionsMap.get("006").getPercentage().divide(new BigDecimal("100"));
        BigDecimal transportAmount = baseSalary.multiply(transportPercentage).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate gross salary
        BigDecimal grossSalary = baseSalary.add(houseAmount).add(transportAmount).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate employee tax (30% of base salary)
        BigDecimal employeeTaxPercentage = deductionsMap.get("001").getPercentage().divide(new BigDecimal("100"));
        BigDecimal employeeTaxedAmount = baseSalary.multiply(employeeTaxPercentage).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate pension (6% of base salary)
        BigDecimal pensionPercentage = deductionsMap.get("002").getPercentage().divide(new BigDecimal("100"));
        BigDecimal pensionAmount = baseSalary.multiply(pensionPercentage).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate medical insurance (5% of base salary)
        BigDecimal medicalInsurancePercentage = deductionsMap.get("003").getPercentage().divide(new BigDecimal("100"));
        BigDecimal medicalInsuranceAmount = baseSalary.multiply(medicalInsurancePercentage).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate other deductions (5% of base salary)
        BigDecimal otherPercentage = deductionsMap.get("004").getPercentage().divide(new BigDecimal("100"));
        BigDecimal otherTaxedAmount = baseSalary.multiply(otherPercentage).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate net salary
        BigDecimal netSalary = grossSalary
                .subtract(employeeTaxedAmount)
                .subtract(pensionAmount)
                .subtract(medicalInsuranceAmount)
                .subtract(otherTaxedAmount)
                .setScale(2, RoundingMode.HALF_UP);
        
        // Create payslip
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setHouseAmount(houseAmount);
        payslip.setTransportAmount(transportAmount);
        payslip.setEmployeeTaxedAmount(employeeTaxedAmount);
        payslip.setPensionAmount(pensionAmount);
        payslip.setMedicalInsuranceAmount(medicalInsuranceAmount);
        payslip.setOtherTaxedAmount(otherTaxedAmount);
        payslip.setGrossSalary(grossSalary);
        payslip.setNetSalary(netSalary);
        payslip.setMonth(month);
        payslip.setYear(year);
        payslip.setStatus(Payslip.PayslipStatus.PENDING);
        
        Payslip savedPayslip = payslipRepository.save(payslip);
        return PayslipDTO.fromEntity(savedPayslip);
    }

    /**
     * Approve a payslip.
     * 
     * @param id Payslip ID
     * @return Approved payslip DTO
     */
    @Transactional
    public PayslipDTO approvePayslip(Long id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + id));
        
        if (payslip.getStatus() == Payslip.PayslipStatus.PAID) {
            throw new RuntimeException("Payslip is already approved");
        }
        
        payslip.setStatus(Payslip.PayslipStatus.PAID);
        Payslip updatedPayslip = payslipRepository.save(payslip);
        
        return PayslipDTO.fromEntity(updatedPayslip);
    }

    /**
     * Delete a payslip.
     * 
     * @param id Payslip ID
     */
    @Transactional
    public void deletePayslip(Long id) {
        if (!payslipRepository.existsById(id)) {
            throw new RuntimeException("Payslip not found with id: " + id);
        }
        payslipRepository.deleteById(id);
    }
}