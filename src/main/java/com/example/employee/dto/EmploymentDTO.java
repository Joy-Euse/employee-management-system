package com.example.employee.dto;

import com.example.employee.model.Employee;
import com.example.employee.model.Employment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for Employment entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {

    private Long id;

    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeCode;
    private String employeeName;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;

    private Employment.EmploymentStatus status = Employment.EmploymentStatus.ACTIVE;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    /**
     * Convert DTO to Entity.
     * 
     * @param employee The employee associated with this employment
     * @return Employment entity
     */
    public Employment toEntity(Employee employee) {
        Employment employment = new Employment();
        // Don't set ID for new entities (only set for updates)
        if (this.id != null) {
            employment.setId(this.id);
        }
        employment.setCode(this.code);
        employment.setEmployee(employee);
        employment.setDepartment(this.department);
        employment.setPosition(this.position);
        employment.setBaseSalary(this.baseSalary);
        employment.setStatus(this.status);
        employment.setJoiningDate(this.joiningDate);
        return employment;
    }

    /**
     * Create DTO from Entity.
     * 
     * @param employment Employment entity
     * @return EmploymentDTO
     */
    public static EmploymentDTO fromEntity(Employment employment) {
        EmploymentDTO dto = new EmploymentDTO();
        dto.setId(employment.getId());
        dto.setCode(employment.getCode());
        dto.setEmployeeId(employment.getEmployee().getId());
        dto.setEmployeeCode(employment.getEmployee().getCode());
        dto.setEmployeeName(employment.getEmployee().getFirstName() + " " + employment.getEmployee().getLastName());
        dto.setDepartment(employment.getDepartment());
        dto.setPosition(employment.getPosition());
        dto.setBaseSalary(employment.getBaseSalary());
        dto.setStatus(employment.getStatus());
        dto.setJoiningDate(employment.getJoiningDate());
        return dto;
    }
}