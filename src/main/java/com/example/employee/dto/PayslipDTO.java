package com.example.employee.dto;

import com.example.employee.model.Employee;
import com.example.employee.model.Payslip;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for Payslip entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayslipDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeCode;
    private String employeeName;

    private BigDecimal houseAmount;
    private BigDecimal transportAmount;
    private BigDecimal employeeTaxedAmount;
    private BigDecimal pensionAmount;
    private BigDecimal medicalInsuranceAmount;
    private BigDecimal otherTaxedAmount;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be after 2000")
    private Integer year;

    private Payslip.PayslipStatus status = Payslip.PayslipStatus.PENDING;

    /**
     * Convert DTO to Entity.
     * 
     * @param employee The employee associated with this payslip
     * @return Payslip entity
     */
    public Payslip toEntity(Employee employee) {
        Payslip payslip = new Payslip();
        // Don't set ID for new entities (only set for updates)
        if (this.id != null) {
            payslip.setId(this.id);
        }
        payslip.setEmployee(employee);
        payslip.setHouseAmount(this.houseAmount);
        payslip.setTransportAmount(this.transportAmount);
        payslip.setEmployeeTaxedAmount(this.employeeTaxedAmount);
        payslip.setPensionAmount(this.pensionAmount);
        payslip.setMedicalInsuranceAmount(this.medicalInsuranceAmount);
        payslip.setOtherTaxedAmount(this.otherTaxedAmount);
        payslip.setGrossSalary(this.grossSalary);
        payslip.setNetSalary(this.netSalary);
        payslip.setMonth(this.month);
        payslip.setYear(this.year);
        payslip.setStatus(this.status);
        return payslip;
    }

    /**
     * Create DTO from Entity.
     * 
     * @param payslip Payslip entity
     * @return PayslipDTO
     */
    public static PayslipDTO fromEntity(Payslip payslip) {
        PayslipDTO dto = new PayslipDTO();
        dto.setId(payslip.getId());
        dto.setEmployeeId(payslip.getEmployee().getId());
        dto.setEmployeeCode(payslip.getEmployee().getCode());
        dto.setEmployeeName(payslip.getEmployee().getFirstName() + " " + payslip.getEmployee().getLastName());
        dto.setHouseAmount(payslip.getHouseAmount());
        dto.setTransportAmount(payslip.getTransportAmount());
        dto.setEmployeeTaxedAmount(payslip.getEmployeeTaxedAmount());
        dto.setPensionAmount(payslip.getPensionAmount());
        dto.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
        dto.setOtherTaxedAmount(payslip.getOtherTaxedAmount());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setMonth(payslip.getMonth());
        dto.setYear(payslip.getYear());
        dto.setStatus(payslip.getStatus());
        return dto;
    }
}