package com.example.employee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Entity representing a deduction type that can be applied to an employee's salary.
 */
@Entity
@Table(name = "deductions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(name = "deduction_name", nullable = false, unique = true)
    private String deductionName;

    @Column(nullable = false)
    private BigDecimal percentage;

    /**
     * Factory method to create standard deductions with predefined percentages.
     * 
     * @return Array of standard deductions
     */
    public static Deduction[] createStandardDeductions() {
        return new Deduction[] {
            new Deduction(null, "EMP_TAX", "Employee Tax", new BigDecimal("30.0")),
            new Deduction(null, "PENSION", "Pension", new BigDecimal("6.0")),
            new Deduction(null, "MED_INS", "Medical Insurance", new BigDecimal("5.0")),
            new Deduction(null, "OTHERS", "Others", new BigDecimal("5.0")),
            new Deduction(null, "HOUSING", "Housing", new BigDecimal("14.0")),
            new Deduction(null, "TRANSPORT", "Transport", new BigDecimal("14.0"))
        };
    }
}