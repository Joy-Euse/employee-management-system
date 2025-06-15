package com.example.employee.repository;

import com.example.employee.model.Employee;
import com.example.employee.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Payslip entities.
 */
@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {

    /**
     * Find all payslips for a specific employee.
     * 
     * @param employee The employee to find payslips for
     * @return A list of payslips for the employee
     */
    List<Payslip> findByEmployee(Employee employee);

    /**
     * Find all payslips for a specific month and year.
     * 
     * @param month The month to search for
     * @param year The year to search for
     * @return A list of payslips for the month and year
     */
    List<Payslip> findByMonthAndYear(Integer month, Integer year);

    /**
     * Find all payslips for a specific employee, month, and year.
     * 
     * @param employee The employee to find payslips for
     * @param month The month to search for
     * @param year The year to search for
     * @return A list of payslips for the employee, month, and year
     */
    List<Payslip> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

    /**
     * Find a payslip for a specific employee, month, and year.
     * 
     * @param employee The employee to find the payslip for
     * @param month The month to search for
     * @param year The year to search for
     * @return An Optional containing the payslip if found
     */
    Optional<Payslip> findFirstByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

    /**
     * Find all payslips with a specific status.
     * 
     * @param status The status to filter by
     * @return A list of payslips with the status
     */
    List<Payslip> findByStatus(Payslip.PayslipStatus status);

    /**
     * Check if a payslip exists for a specific employee, month, and year.
     * 
     * @param employee The employee to check
     * @param month The month to check
     * @param year The year to check
     * @return True if a payslip exists, false otherwise
     */
    boolean existsByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
}