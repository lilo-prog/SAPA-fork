package com.example.SAPA.service;

import com.example.SAPA.DTOs.FdaDrugDetailsDTO;
import com.example.SAPA.DTOs.FdaResultDTO;
import com.example.SAPA.Models.Medicine;
import com.example.SAPA.Repositories.MedicalRecordRepository;
import com.example.SAPA.Repositories.MedicineRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private FdaService fdaService;

    public void validateMedicines() throws EmptyCollectionException {
        if(medicineRepository.count()==0) throw new EmptyCollectionException("No hay medicinas registradas");
    }
    public List<Medicine> findAll() throws EmptyCollectionException {
        validateMedicines();
        return medicineRepository.findAll();
    }
    public Medicine findById(Long id) throws EmptyCollectionException {
        validateMedicines();
        return medicineRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Medicina no encontrada"));
    }
     public Medicine findByName(String name) throws EmptyCollectionException {
        validateMedicines();
        return medicineRepository.findAll().
                stream().
                filter(m ->m.getName().equals(name)).
                findFirst().
                orElseThrow(
                        ()-> new EntityNotFoundException("Medicina no encontrada")
                );
     }
    private Medicine toMedicine(FdaResultDTO result) {
        Medicine medicine = new Medicine();

        FdaDrugDetailsDTO details = result.getOpenfda();

        if (details != null) {
            medicine.setName(getFirst(details.getGenericName()));
            medicine.setBrand(getFirst(details.getBrandName()));
        }

        return medicine;
    }

    private String getFirst(List<String> list) {
        return list != null && !list.isEmpty()
                ? list.get(0)
                : null;
    }
    @Transactional
    public void persistAllMedicines() {
        List<FdaResultDTO> fdaResults =
                fdaService.getAllMedicines();

        List<Medicine> medicines = fdaResults.stream()
                .map(this::toMedicine)
                .filter(Objects::nonNull)
                .toList();

        medicineRepository.saveAll(medicines);
    }


}
