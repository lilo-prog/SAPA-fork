package com.example.SAPA.service;

import com.example.SAPA.DTOs.FdaDrugDetailsDTO;
import com.example.SAPA.DTOs.FdaResponseDTO;
import com.example.SAPA.DTOs.FdaResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FdaService {
    //Atributos.
    private final RestTemplate restTemplate = new RestTemplate();
    private static String BASE_URL = "https://api.fda.gov";

    //Métodos
    public FdaResponseDTO searchForMedicationByName(String name){
        String url = BASE_URL + "/drug/label.json?search=openfda.brand_name:\"" +name+ "\"&limit=5";

        try{
            return restTemplate.getForObject(url, FdaResponseDTO.class);

        }catch (Exception e){
            System.out.println("Error al conectar con openFDA: "+e.getMessage());
            return null;
        }
    }
    public List<FdaResultDTO> getMedicinesPage(int limit, int skip) {
        String url = String.format(
                "%s/drug/label.json?limit=%d&skip=%d",
                BASE_URL,
                limit,
                skip
        );

        FdaResponseDTO response =
                restTemplate.getForObject(url, FdaResponseDTO.class);

        return response != null && response.getResults() != null
                ? response.getResults()
                : Collections.emptyList();
    }
    public List<FdaResultDTO> getAllMedicines() {
        List<FdaResultDTO> medicines = new ArrayList<>();

        int skip = 0;
        final int limit = 100;

        while (true) {
            List<FdaResultDTO> page = getMedicinesPage(limit, skip);

            if (page.isEmpty()) {
                break;
            }

            medicines.addAll(page);
            skip += limit;
        }

        return medicines;
    }
}
