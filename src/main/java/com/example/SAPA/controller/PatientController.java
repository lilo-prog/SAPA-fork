package com.example.SAPA.controller;

import com.example.SAPA.DTOs.Mappers.UserMapper;
import com.example.SAPA.DTOs.Request.PatientDTORequest;
import com.example.SAPA.DTOs.Response.PatientDTOResponse;
import com.example.SAPA.DTOs.Response.UserDTOResponse;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.exceptions.EmptyCollectionException;
import com.example.SAPA.service.PatientService;
import com.example.SAPA.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/patients")
@CrossOrigin("*")
public class PatientController {
        // Atributo.
    private final PatientService patientService;

        //Método constructor.
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

        //Métodos propios.
    @GetMapping
    public List<PatientDTOResponse> getAllPatients() throws EmptyCollectionException {return patientService.getAllPatients();}

    @GetMapping("/{id}")
    public Optional<PatientDTOResponse> getPatientById(@PathVariable Long id) throws EmptyCollectionException {
        return patientService.getPatientById(id);
    }
    @PostMapping
    public String savePatient(@RequestBody PatientDTORequest patientDTORequest) throws EmptyCollectionException {
        return patientService.savePatient(patientDTORequest);
    }
    @PutMapping("/{id}")
    public String updatePatient(@PathVariable Long id,@RequestBody PatientEntity patientEntity) throws EmptyCollectionException {
        return patientService.updatePatient(patientEntity);
    }
}
