package com.example.SAPA.service;

import com.example.SAPA.Models.SpecialityEntity;
import com.example.SAPA.Repositories.SpecialityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    public SpecialityService(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    public List<SpecialityEntity> getAll() {
        return specialityRepository.findAll();
    }
}
