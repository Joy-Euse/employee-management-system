package com.example.employee.repository;

import com.example.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing Employee entities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find an employee by their email.
     * 
     * @param email The email to search for
     * @return An Optional containing the employee if found
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Find an employee by their code.
     * 
     * @param code The code to search for
     * @return An Optional containing the employee if found
     */
    Optional<Employee> findByCode(String code);

    /**
     * Check if an employee exists with the given email.
     * 
     * @param email The email to check
     * @return True if an employee exists with the email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if an employee exists with the given code.
     * 
     * @param code The code to check
     * @return True if an employee exists with the code, false otherwise
     */
    boolean existsByCode(String code);
}