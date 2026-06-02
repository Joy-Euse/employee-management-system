package com.example.employee.dto;

import com.example.employee.model.Employee;
import com.example.employee.model.Message;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Message entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeCode;
    private String employeeName;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be after 2000")
    private Integer year;

    private LocalDateTime createdAt;

    private boolean sent;

    /**
     * Convert DTO to Entity.
     * 
     * @param employee The employee associated with this message
     * @return Message entity
     */
    public Message toEntity(Employee employee) {
        Message message = new Message();
        // Don't set ID for new entities (only set for updates)
        if (this.id != null) {
            message.setId(this.id);
        }
        message.setEmployee(employee);
        message.setContent(this.content);
        message.setMonth(this.month);
        message.setYear(this.year);
        if (this.createdAt != null) {
            message.setCreatedAt(this.createdAt);
        } else {
            message.setCreatedAt(LocalDateTime.now());
        }
        message.setSent(this.sent);
        return message;
    }

    /**
     * Create DTO from Entity.
     * 
     * @param message Message entity
     * @return MessageDTO
     */
    public static MessageDTO fromEntity(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setEmployeeId(message.getEmployee().getId());
        dto.setEmployeeCode(message.getEmployee().getCode());
        dto.setEmployeeName(message.getEmployee().getFirstName() + " " + message.getEmployee().getLastName());
        dto.setContent(message.getContent());
        dto.setMonth(message.getMonth());
        dto.setYear(message.getYear());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setSent(message.isSent());
        return dto;
    }
}