package com.example.SAPA.controller;

import com.example.SAPA.Models.Medicine;
import com.example.SAPA.exceptions.EmptyCollectionException;
import com.example.SAPA.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @GetMapping
    public List<Medicine> getMedicines() throws EmptyCollectionException {
        return medicineService.findAll();
    }
    @GetMapping("/{name}")
    public Medicine getMedicineByName(@PathVariable String name) throws EmptyCollectionException {
        return medicineService.findByName(name);
    }
}
