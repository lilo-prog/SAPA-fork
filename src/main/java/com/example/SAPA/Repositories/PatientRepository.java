package com.example.SAPA.Repositories;

import com.example.SAPA.Models.Entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

}
