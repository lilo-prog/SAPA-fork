package com.example.SAPA.DTOs.Request;

import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.enums.AccountStatus;
import com.example.SAPA.enums.UserCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTORequest {
    private String email;
    private String password;
    private String status;
    private String role;


}
