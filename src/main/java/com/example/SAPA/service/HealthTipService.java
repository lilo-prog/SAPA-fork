package com.example.SAPA.service;

import com.example.SAPA.Models.HealthTipEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthTipService {
    public HealthTipEntity createHealthTip(HealthTipEntity healthTip){
        System.out.println("Health Tip created" + healthTip.getTitle());

        return healthTip;
    }

    public List<HealthTipEntity> getHealthTipsByDoctor(Long doctorId){
        return new ArrayList<>();
    }

    public boolean canPatientViewHealthTips(Long patientId, Long doctorId){
        // añadir verificacion de si hay relacion medico/paciente
        return true;
    }
}
