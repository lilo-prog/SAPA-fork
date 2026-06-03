package com.example.SAPA.service;

import com.example.SAPA.Models.HealthTipEntity;
import com.example.SAPA.Repositories.FollowRequestRepository;
import com.example.SAPA.Repositories.HealthTipRepository;
import com.example.SAPA.enums.FollowRequestStatus;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class HealthTipService {
    private final HealthTipRepository healthTipRepository;
    private final FollowRequestRepository followRequestRepository;

    public HealthTipService(HealthTipRepository healthTipRepository, FollowRequestRepository followRequestRepository) {
        this.healthTipRepository = healthTipRepository;
        this.followRequestRepository = followRequestRepository;
    }

    public HealthTipEntity create(HealthTipEntity healthTip){
        return healthTipRepository.save(healthTip);
    }

    public List<HealthTipEntity> getAll(){
        return healthTipRepository.findAll();
    }

    public HealthTipEntity update(Long id, HealthTipEntity healthTip){
        HealthTipEntity existing = healthTipRepository.findById(id).orElseThrow(() -> new RuntimeException("Consejo no encontrado"));

        existing.setTitle(healthTip.getTitle());
        existing.setContent(healthTip.getContent());

        return healthTipRepository.save(existing);
    }

    public void delete(Long id){
        healthTipRepository.deleteById(id);
    }

    public boolean canPatientViewHealthTips(Long patientId, Long doctorId){
        return followRequestRepository.findByPatientIdAndDoctorIdAndStatus(patientId, doctorId, FollowRequestStatus.APPROVED).isPresent();
    }

    public List<HealthTipEntity> getVisibleHealthTips(Long patientId, Long doctorId){
        boolean approved = canPatientViewHealthTips(patientId, doctorId);

        if(!approved){
            return Collections.emptyList();
        }

        return healthTipRepository.findByDoctorId(doctorId);
    }
}