package com.example.SAPA.service;

import com.example.SAPA.DTOs.FdaDrugDetailsDTO;
import com.example.SAPA.Models.Medicine;
import com.example.SAPA.Repositories.MedicineRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    public void validateMedicines() throws EmptyCollectionException {
        if(medicineRepository.count()==0) throw new EmptyCollectionException("No hay medicinas registradas");
    }
    public List<Medicine> findAll() throws EmptyCollectionException {
        validateMedicines();
        return medicineRepository.findAll();
    }
    public Medicine findById(Long id) throws EmptyCollectionException {
        validateMedicines();
        return medicineRepository.findById(id).orElseThrow(()->new EmptyCollectionException("Medicina no encontrada"));
    }
     public Medicine findByName(String name) throws EmptyCollectionException {
        validateMedicines();
        return medicineRepository.findAll().stream().filter(m ->m.getName().equals(name)).findFirst().orElseThrow(()->new EntityNotFoundException("Medicina no encontrada"));
     }
     public Medicine toEntity(FdaDrugDetailsDTO fda,Long id) throws EntityNotFoundException {
        return new Medicine(
                id,
                fda.getGenericName().get(id.intValue()),
                fda.getBrandName().get(id.intValue())
        );
     }
}
