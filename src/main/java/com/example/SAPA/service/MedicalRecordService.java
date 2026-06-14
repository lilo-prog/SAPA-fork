package com.example.SAPA.service;

import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.MedicalRecord.MedicalRecordEntity;
import com.example.SAPA.Models.Medicine;
import com.example.SAPA.Repositories.MedicalRecordRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final MedicineService medicineService;

    public void validateMedicalRecords() throws EmptyCollectionException {
        if(medicalRecordRepository.count()==0) throw new EmptyCollectionException("No hay fichas medicas");
    }

    private MedicalRecordEntity validateMedicines(Long medical_record_id) throws EmptyCollectionException {
        validateMedicalRecords();
        MedicalRecordEntity fichaMedica = medicalRecordRepository.findById(medical_record_id).orElseThrow(()->new EntityNotFoundException("Ficha medica no encontrada"));
        if(fichaMedica.getMedicines().isEmpty()) throw new EmptyCollectionException("No hay medicinas cargadas");
        return fichaMedica;
    }

    public List<Medicine> getMedicinesOfAMedicalRecord(Long medical_record_id) throws EmptyCollectionException {
        MedicalRecordEntity fichaMedica = validateMedicines(medical_record_id);
        return fichaMedica.getMedicines();
    }
    public List<MedicalRecordEntity> getAllMedicalRecords() throws EmptyCollectionException {
        validateMedicalRecords();
        return medicalRecordRepository.findAll();
    }
    @Transactional
    public MedicalRecordEntity save(MedicalRecordEntity medicalRecord){
        return medicalRecordRepository.save(medicalRecord);
    }

    @Transactional
    public void addMedicineToMedicalRecord(
            Long patientId,
            Long medicineId) throws EmptyCollectionException {

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(()-> new EntityNotFoundException("Paciente no encontrado"));

        MedicalRecordEntity record = patient.getMedicalRecord();

        Medicine medicine = medicineService.findById(medicineId);

        record.getMedicines().add(medicine);

        medicalRecordRepository.save(record);
    }
    @Transactional
    public void removeMedicineToMedicalRecord(
            Long patientId,
            Long medicineId) throws EmptyCollectionException {

        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(()-> new EntityNotFoundException("Paciente no encontrado"));

        MedicalRecordEntity record = patient.getMedicalRecord();

        Medicine medicine = medicineService.findById(medicineId);

        record.getMedicines().remove(medicine);

        medicalRecordRepository.save(record);
    }
}
