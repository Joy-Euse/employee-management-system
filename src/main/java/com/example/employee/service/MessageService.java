package com.example.employee.service;

import com.example.employee.dto.MessageDTO;
import com.example.employee.model.Employee;
import com.example.employee.model.Message;
import com.example.employee.model.Payslip;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.repository.MessageRepository;
import com.example.employee.repository.PayslipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing messages and sending email notifications.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.application.name}")
    private String institutionName;

    /**
     * Get all messages.
     * 
     * @return List of message DTOs
     */
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get message by ID.
     * 
     * @param id Message ID
     * @return Message DTO
     */
    public MessageDTO getMessageById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        return MessageDTO.fromEntity(message);
    }

    /**
     * Get messages by employee ID.
     * 
     * @param employeeId Employee ID
     * @return List of message DTOs
     */
    public List<MessageDTO> getMessagesByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        return messageRepository.findByEmployee(employee).stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get messages by month and year.
     * 
     * @param month Month
     * @param year Year
     * @return List of message DTOs
     */
    public List<MessageDTO> getMessagesByMonthAndYear(Integer month, Integer year) {
        return messageRepository.findByMonthAndYear(month, year).stream()
                .map(MessageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Create a payslip approval message.
     * 
     * @param payslipId Payslip ID
     * @return Created message DTO
     */
    @Transactional
    public MessageDTO createPayslipApprovalMessage(Long payslipId) {
        Payslip payslip = payslipRepository.findById(payslipId)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + payslipId));
        
        if (payslip.getStatus() != Payslip.PayslipStatus.PAID) {
            throw new RuntimeException("Cannot create message for unapproved payslip");
        }
        
        Message message = Message.createPayslipApprovalMessage(
                payslip.getEmployee(), payslip, institutionName);
        
        Message savedMessage = messageRepository.save(message);
        return MessageDTO.fromEntity(savedMessage);
    }

    /**
     * Send all unsent messages.
     * 
     * @return Number of messages sent
     */
    @Transactional
    public int sendUnsentMessages() {
        List<Message> unsentMessages = messageRepository.findBySentFalse();
        
        for (Message message : unsentMessages) {
            sendEmail(message);
            message.setSent(true);
            messageRepository.save(message);
        }
        
        return unsentMessages.size();
    }

    /**
     * Send a specific message.
     * 
     * @param id Message ID
     * @return Sent message DTO
     */
    @Transactional
    public MessageDTO sendMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        
        if (message.isSent()) {
            throw new RuntimeException("Message is already sent");
        }
        
        sendEmail(message);
        message.setSent(true);
        Message updatedMessage = messageRepository.save(message);
        
        return MessageDTO.fromEntity(updatedMessage);
    }

    /**
     * Send email for a message.
     * 
     * @param message Message
     */
    private void sendEmail(Message message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(message.getEmployee().getEmail());
        email.setSubject("Salary Payment Notification - " + message.getMonth() + "/" + message.getYear());
        email.setText(message.getContent());
        
        emailSender.send(email);
    }

    /**
     * Delete a message.
     * 
     * @param id Message ID
     */
    @Transactional
    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
    }
}