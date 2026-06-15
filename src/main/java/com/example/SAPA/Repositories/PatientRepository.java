package com.example.SAPA.Repositories;

import com.example.SAPA.DTOs.Response.UserDTOResponse;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    @Query(value = "SELECT user_id,email,role,status,createdAt FROM user u WHERE u.user_id = :user_id",
    nativeQuery = true)
    public Optional<UserDTOResponse> findByUserId(@Param("user_id") Long id);
}
