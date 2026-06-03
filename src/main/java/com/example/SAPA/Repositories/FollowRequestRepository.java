package com.example.SAPA.Repositories;

import com.example.SAPA.Models.FollowRequestEntity;
import com.example.SAPA.enums.FollowRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRequestRepository extends JpaRepository<FollowRequestEntity, Long> {
    List<FollowRequestEntity> findByPatientId(Long patientId);
    List<FollowRequestEntity> findByDoctorId(Long doctorId);
    Optional<FollowRequestEntity> findByPatientIdAndDoctorIdAndStatus(Long patientId, Long doctorId, FollowRequestStatus status);
}
