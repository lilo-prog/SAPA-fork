package com.example.SAPA.Repositories;

import com.example.SAPA.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

}
