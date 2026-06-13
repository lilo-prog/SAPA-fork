package com.example.SAPA.DTOs;

import com.example.SAPA.DTOs.Request.UserDTORequest;
import com.example.SAPA.DTOs.Response.UserDTOResponse;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.enums.AccountStatus;
import com.example.SAPA.enums.UserCategory;

public class UserMapper {

    public static UserEntity toEntity(UserDTORequest userDTORequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTORequest.getEmail());
        userEntity.setPassword(userDTORequest.getPassword());
        userEntity.setStatus(AccountStatus.valueOf(userDTORequest.getStatus()));
        userEntity.setRole(UserCategory.valueOf(userDTORequest.getRole()));
        return userEntity;
    }

    public static UserDTOResponse fromEntity(UserEntity user){
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setId(user.getId());
        userDTOResponse.setEmail(user.getEmail());
        userDTOResponse.setRole(user.getRole().name());
        userDTOResponse.setStatus(user.getStatus().name());
        userDTOResponse.setCreatedAt(user.getCreatedAt());
        return userDTOResponse;
    }
}
