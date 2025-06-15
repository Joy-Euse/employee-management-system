package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for authentication responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String code;
    private String email;
    private List<String> roles;

    /**
     * Constructor with token and user details.
     * 
     * @param token JWT token
     * @param id User ID
     * @param code Employee code
     * @param email User email
     * @param roles User roles
     */
    public AuthResponse(String token, Long id, String code, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.code = code;
        this.email = email;
        this.roles = roles;
    }
}