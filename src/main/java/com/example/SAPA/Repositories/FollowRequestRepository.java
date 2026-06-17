package com.example.SAPA.Repositories;

import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.FollowRequestEntity;
import com.example.SAPA.enums.FollowRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequestEntity, Long> {
    boolean existsByPatientAndDoctorAndStatus(PatientEntity patient, DoctorEntity doctor, FollowRequestStatus status);

    boolean existsByDoctorIdAndPatientIdAndStatus(Long doctorId, Long patientId, FollowRequestStatus status);

    List<FollowRequestEntity> findByDoctorAndStatus(DoctorEntity doctor, FollowRequestStatus status);

    List<FollowRequestEntity> findByPatient(PatientEntity patient);
    @Query("SELECT COUNT(f) > 0 FROM FollowRequestEntity f WHERE f.doctor.id = :doctorId AND f.patient.id = :pacienteId")
    boolean existsByMedicoIdAndPacienteId(@Param("doctorId")Long doctorId,@Param("pacienteId") Long paciente_id);
}
