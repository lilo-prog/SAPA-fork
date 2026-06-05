package com.example.SAPA.Repositories;

import com.example.SAPA.Models.Entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Long>{
    Optional<DoctorEntity> findByUserId(Long userId);
}
