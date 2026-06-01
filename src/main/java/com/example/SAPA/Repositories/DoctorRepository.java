package com.example.SAPA.Repositories;

import com.example.SAPA.entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long>{
}
