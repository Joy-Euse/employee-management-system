package com.example.employee.dto;

import com.example.employee.model.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for Employee entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String password;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Mobile number should be valid")
    private String mobile;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    private Employee.EmployeeStatus status = Employee.EmployeeStatus.ACTIVE;

    private Set<Employee.Role> roles;

    /**
     * Convert DTO to Entity.
     * 
     * @return Employee entity
     */
    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setId(this.id);
        employee.setCode(this.code);
        employee.setFirstName(this.firstName);
        employee.setLastName(this.lastName);
        employee.setEmail(this.email);
        if (this.password != null && !this.password.isEmpty()) {
            employee.setPassword(this.password); // Password will be encoded in the service layer
        }
        employee.setMobile(this.mobile);
        employee.setDateOfBirth(this.dateOfBirth);
        employee.setStatus(this.status);
        if (this.roles != null) {
            employee.setRoles(this.roles);
        }
        return employee;
    }

    /**
     * Create DTO from Entity.
     * 
     * @param employee Employee entity
     * @return EmployeeDTO
     */
    public static EmployeeDTO fromEntity(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setCode(employee.getCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        // Don't set password for security reasons
        dto.setMobile(employee.getMobile());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setStatus(employee.getStatus());
        dto.setRoles(employee.getRoles());
        return dto;
    }
}