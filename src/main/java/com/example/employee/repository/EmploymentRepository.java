package com.example.employee.repository;

import com.example.employee.model.Employee;
import com.example.employee.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Employment entities.
 */
@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Long> {

    /**
     * Find an employment by its code.
     * 
     * @param code The code to search for
     * @return An Optional containing the employment if found
     */
    Optional<Employment> findByCode(String code);

    /**
     * Find all employments for a specific employee.
     * 
     * @param employee The employee to find employments for
     * @return A list of employments for the employee
     */
    List<Employment> findByEmployee(Employee employee);

    /**
     * Find all active employments for a specific employee.
     * 
     * @param employee The employee to find active employments for
     * @param status The status to filter by (ACTIVE)
     * @return A list of active employments for the employee
     */
    List<Employment> findByEmployeeAndStatus(Employee employee, Employment.EmploymentStatus status);

    /**
     * Check if an employment exists with the given code.
     * 
     * @param code The code to check
     * @return True if an employment exists with the code, false otherwise
     */
    boolean existsByCode(String code);
}