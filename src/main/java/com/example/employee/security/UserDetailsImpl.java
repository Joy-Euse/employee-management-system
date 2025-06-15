package com.example.employee.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.employee.model.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of UserDetails interface for authentication.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String email;
    
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor for UserDetailsImpl.
     * 
     * @param id User ID
     * @param code Employee code
     * @param email User email
     * @param password User password
     * @param authorities User authorities
     */
    public UserDetailsImpl(Long id, String code, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.code = code;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Build UserDetailsImpl from Employee entity.
     * 
     * @param employee Employee entity
     * @return UserDetailsImpl
     */
    public static UserDetailsImpl build(Employee employee) {
        List<GrantedAuthority> authorities = employee.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                employee.getId(),
                employee.getCode(),
                employee.getEmail(),
                employee.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}