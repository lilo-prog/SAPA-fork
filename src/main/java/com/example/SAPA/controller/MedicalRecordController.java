package com.example.SAPA.controller;

import com.example.SAPA.Models.Medicine;
import com.example.SAPA.exceptions.EmptyCollectionException;
import com.example.SAPA.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/{id}")
    public List<Medicine> getMedicinesOfAMedicalRecord(@PathVariable Long id) throws EmptyCollectionException {
        return medicalRecordService.getMedicinesOfAMedicalRecord(id);
    }
}
