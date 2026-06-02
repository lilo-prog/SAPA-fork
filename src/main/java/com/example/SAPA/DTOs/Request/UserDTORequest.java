package com.example.SAPA.DTOs.Request;

import com.example.SAPA.entities.UserEntity;
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

    public UserEntity toEntity(UserDTORequest userDTORequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTORequest.getEmail());
        userEntity.setPassword(userDTORequest.getPassword());
        userEntity.setStatus(AccountStatus.valueOf(userDTORequest.getStatus()));
        userEntity.setRole(UserCategory.valueOf(userDTORequest.getRole()));
        return userEntity;
    }
}
