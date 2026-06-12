package com.example.SAPA.DTOs;

import com.example.SAPA.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private List<Long> specialities;
}
