package com.example.SAPA.service;

import com.example.SAPA.Models.HealthTipEntity;
import com.example.SAPA.Repositories.HealthTipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthTipService {

    @Autowired
    private HealthTipRepository healthTipRepository;

    public HealthTipEntity create(HealthTipEntity healthTip){
        System.out.println("Health Tip created" + healthTip.getTitle());

        return healthTip;
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
        // añadir verificacion de si hay relacion medico/paciente
        return true;
    }
}
