package com.example.SAPA.DTOs;

import com.example.SAPA.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    private String email;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String licenseNumber;
}
