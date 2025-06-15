package com.example.employee.controller;

import com.example.employee.dto.MessageDTO;
import com.example.employee.service.MessageService;
import com.example.employee.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for message endpoints.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SecurityService securityService;

    /**
     * Get all messages.
     * 
     * @return List of message DTOs
     */
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        List<MessageDTO> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    /**
     * Get message by ID.
     * 
     * @param id Message ID
     * @return Message DTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isCurrentUser(#id))")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long id) {
        MessageDTO message = messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    /**
     * Get messages by employee ID.
     * 
     * @param employeeId Employee ID
     * @return List of message DTOs
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or (hasRole('EMPLOYEE') and @securityService.isCurrentUser(#employeeId))")
    public ResponseEntity<List<MessageDTO>> getMessagesByEmployeeId(@PathVariable Long employeeId) {
        List<MessageDTO> messages = messageService.getMessagesByEmployeeId(employeeId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get messages by month and year.
     * 
     * @param month Month
     * @param year Year
     * @return List of message DTOs
     */
    @GetMapping("/month/{month}/year/{year}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<MessageDTO>> getMessagesByMonthAndYear(
            @PathVariable Integer month, @PathVariable Integer year) {
        List<MessageDTO> messages = messageService.getMessagesByMonthAndYear(month, year);
        return ResponseEntity.ok(messages);
    }

    /**
     * Create a payslip approval message.
     * 
     * @param payslipId Payslip ID
     * @return Created message DTO
     */
    @PostMapping("/payslip/{payslipId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageDTO> createPayslipApprovalMessage(@PathVariable Long payslipId) {
        MessageDTO message = messageService.createPayslipApprovalMessage(payslipId);
        return ResponseEntity.ok(message);
    }

    /**
     * Send all unsent messages.
     * 
     * @return Number of messages sent
     */
    @PostMapping("/send-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> sendUnsentMessages() {
        int count = messageService.sendUnsentMessages();
        return ResponseEntity.ok(count);
    }

    /**
     * Send a specific message.
     * 
     * @param id Message ID
     * @return Sent message DTO
     */
    @PostMapping("/{id}/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable Long id) {
        MessageDTO message = messageService.sendMessage(id);
        return ResponseEntity.ok(message);
    }

    /**
     * Delete a message.
     * 
     * @param id Message ID
     * @return Response with no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}