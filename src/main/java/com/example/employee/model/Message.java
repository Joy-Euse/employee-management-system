package com.example.employee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity representing a message sent to an employee.
 */
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "sent", nullable = false)
    private boolean sent = false;

    /**
     * Creates a payslip approval message.
     * 
     * @param employee The employee to send the message to
     * @param payslip The approved payslip
     * @param institutionName The name of the institution
     * @return A new message entity
     */
    public static Message createPayslipApprovalMessage(Employee employee, Payslip payslip, String institutionName) {
        String messageContent = String.format(
            "Dear %s, your salary for %d/%d from %s amounting to %s has been credited to your account %s successfully.",
            employee.getFirstName(),
            payslip.getMonth(),
            payslip.getYear(),
            institutionName,
            payslip.getNetSalary().toString(),
            employee.getCode()
        );
        
        return new Message(null, employee, messageContent, payslip.getMonth(), payslip.getYear(), LocalDateTime.now(), false);
    }
}