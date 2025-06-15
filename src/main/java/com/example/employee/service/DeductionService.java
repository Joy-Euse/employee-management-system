package com.example.employee.service;

import com.example.employee.dto.DeductionDTO;
import com.example.employee.model.Deduction;
import com.example.employee.repository.DeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing deductions.
 */
@Service
public class DeductionService {

    @Autowired
    private DeductionRepository deductionRepository;

    /**
     * Get all deductions.
     * 
     * @return List of deduction DTOs
     */
    public List<DeductionDTO> getAllDeductions() {
        return deductionRepository.findAll().stream()
                .map(DeductionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get deduction by ID.
     * 
     * @param id Deduction ID
     * @return Deduction DTO
     */
    public DeductionDTO getDeductionById(Long id) {
        Deduction deduction = deductionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deduction not found with id: " + id));
        return DeductionDTO.fromEntity(deduction);
    }

    /**
     * Get deduction by code.
     * 
     * @param code Deduction code
     * @return Deduction DTO
     */
    public DeductionDTO getDeductionByCode(String code) {
        Deduction deduction = deductionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Deduction not found with code: " + code));
        return DeductionDTO.fromEntity(deduction);
    }

    /**
     * Get deduction by name.
     * 
     * @param name Deduction name
     * @return Deduction DTO
     */
    public DeductionDTO getDeductionByName(String name) {
        Deduction deduction = deductionRepository.findByDeductionName(name)
                .orElseThrow(() -> new RuntimeException("Deduction not found with name: " + name));
        return DeductionDTO.fromEntity(deduction);
    }

    /**
     * Create a new deduction.
     * 
     * @param deductionDTO Deduction data
     * @return Created deduction DTO
     */
    @Transactional
    public DeductionDTO createDeduction(DeductionDTO deductionDTO) {
        if (deductionRepository.existsByCode(deductionDTO.getCode())) {
            throw new RuntimeException("Deduction code is already in use!");
        }

        if (deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new RuntimeException("Deduction name is already in use!");
        }

        Deduction deduction = deductionDTO.toEntity();
        Deduction savedDeduction = deductionRepository.save(deduction);
        
        return DeductionDTO.fromEntity(savedDeduction);
    }

    /**
     * Update an existing deduction.
     * 
     * @param id Deduction ID
     * @param deductionDTO Deduction data
     * @return Updated deduction DTO
     */
    @Transactional
    public DeductionDTO updateDeduction(Long id, DeductionDTO deductionDTO) {
        Deduction deduction = deductionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deduction not found with id: " + id));

        // Check if code is being changed and if it's already in use
        if (!deduction.getCode().equals(deductionDTO.getCode()) && 
                deductionRepository.existsByCode(deductionDTO.getCode())) {
            throw new RuntimeException("Deduction code is already in use!");
        }

        // Check if name is being changed and if it's already in use
        if (!deduction.getDeductionName().equals(deductionDTO.getDeductionName()) && 
                deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new RuntimeException("Deduction name is already in use!");
        }

        // Update deduction fields
        deduction.setCode(deductionDTO.getCode());
        deduction.setDeductionName(deductionDTO.getDeductionName());
        deduction.setPercentage(deductionDTO.getPercentage());

        Deduction updatedDeduction = deductionRepository.save(deduction);
        return DeductionDTO.fromEntity(updatedDeduction);
    }

    /**
     * Delete a deduction.
     * 
     * @param id Deduction ID
     */
    @Transactional
    public void deleteDeduction(Long id) {
        if (!deductionRepository.existsById(id)) {
            throw new RuntimeException("Deduction not found with id: " + id);
        }
        deductionRepository.deleteById(id);
    }

    /**
     * Initialize standard deductions if they don't exist.
     */
    @Transactional
    public void initializeStandardDeductions() {
        if (deductionRepository.count() == 0) {
            for (Deduction deduction : Deduction.createStandardDeductions()) {
                deductionRepository.save(deduction);
            }
        }
    }
}