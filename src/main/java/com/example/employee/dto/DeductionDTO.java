package com.example.employee.dto;

import com.example.employee.model.Deduction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Deduction entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductionDTO {

    private Long id;

    @NotBlank(message = "Code is required")
    @Size(min = 3, max = 20, message = "Code must be between 3 and 20 characters")
    private String code;

    @NotBlank(message = "Deduction name is required")
    @Size(min = 3, max = 50, message = "Deduction name must be between 3 and 50 characters")
    private String deductionName;

    @NotNull(message = "Percentage is required")
    @Positive(message = "Percentage must be positive")
    private BigDecimal percentage;

    /**
     * Convert DTO to Entity.
     * 
     * @return Deduction entity
     */
    public Deduction toEntity() {
        Deduction deduction = new Deduction();
        // Don't set ID for new entities (only set for updates)
        if (this.id != null) {
            deduction.setId(this.id);
        }
        deduction.setCode(this.code);
        deduction.setDeductionName(this.deductionName);
        deduction.setPercentage(this.percentage);
        return deduction;
    }

    /**
     * Create DTO from Entity.
     * 
     * @param deduction Deduction entity
     * @return DeductionDTO
     */
    public static DeductionDTO fromEntity(Deduction deduction) {
        DeductionDTO dto = new DeductionDTO();
        dto.setId(deduction.getId());
        dto.setCode(deduction.getCode());
        dto.setDeductionName(deduction.getDeductionName());
        dto.setPercentage(deduction.getPercentage());
        return dto;
    }
}