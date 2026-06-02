package com.example.SAPA.DTOs.Response;

import com.example.SAPA.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTOResponse {
    private Long id;
    private String email;
    private String role;
    private String status;
    private LocalDateTime createdAt;

    public UserDTOResponse roResponseDTO(UserEntity user){
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setId(user.getId());
        userDTOResponse.setEmail(user.getEmail());
        userDTOResponse.setRole(user.getRole().name());
        userDTOResponse.setStatus(user.getStatus().name());
        userDTOResponse.setCreatedAt(user.getCreatedAt());
        return userDTOResponse;
    }
}

