package com.example.employee.repository;

import com.example.employee.model.Employee;
import com.example.employee.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Message entities.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Find all messages for a specific employee.
     * 
     * @param employee The employee to find messages for
     * @return A list of messages for the employee
     */
    List<Message> findByEmployee(Employee employee);

    /**
     * Find all messages for a specific month and year.
     * 
     * @param month The month to search for
     * @param year The year to search for
     * @return A list of messages for the month and year
     */
    List<Message> findByMonthAndYear(Integer month, Integer year);

    /**
     * Find all messages for a specific employee, month, and year.
     * 
     * @param employee The employee to find messages for
     * @param month The month to search for
     * @param year The year to search for
     * @return A list of messages for the employee, month, and year
     */
    List<Message> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

    /**
     * Find all unsent messages.
     * 
     * @return A list of unsent messages
     */
    List<Message> findBySentFalse();

    /**
     * Find all unsent messages for a specific employee.
     * 
     * @param employee The employee to find unsent messages for
     * @return A list of unsent messages for the employee
     */
    List<Message> findByEmployeeAndSentFalse(Employee employee);
}