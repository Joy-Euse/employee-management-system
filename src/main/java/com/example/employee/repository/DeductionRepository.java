package com.example.employee.repository;

import com.example.employee.model.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing Deduction entities.
 */
@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long> {

    /**
     * Find a deduction by its code.
     * 
     * @param code The code to search for
     * @return An Optional containing the deduction if found
     */
    Optional<Deduction> findByCode(String code);

    /**
     * Find a deduction by its name.
     * 
     * @param deductionName The name to search for
     * @return An Optional containing the deduction if found
     */
    Optional<Deduction> findByDeductionName(String deductionName);

    /**
     * Check if a deduction exists with the given code.
     * 
     * @param code The code to check
     * @return True if a deduction exists with the code, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Check if a deduction exists with the given name.
     * 
     * @param deductionName The name to check
     * @return True if a deduction exists with the name, false otherwise
     */
    boolean existsByDeductionName(String deductionName);
}